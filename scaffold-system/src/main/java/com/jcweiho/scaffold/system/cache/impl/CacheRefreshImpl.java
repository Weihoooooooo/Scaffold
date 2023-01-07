package com.jcweiho.scaffold.system.cache.impl;

import com.jcweiho.scaffold.system.cache.service.CacheRefresh;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.SysSetting;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.entity.Village;
import com.jcweiho.scaffold.system.entity.dto.MenuDTO;
import com.jcweiho.scaffold.system.service.MenuService;
import com.jcweiho.scaffold.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/9/26
 */
@Service
@RequiredArgsConstructor
public class CacheRefreshImpl implements CacheRefresh {
    private final RoleService roleService;
    private final MenuService menuService;

    @Override
    @CachePut(value = "Scaffold:Commons:User", key = "'loadUserByUsername:' + #p0.getUsername()", unless = "#result == null || #result.enabled == false")
    public User updateUserCache(User newUser) {
        return newUser;
    }

    @Override
    @CachePut(value = "Scaffold:Commons:Permission", key = "'loadPermissionByUsername:' + #p1", unless = "#result.size() <= 1")
    public Collection<SimpleGrantedAuthority> updateRolesCacheForGrantedAuthorities(Long userId, String username) {
        return roleService.mapToGrantedAuthorities(userId, username);
    }

    @Override
    @CachePut(value = "Scaffold:Commons:Roles", key = "'loadRolesByUsername:' + #p0.getUsername()")
    public List<Role> updateRolesCacheForRoleList(User user) {
        return roleService.findListByUser(user);
    }

    @Override
    @CachePut(value = "Scaffold:Commons:Menus", key = "'loadMenusByUsername:' + #p1")
    public List<MenuDTO> updateMenuCache(Long userId, String username) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        return menuService.findListByRoles(roleService.findListByUser(user), username);
    }

    @Override
    @CachePut(value = "Scaffold:System", key = "'settings'")
    public SysSetting updateSysSetting(SysSetting sysSetting) {
        return sysSetting;
    }

    @Override
    @CachePut(value = "Scaffold:Village", key = "'settings'")
    public Village updateVillage(Village village) {
        return village;
    }
}
