package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Menu;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.criteria.MenuQueryCriteria;
import com.weiho.scaffold.system.entity.dto.MenuDTO;
import com.weiho.scaffold.system.entity.vo.MenuVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
public interface MenuService extends CommonService<Menu> {
    /**
     * 根据角色ID列表查询所有能访问的菜单
     *
     * @param roles 角色列表
     * @return Menu列表
     */
    List<MenuDTO> findListByRoles(List<Role> roles, String username);

    /**
     * 根据菜单列表和父子关系构建菜单树
     *
     * @param menuDTOS 菜单列表
     * @return 菜单树
     */
    List<MenuDTO> buildTree(List<MenuDTO> menuDTOS);

    /**
     * 构建前端所需的菜单json
     *
     * @param menuDTOS 菜单树
     * @return 前端所需的路由表json
     */
    List<MenuVO> buildMenuList(List<MenuDTO> menuDTOS, HttpServletRequest request);

    /**
     * 获取菜单树
     *
     * @param menus   菜单列表
     * @param request 请求对象
     * @return /
     */
    Object getMenuTree(List<Menu> menus, HttpServletRequest request);

    /**
     * 根据上级ID查找菜单列表
     *
     * @param pid 上级ID
     * @return /
     */
    List<Menu> findByParentId(long pid);

    /**
     * 根据角色ID查询能访问的所有菜单
     *
     * @param roleId 角色ID
     * @return 菜单集合
     */
    Set<Menu> findSetByRoleId(Long roleId);

    /**
     * 构建菜单树并结合分页组件构造前端列表
     *
     * @param menuDTOS 菜单信息列表
     * @return /
     */
    Map<String, Object> buildTreeForList(List<MenuDTO> menuDTOS);

    /**
     * 获取所有菜单（不分页）
     *
     * @param criteria 查询条件
     * @return /
     */
    List<Menu> getAll(MenuQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      数据
     * @param response 响应实体
     */
    void download(List<MenuDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 新增菜单
     *
     * @param resources 菜单信息
     */
    boolean createMenu(Menu resources);

    /**
     * 修改菜单
     *
     * @param resources 菜单信息
     */
    boolean updateMenu(Menu resources);

    /**
     * 删除菜单
     *
     * @param ids 主键列表
     */
    boolean deleteMenu(Set<Long> ids);

    /**
     * 根据菜单ID获取所有的下级菜单列表
     *
     * @param childrenList 子菜单列表
     * @param resultList   当前级的菜单列表
     * @return 总列表
     */
    Set<Menu> getLowerMenus(List<Menu> childrenList, Set<Menu> resultList);
}
