package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public List<OwnerVO> findAll(OwnerQueryCriteria criteria) {
        criteria.setBlurry(LikeCipher.likeEncrypt(criteria.getBlurry()));
        List<Owner> owners = this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Owner.class, criteria)));
        return ownerVOConvert.toPojo(owners);
    }

    @Override
    public Map<String, Object> getOwnerList(OwnerQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<OwnerVO> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(pageInfo.getList(), pageInfo.getTotal());
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
        if (this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getIdentityId, ownerVO.getIdentityId())) != null) {
            throw new BadRequestException(I18nMessagesUtils.get("identity.exist.tip"));
        }
        if (this.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getEmail, ownerVO.getEmail())) != null) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        }
        MailUtils.checkEmail(ownerVO.getEmail());
        Owner resource = ownerVOConvert.toEntity(ownerVO);
        // 业主默认密码为手机号
        resource.setPassword(ownerVO.getPhone());
        return this.save(resource);
    }
}
