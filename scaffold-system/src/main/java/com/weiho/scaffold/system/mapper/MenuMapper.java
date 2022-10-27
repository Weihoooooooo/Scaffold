package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Mapper
@Repository
public interface MenuMapper extends CommonMapper<Menu> {
    /**
     * 根据角色ID查询能访问的所有菜单
     *
     * @param roleId 角色ID
     * @return 菜单集合
     */
    Set<Menu> findSetByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID列表查询所有能访问的菜单
     *
     * @param roleIds 角色ID列表
     * @return 菜单列表
     */
    List<Menu> findListByRoles(@Param("roleIds") List<Long> roleIds);
}
