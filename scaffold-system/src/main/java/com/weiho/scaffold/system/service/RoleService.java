package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.criteria.RoleQueryCriteria;
import com.weiho.scaffold.system.entity.dto.RoleDTO;
import com.weiho.scaffold.system.entity.vo.RoleVO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
public interface RoleService extends CommonService<Role> {
    /**
     * 根据用户ID获取用户的所有权限
     *
     * @param userId   用户ID
     * @param username 当前登录的用户名
     * @return 权限集合
     */
    Collection<SimpleGrantedAuthority> mapToGrantedAuthorities(Long userId, String username);

    /**
     * 根据用户ID查询所有的角色信息
     *
     * @param user 用户实体
     * @return 角色集合
     */
    List<Role> findListByUser(User user);

    /**
     * 查询所有的角色列表(带国际化)
     *
     * @param criteria 查询条件
     * @param pageable 分页条件
     * @return /
     */
    Map<String, Object> findAll(RoleQueryCriteria criteria, Pageable pageable, HttpServletRequest request);

    /**
     * 查询所有角色列表(不带国际化)
     *
     * @param criteria 查询条件
     * @param pageable 分页条件
     * @return /
     */
    Map<String, Object> findAll(RoleQueryCriteria criteria, Pageable pageable);

    /**
     * 根据用户ID查找该用户的最高角色等级
     *
     * @param userId 用户ID
     * @return 角色等级
     */
    Integer findHighLevel(Long userId);

    /**
     * 检查当前传入的用户ID的角色等级是否低于当前操作的用户
     * 若低于当前操作的用户则抛出异常
     *
     * @param resourceId 检查当前的用户ID
     */
    void checkLevel(Long resourceId);

    /**
     * 检查当前传入的角色等级是否低于当前操作的用户
     * 若低于当前操作的用户则抛出异常
     *
     * @param roles 角色集合
     */
    void checkLevel(Set<Role> roles);

    /**
     * 检查当前传入的级别是否高于当前登录用户的级别
     *
     * @param level 当前操作的角色级别
     */
    void checkLevel(Integer level);

    /**
     * 获取角色的角色权限等级范围
     *
     * @return Map
     */
    Map<String, Integer> getLevelScope();

    /**
     * 导出角色列表
     *
     * @param all      数据列
     * @param response 响应对象
     */
    void download(List<RoleDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 获取所有数据不分页
     *
     * @param criteria 查询条件
     * @return /
     */
    List<Role> findAll(RoleQueryCriteria criteria);

    /**
     * 获取所有数据不分页，并且根据国际化转换结果语言
     *
     * @param criteria 查询条件
     * @param request  请求对象
     * @return /
     */
    List<RoleDTO> findAllForLanguage(RoleQueryCriteria criteria, HttpServletRequest request);

    /**
     * 修改角色
     *
     * @param resource 修改后的实体
     */
    boolean updateRole(Role resource);

    /**
     * 删除角色
     *
     * @param ids 角色主键集合
     */
    boolean deleteRole(Set<Long> ids);

    /**
     * 根据角色ID查询角色信息
     *
     * @param id ID
     * @return /
     */
    RoleVO findById(Long id);

    /**
     * 修改角色菜单对应关系
     *
     * @param resource 新的关系
     */
    void updateMenu(RoleVO resource);

    /**
     * 新增角色
     *
     * @param resource 角色信息
     */
    boolean createRole(Role resource);
}
