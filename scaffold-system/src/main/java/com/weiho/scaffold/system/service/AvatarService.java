package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Avatar;
import com.weiho.scaffold.system.entity.criteria.AvatarQueryCriteria;
import com.weiho.scaffold.system.entity.vo.AvatarEnabledVO;
import com.weiho.scaffold.system.entity.vo.AvatarVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户头像表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-07-29
 */
public interface AvatarService extends CommonService<Avatar> {
    /**
     * 根据头像ID查询头像信息
     *
     * @param avatarId 头像ID
     * @param username 当前登录的用户名
     * @return 头像实体
     */
    Avatar selectByAvatarId(Long avatarId, String username);

    /**
     * 修改头像
     *
     * @param avatar 头像
     */
    @SuppressWarnings("all")
    Avatar updateAvatar(Avatar avatar, String username);

    /**
     * 查询头像列表(可模糊查询)
     *
     * @param criteria 查询实体
     * @return AvatarVO
     */
    Map<String, Object> selectAvatarList(AvatarQueryCriteria criteria, Pageable pageable);

    /**
     * 导出头像信息
     *
     * @param all      查询结果
     * @param response 响应
     */
    void download(List<AvatarVO> all, HttpServletResponse response) throws IOException;

    /**
     * 根据条件查询所有头像列表
     *
     * @param criteria 查询实体
     * @return List
     */
    List<AvatarVO> getAll(AvatarQueryCriteria criteria);

    /**
     * 删除用户头像
     *
     * @param ids 头像ID
     */
    boolean deleteAvatar(Set<Long> ids);

    /**
     * 修改用户头像状态
     *
     * @param avatarEnabledVO 实体
     */
    boolean updateEnabled(AvatarEnabledVO avatarEnabledVO);
}
