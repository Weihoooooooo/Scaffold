package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.aes.AesUtils;
import com.weiho.scaffold.common.util.cipher.LikeCipher;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Owner;
import com.weiho.scaffold.system.entity.convert.OwnerVOConvert;
import com.weiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
import com.weiho.scaffold.system.entity.vo.OwnerVO;
import com.weiho.scaffold.system.mapper.OwnerMapper;
import com.weiho.scaffold.system.service.OwnerService;
import com.weiho.scaffold.tools.mail.util.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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
        criteria.setBlurry(LikeCipher.likeEncrypt(criteria.getBlurry()));
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
        List<Map<String, Object>> list = new ArrayList<>();
        for (OwnerVO ownerVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", ownerVO.getName());
            map.put("性别", ownerVO.getSex().getDisplay());
            map.put("手机号码", ownerVO.getPhone());
            map.put("邮箱", ownerVO.getEmail());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOwner(OwnerVO ownerVO) {
        if (this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getIdentityId, AesUtils.encrypt(ownerVO.getIdentityId()))) != null) {
            throw new BadRequestException(I18nMessagesUtils.get("identity.exist.tip"));
        }
        if (this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getEmail, AesUtils.encrypt(ownerVO.getEmail()))) != null) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        }
        if (this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getPhone, LikeCipher.phoneLikeEncrypt(ownerVO.getPhone()))) != null) {
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
        // 根据ID查找
        Owner owner = this.getById(ownerVO.getId());
        // 验证邮箱类型
        MailUtils.checkEmail(ownerVO.getEmail());
        // 根据身份证号码查找
        Owner ownerIdentityId = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getIdentityId, AesUtils.encrypt(ownerVO.getIdentityId())));
        // 根据邮箱查找
        Owner ownerEmail = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getEmail, AesUtils.encrypt(ownerVO.getEmail())));
        // 根据手机号查找
        Owner ownerPhone = this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getPhone, LikeCipher.phoneLikeEncrypt(ownerVO.getPhone())));

        if (ownerIdentityId != null && !owner.getId().equals(ownerIdentityId.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("identity.exist.tip"));
        }

        if (ownerEmail != null && !owner.getId().equals(ownerEmail.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        }

        if (ownerPhone != null && !owner.getId().equals(ownerPhone.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("phone.exist.tip"));
        }
        return this.saveOrUpdate(ownerVOConvert.toEntity(ownerVO));
    }

    @Override
    public void resetPassword(Serializable id) {
        Owner owner = this.getById(id);
        if (owner != null) {
            String initPass = owner.getIdentityId().substring(owner.getIdentityId().length() - 6);
            this.lambdaUpdate().set(Owner::getPassword, initPass, "typeHandler=com.weiho.scaffold.mp.handler.EncryptHandler")
                    .eq(Owner::getId, id).eq(Owner::getIsDel, 0).update();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOwner(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
