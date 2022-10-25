package com.weiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Avatar;
import com.weiho.scaffold.system.entity.criteria.AvatarQueryCriteria;
import com.weiho.scaffold.system.entity.vo.AvatarEnabledVO;
import com.weiho.scaffold.system.entity.vo.AvatarVO;
import com.weiho.scaffold.system.mapper.AvatarMapper;
import com.weiho.scaffold.system.service.AvatarService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 用户头像表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-07-29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AvatarServiceImpl extends CommonServiceImpl<AvatarMapper, Avatar> implements AvatarService {

    @Override
    @Cacheable(value = "Scaffold:Commons:Avatar", key = "'loadAvatarByUsername:' + #p1", unless = "#result == null")
    public Avatar selectByAvatarId(Long avatarId, String username) {
        return this.getBaseMapper().selectById(avatarId);
    }

    @Override
    @CachePut(value = "Scaffold:Commons:Avatar", key = "'loadAvatarByUsername:' + #p1")
    public Avatar updateAvatar(Avatar avatar, String username) {
        this.getBaseMapper().updateById(avatar);
        return avatar;
    }

    @Override
    public Map<String, Object> selectAvatarList(AvatarQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<AvatarVO> pageInfo = new PageInfo<>(getAll(criteria));
        return PageUtils.toPageContainer(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public void download(List<AvatarVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AvatarVO avatarVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", avatarVO.getUsername());
            map.put("文件名", avatarVO.getRealName());
            map.put("头像路径", avatarVO.getPath());
            map.put("头像大小", avatarVO.getSize());
            map.put("审核情况", avatarVO.getEnabled().getDisplay());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public List<AvatarVO> getAll(AvatarQueryCriteria criteria) {
        List<AvatarVO> avatarVOS;
        if (criteria.getCreateTime() != null && criteria.getCreateTime().size() > 0) {
            if (criteria.getEnabled() != null) {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), criteria.getCreateTime().get(0), criteria.getCreateTime().get(1), criteria.getEnabled().getKey());
            } else {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), criteria.getCreateTime().get(0), criteria.getCreateTime().get(1), null);
            }
        } else {
            if (criteria.getEnabled() != null) {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), null, null, criteria.getEnabled().getKey());
            } else {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), null, null, null);
            }
        }
        return avatarVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(AvatarEnabledVO avatarEnabledVO) {
        this.lambdaUpdate().set(Avatar::getEnabled, avatarEnabledVO.getEnabled()).eq(Avatar::getId, avatarEnabledVO.getId())
                .eq(Avatar::getIsDel, 0).update();
    }
}
