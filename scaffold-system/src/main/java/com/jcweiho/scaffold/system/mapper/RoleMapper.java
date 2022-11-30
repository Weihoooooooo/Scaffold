package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Mapper
@Repository
public interface RoleMapper extends CommonMapper<Role> {
    /**
     * 根据用户ID查找用户的角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    Set<Role> findSetByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询所有的角色信息
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    List<Role> findListByUserId(@Param("userId") Long userId);
}
