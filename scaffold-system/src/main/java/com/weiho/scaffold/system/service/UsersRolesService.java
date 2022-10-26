package com.weiho.scaffold.system.service;

import com.weiho.scaffold.system.entity.UsersRoles;

/**
 * @author Weiho
 * @since 2022/9/21
 */
public interface UsersRolesService {
    /**
     * 根据用户名删除所有的角色
     *
     * @param userId 用户ID
     */
    void deleteRolesByUserId(Long userId);

    /**
     * 为用户添加角色
     *
     * @param usersRoles 实体
     */
    void saveUserRoles(UsersRoles usersRoles);
}
