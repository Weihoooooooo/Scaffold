package com.jcweiho.scaffold.system.cache.service;

import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.SysSetting;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.entity.Village;
import com.jcweiho.scaffold.system.entity.dto.MenuDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/9/26
 */
@SuppressWarnings("all")
public interface CacheRefresh {
    /**
     * 更新用户缓存
     *
     * @param newUser 新的用户信息
     * @return /
     */
    User updateUserCache(User newUser);

    /**
     * 更新权限缓存(需要手动删除后调用)
     *
     * @param userId   用户ID
     * @param username 当前登录的用户名
     * @return 权限集合
     */
    Collection<SimpleGrantedAuthority> updateRolesCacheForGrantedAuthorities(Long userId, String username);

    /**
     * 更新角色缓存(需要手动删除后调用)
     *
     * @param user 用户实体
     * @return 角色集合
     */
    List<Role> updateRolesCacheForRoleList(User user);

    /**
     * 更新菜单缓存
     *
     * @param userId   当前登录用户ID
     * @param username 当前登录用户名
     * @return /
     */
    List<MenuDTO> updateMenuCache(Long userId, String username);

    /**
     * 更新系统设置
     *
     * @param sysSetting 新的系统设置
     * @return
     */
    SysSetting updateSysSetting(SysSetting sysSetting);

    /**
     * 更新小区参数
     *
     * @param village 新的小区参数
     * @return
     */
    Village updateVillage(Village village);
}
