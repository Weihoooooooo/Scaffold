package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Owner;
import com.jcweiho.scaffold.system.entity.convert.OwnerVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.OwnerVO;
import com.jcweiho.scaffold.system.mapper.OwnerMapper;
import com.jcweiho.scaffold.system.service.OwnerService;
import com.jcweiho.scaffold.tools.mail.util.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业主表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OwnerServiceImpl extends CommonServiceImpl<OwnerMapper, Owner> implements OwnerService {
    private final OwnerVOConvert ownerVOConvert;

    @Override
    public List<Owner> findAll(OwnerQueryCriteria criteria) {
        criteria.setBlurry(LikeCipherUtils.likeEncrypt(criteria.getBlurry()));
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Owner.class, criteria)));
    }

    @Override
    public Map<String, Object> getOwnerList(OwnerQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Owner> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(ownerVOConvert.toPojo(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(List<OwnerVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (OwnerVO ownerVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.owner.username"), ownerVO.getName());
            map.put(I18nMessagesUtils.get("download.sex"), ownerVO.getSex().getDisplay());
            map.put(I18nMessagesUtils.get("download.phone"), ownerVO.getPhone());
            map.put(I18nMessagesUtils.get("download.email"), ownerVO.getEmail());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOwner(OwnerVO ownerVO) {
        IdSecureUtils.verifyIdNull(ownerVO.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getIdentityId, AesUtils.encrypt(ownerVO.getIdentityId()))))) {
            throw new BadRequestException(I18nMessagesUtils.get("identity.exist.tip"));
        }
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getEmail, AesUtils.encrypt(ownerVO.getEmail()))))) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        }
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getPhone, LikeCipherUtils.phoneLikeEncrypt(ownerVO.getPhone()))))) {
            throw new BadRequestException(I18nMessagesUtils.get("phone.exist.tip"));
        }
        MailUtils.checkEmail(ownerVO.getEmail());
        Owner resource = ownerVOConvert.toEntity(ownerVO);
        // 业主默认密码为身份证号后六位
        String initPass = ownerVO.getIdentityId().substring(ownerVO.getIdentityId().length() - 6);
        resource.setPassword(initPass);
        return this.save(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOwner(OwnerVO ownerVO) {
        IdSecureUtils.verifyIdNotNull(ownerVO.getId());
        // 根据ID查找
        Owner owner = this.getById(ownerVO.getId());
        // 验证邮箱类型
        MailUtils.checkEmail(ownerVO.getEmail());
        // 根据身份证号码查找
        Owner ownerIdentityId = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getIdentityId, AesUtils.encrypt(ownerVO.getIdentityId())));
        // 根据邮箱查找
        Owner ownerEmail = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getEmail, AesUtils.encrypt(ownerVO.getEmail())));
        // 根据手机号查找
        Owner ownerPhone = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getPhone, LikeCipherUtils.phoneLikeEncrypt(ownerVO.getPhone())));

        if (ObjectUtil.isNotNull(ownerIdentityId) && !owner.getId().equals(ownerIdentityId.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("identity.exist.tip"));
        }

        if (ObjectUtil.isNotNull(ownerEmail) && !owner.getId().equals(ownerEmail.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        }

        if (ObjectUtil.isNotNull(ownerPhone) && !owner.getId().equals(ownerPhone.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("phone.exist.tip"));
        }
        return this.saveOrUpdate(ownerVOConvert.toEntity(ownerVO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Serializable id) {
        IdSecureUtils.verifyIdNotNull((Long) id);
        Owner owner = this.getById(id);
        if (ObjectUtil.isNotNull(owner)) {
            String initPass = owner.getIdentityId().substring(owner.getIdentityId().length() - 6);
            return this.lambdaUpdate().set(Owner::getPassword, initPass, "typeHandler=com.jcweiho.scaffold.mp.handler.EncryptHandler")
                    .eq(Owner::getId, id).eq(Owner::getIsDel, 0).update();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOwner(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
