package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jcweiho.scaffold.common.util.CollUtils;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Avatar;
import com.jcweiho.scaffold.system.entity.criteria.AvatarQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.AvatarEnabledVO;
import com.jcweiho.scaffold.system.entity.vo.AvatarVO;
import com.jcweiho.scaffold.system.mapper.AvatarMapper;
import com.jcweiho.scaffold.system.service.AvatarService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return toPageContainer(getAll(criteria));
    }

    @Override
    public void download(List<AvatarVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (AvatarVO avatarVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.avatar.username"), avatarVO.getUsername());
            map.put(I18nMessagesUtils.get("download.avatar.filename"), avatarVO.getRealName());
            map.put(I18nMessagesUtils.get("download.avatar.path"), avatarVO.getPath());
            map.put(I18nMessagesUtils.get("download.avatar.size"), avatarVO.getSize());
            map.put(I18nMessagesUtils.get("download.avatar.enabled"), avatarVO.getEnabled().getDisplay());
            map.put(I18nMessagesUtils.get("download.createTime"), avatarVO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public List<AvatarVO> getAll(AvatarQueryCriteria criteria) {
        List<AvatarVO> avatarVOS;
        if (CollUtils.isNotEmpty(criteria.getCreateTime())) {
            if (ObjectUtil.isNotNull(criteria.getEnabled())) {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), criteria.getCreateTime().get(0), criteria.getCreateTime().get(1), criteria.getEnabled().getKey());
            } else {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), criteria.getCreateTime().get(0), criteria.getCreateTime().get(1), null);
            }
        } else {
            if (ObjectUtil.isNotNull(criteria.getEnabled())) {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), null, null, criteria.getEnabled().getKey());
            } else {
                avatarVOS = this.getBaseMapper().selectAvatarList(criteria.getBlurry(), null, null, null);
            }
        }
        return avatarVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAvatar(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(AvatarEnabledVO avatarEnabledVO) {
        IdSecureUtils.verifyIdNotNull(avatarEnabledVO.getId());
        return this.lambdaUpdate().set(Avatar::getEnabled, avatarEnabledVO.getEnabled()).eq(Avatar::getId, avatarEnabledVO.getId())
                .eq(Avatar::getIsDel, 0).update();
    }
}
