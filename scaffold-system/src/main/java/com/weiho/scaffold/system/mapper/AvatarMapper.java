package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.Avatar;
import com.weiho.scaffold.system.entity.vo.AvatarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 用户头像表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-07-29
 */
@Mapper
@Repository
public interface AvatarMapper extends CommonMapper<Avatar> {
    /**
     * 查询头像列表(usernameLike为空则全查)
     *
     * @param usernameLike    模糊查询
     * @param createTimeStart 创建时间区间
     * @param createTimeEnd   创建时间区间
     * @param enabled         状态
     * @return List
     */
    List<AvatarVO> selectAvatarList(@Param("usernameLike") String usernameLike,
                                    @Param("createTimeStart") Timestamp createTimeStart,
                                    @Param("createTimeEnd") Timestamp createTimeEnd,
                                    @Param("enabled") Integer enabled);

    /**
     * 修改用户头像状态
     *
     * @param id      主键ID
     * @param enabled 头像状态
     */
    void updateEnabled(@Param("id") Long id, @Param("enabled") Integer enabled);
}
