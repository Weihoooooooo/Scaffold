package com.weiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.CollUtils;
import com.weiho.scaffold.common.util.FileUtils;
import com.weiho.scaffold.common.util.PageUtils;
import com.weiho.scaffold.common.util.StringUtils;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Menu;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.convert.MenuDTOConvert;
import com.weiho.scaffold.system.entity.criteria.MenuQueryCriteria;
import com.weiho.scaffold.system.entity.dto.MenuDTO;
import com.weiho.scaffold.system.entity.vo.MenuMetaVO;
import com.weiho.scaffold.system.entity.vo.MenuTreeVO;
import com.weiho.scaffold.system.entity.vo.MenuVO;
import com.weiho.scaffold.system.mapper.MenuMapper;
import com.weiho.scaffold.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends CommonServiceImpl<MenuMapper, Menu> implements MenuService {
    private final MenuDTOConvert menuDTOConvert;

    @Override
    @Cacheable(value = "Scaffold:Commons:Menus", key = "'loadMenusByUsername:' + #p1")
    public List<MenuDTO> findListByRoles(List<Role> roles, String username) {
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<Menu> menus = this.getBaseMapper().findListByRoles(roleIds).stream().filter(Menu::isEnabled).collect(Collectors.toList());
        return menuDTOConvert.toPojo(menus);
    }

    @Override
    public List<MenuDTO> buildTree(List<MenuDTO> menuDTOS) {
        //构建空的返回结果
        List<MenuDTO> trees = new ArrayList<>();
        //构造作为子菜单的主键集合,用于防止菜单树为空
        Set<Long> ids = new HashSet<>();
        for (MenuDTO menuDTO : menuDTOS) {
            //判断是否是顶级菜单,是，则直接加入结果集
            if (menuDTO.getParentId() == 0) {
                trees.add(menuDTO);
            }
            //处理不是顶级菜单，再次遍历
            for (MenuDTO it : menuDTOS) {
                //若某一个菜单是某一个顶级菜单的子菜单则放入children中
                if (it.getParentId().equals(menuDTO.getId())) {
                    //children非空判断
                    if (ObjectUtil.isNull(menuDTO.getChildren())) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        //过滤作为子菜单的menu,将下一级菜单进行展示,保证tree不是空
        if (CollUtils.isEmpty(trees)) {
            trees = menuDTOS.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuVO> buildMenuList(List<MenuDTO> menuDTOS, HttpServletRequest request) {
        //采用链表结构，提高add和remove有效率
        List<MenuVO> menuVOs = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
            if (ObjectUtil.isNotNull(menuDTO)) {
                //获取子菜单
                List<MenuDTO> menuDTOList = menuDTO.getChildren();
                //构造VO对象
                MenuVO menuVO = new MenuVO();
                //设置菜单名称 (如果组件名不为空则拿组件名，否则拿name)
                menuVO.setName(StringUtils.isNotBlank(menuDTO.getComponentName()) ? menuDTO.getComponentName() : I18nMessagesUtils.getNameForI18n(request, menuDTO));
                //一级目录的path需要加上'/'
                menuVO.setPath(menuDTO.getParentId() == 0 ? "/" + menuDTO.getPath() : menuDTO.getPath());
                menuVO.setHidden(menuDTO.getHidden());

                //如果是顶级菜单，加上Layout标识，给前端处理
                if (menuDTO.getParentId() == 0) {
                    menuVO.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuVO.getComponent());
                } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                    menuVO.setComponent(menuDTO.getComponent());
                }

                //设置Meta对象
                menuVO.setMeta(new MenuMetaVO(I18nMessagesUtils.getNameForI18n(request, menuDTO), menuDTO.getIconCls()));

                //处理Children
                if (CollUtils.isNotBlank(menuDTOList)) {
                    menuVO.setRedirect("noRedirect");
                    //递归处理
                    menuVO.setChildren(buildMenuList(menuDTOList, request));
                } else if (menuDTO.getParentId() == 0) {
                    //当一级菜单没有子菜单
                    //构造一个子菜单
                    MenuVO menuVO1 = new MenuVO();
                    menuVO1.setMeta(menuVO.getMeta());
                    menuVO1.setPath("index");
                    menuVO1.setName(I18nMessagesUtils.getNameForI18n(request, menuDTO));
                    menuVO1.setComponent(menuVO.getComponent());
                    menuVO1.setHidden(false);

                    //父菜单
                    menuVO.setName(null);
                    menuVO.setMeta(null);
                    menuVO.setComponent("Layout");
                    List<MenuVO> list = new ArrayList<MenuVO>() {{
                        add(menuVO1);
                    }};
                    menuVO.setChildren(list);
                }
                menuVOs.add(menuVO);
            }
        });
        return menuVOs;
    }

    @Override
    public List<MenuTreeVO> getMenuTree(List<Menu> menus, HttpServletRequest request) {
        List<MenuTreeVO> list = new LinkedList<>();
        menus.forEach(menu -> {
            if (ObjectUtil.isNotNull(menu)) {
                // 获取子菜单
                List<Menu> menuList = this.findByParentId(menu.getId());
                MenuTreeVO menuTreeVO = new MenuTreeVO();
                menuTreeVO.setId(menu.getId());
                menuTreeVO.setLabel(I18nMessagesUtils.getNameForI18n(request, menu));
                if (CollUtils.isNotBlank(menuList)) {
                    menuTreeVO.setChildren(getMenuTree(menuList, request));
                }
                list.add(menuTreeVO);
            }
        });
        return list;
    }

    @Override
    public List<Menu> findByParentId(long pid) {
        return this.lambdaQuery().eq(Menu::getParentId, pid).eq(Menu::getIsDel, 0).list();
    }

    @Override
    public Set<Menu> findSetByRoleId(Long roleId) {
        return this.getBaseMapper().findSetByRoleId(roleId);
    }

    @Override
    public Map<String, Object> buildTreeForList(List<MenuDTO> menuDTOS) {
        return PageUtils.toPageContainer(this.buildTree(menuDTOS), menuDTOS.size());
    }

    @Override
    public List<Menu> getAll(MenuQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Menu.class, criteria)));
    }

    @Override
    public void download(List<MenuDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.menu.component"), menuDTO.getComponent());
            map.put(I18nMessagesUtils.get("download.menu.component.name"), menuDTO.getComponentName());
            map.put(I18nMessagesUtils.get("download.menu.path"), menuDTO.getPath());
            map.put(I18nMessagesUtils.get("download.menu.name"), menuDTO.getName());
            map.put(I18nMessagesUtils.get("download.menu.icon"), menuDTO.getIconCls());
            map.put(I18nMessagesUtils.get("download.menu.url"), menuDTO.getUrl());
            map.put(I18nMessagesUtils.get("download.menu.permission"), menuDTO.getPermission());
            map.put(I18nMessagesUtils.get("download.menu.keep.live"), menuDTO.getKeepAlive() ? "是" : "否");
            map.put(I18nMessagesUtils.get("download.menu.hidden"), menuDTO.getHidden() ? "是" : "否");
            map.put(I18nMessagesUtils.get("download.menu.enabled"), menuDTO.getEnabled() ? "启用" : "禁用");
            map.put(I18nMessagesUtils.get("download.menu.type"), menuDTO.getType().getDisplay());
            map.put(I18nMessagesUtils.get("download.menu.sort"), menuDTO.getSort());
            map.put(I18nMessagesUtils.get("download.createTime"), menuDTO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public boolean createMenu(Menu resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getName, resources.getName())))) {
            throw new BadRequestException(I18nMessagesUtils.get("menu.name.tip"));
        }
        verifyResources(resources);
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(Menu resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        // 验证上级不能为自己
        if (resources.getId().equals(resources.getParentId())) {
            throw new BadRequestException(I18nMessagesUtils.get("menu.parentId.error"));
        }

        Menu menuForId = this.getById(resources.getId());
        Menu menuForName = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getName, resources.getName()));

        // 验证菜单名是否已存在
        if (ObjectUtil.isNotNull(menuForName) && !menuForName.getId().equals(menuForId.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("menu.name.tip"));
        }

        // 根据菜单类型进行验证
        if (resources.getType().getKey() == 0) {
            verifyPathAndUrlForUpdate(resources, menuForId);

            menuForId.setId(resources.getId());
            menuForId.setParentId(resources.getParentId());
            menuForId.setComponent(null);
            menuForId.setComponentName(null);
            setMenu(resources, menuForId);
            menuForId.setPermission(null);
            menuForId.setKeepAlive(resources.isKeepAlive());
            menuForId.setSort(resources.getSort());
            menuForId.setHidden(resources.isHidden());
            menuForId.setEnabled(resources.isEnabled());
            menuForId.setType(resources.getType());

        } else if (resources.getType().getKey() == 1) {
            // ComponentName只能唯一并且一定存在
            if (StringUtils.isNotBlank(resources.getComponentName())) {
                Menu menuForComponentName = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getComponentName, resources.getComponentName()));
                if (ObjectUtil.isNotNull(menuForComponentName) && !menuForComponentName.getId().equals(menuForId.getId())) {
                    throw new BadRequestException(I18nMessagesUtils.get("menu.componentName.tip"));
                }
            } else {
                throw new BadRequestException(I18nMessagesUtils.get("menu.componentName.null.tip"));
            }

            // Component只能唯一并且一定存在
            if (StringUtils.isNotBlank(resources.getComponent())) {
                Menu menuForComponent = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getComponent, resources.getComponent()));
                if (ObjectUtil.isNotNull(menuForComponent) && !menuForComponent.getId().equals(menuForId.getId())) {
                    throw new BadRequestException(I18nMessagesUtils.get("menu.component.tip"));
                }
            } else {
                throw new BadRequestException(I18nMessagesUtils.get("menu.component.null.tip"));
            }

            // Permission只能唯一并且一定存在
            verifyPermissionForUpdate(resources, menuForId);

            verifyPathAndUrlForUpdate(resources, menuForId);

            menuForId.setId(resources.getId());
            menuForId.setParentId(resources.getParentId());
            menuForId.setComponent(resources.getComponent());
            menuForId.setComponentName(resources.getComponentName());
            setMenu(resources, menuForId);
            menuForId.setPermission(resources.getPermission());
            menuForId.setKeepAlive(resources.isKeepAlive());
            menuForId.setSort(resources.getSort());
            menuForId.setHidden(resources.isHidden());
            menuForId.setEnabled(resources.isEnabled());
            menuForId.setType(resources.getType());
        } else {
            // Permission只能唯一并且一定存在
            verifyPermissionForUpdate(resources, menuForId);

            menuForId.setId(resources.getId());
            menuForId.setParentId(resources.getParentId());
            menuForId.setComponent(null);
            menuForId.setComponentName(null);
            menuForId.setPath(null);
            menuForId.setName(resources.getName());
            menuForId.setNameZhCn(resources.getNameZhCn());
            menuForId.setNameZhHk(resources.getNameZhHk());
            menuForId.setNameZhTw(resources.getNameZhTw());
            menuForId.setNameEnUs(resources.getNameEnUs());
            menuForId.setIconCls(null);
            menuForId.setUrl(null);
            menuForId.setPermission(resources.getPermission());
            menuForId.setKeepAlive(resources.isKeepAlive());
            menuForId.setSort(resources.getSort());
            menuForId.setHidden(resources.isHidden());
            menuForId.setEnabled(resources.isEnabled());
            menuForId.setType(resources.getType());

        }

        return this.saveOrUpdate(menuForId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Set<Menu> getLowerMenus(List<Menu> childrenList, Set<Menu> resultList) {
        // 通过遍历和递归查找下一级是否还有菜单
        for (Menu menu : childrenList) {
            resultList.add(menu);
            // 查找子菜单列表
            List<Menu> menus = this.findByParentId(menu.getId());
            if (CollUtils.isNotEmpty(menus) && ObjectUtil.isNotNull(menus)) {
                // 再次递归
                getLowerMenus(menus, resultList);
            }
        }
        return resultList;
    }

    private void verifyPermissionForUpdate(Menu resources, Menu menuForId) {
        if (StringUtils.isNotBlank(resources.getPermission())) {
            Menu menuForPermission = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getPermission, resources.getPermission()));
            if (ObjectUtil.isNotNull(menuForPermission) && !menuForPermission.getId().equals(menuForId.getId())) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.permission.tip"));
            }
        } else {
            throw new BadRequestException(I18nMessagesUtils.get("menu.permission.null"));
        }
    }

    private void setMenu(Menu resources, Menu menuForId) {
        menuForId.setPath(resources.getPath());
        menuForId.setName(resources.getName());
        menuForId.setNameZhCn(resources.getNameZhCn());
        menuForId.setNameZhHk(resources.getNameZhHk());
        menuForId.setNameZhTw(resources.getNameZhTw());
        menuForId.setNameEnUs(resources.getNameEnUs());
        menuForId.setIconCls(resources.getIconCls());
        menuForId.setUrl(resources.getUrl());
    }

    private void verifyPathAndUrlForUpdate(Menu resources, Menu menuForId) {
        if (StringUtils.isNotBlank(resources.getPath())) {
            Menu menuForPath = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getPath, resources.getPath()));
            if (ObjectUtil.isNotNull(menuForPath) && !menuForPath.getId().equals(menuForId.getId())) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.path.tip"));
            }
        } else {
            throw new BadRequestException(I18nMessagesUtils.get("menu.path.null.tip"));
        }

        if (StringUtils.isBlank(resources.getUrl())) {
            throw new BadRequestException(I18nMessagesUtils.get("menu.url.null.tip"));
        }
    }

    private void verifyResources(Menu resources) {
        if (resources.getType().getKey() == 0) {
            verifyPathAndUrlForAdd(resources);
            resources.setComponent(null);
            resources.setComponentName(null);
            resources.setPermission(null);
        } else if (resources.getType().getKey() == 1) {
            if (StringUtils.isNotBlank(resources.getComponentName())) {
                if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getComponentName, resources.getComponentName())))) {
                    throw new BadRequestException(I18nMessagesUtils.get("menu.componentName.tip"));
                }
            } else {
                throw new BadRequestException(I18nMessagesUtils.get("menu.componentName.null.tip"));
            }

            if (StringUtils.isBlank(resources.getComponent())) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.component.null.tip"));
            }

            if (StringUtils.isBlank(resources.getPermission())) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.permission.null"));
            }

            verifyPathAndUrlForAdd(resources);
        } else {
            if (StringUtils.isBlank(resources.getPermission())) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.permission.null"));
            }

            resources.setComponent(null);
            resources.setComponentName(null);
            resources.setPath(null);
            resources.setIconCls(null);
            resources.setUrl(null);
        }
    }

    private void verifyPathAndUrlForAdd(Menu resources) {
        if (StringUtils.isNotBlank(resources.getPath())) {
            if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getPath, resources.getPath())))) {
                throw new BadRequestException(I18nMessagesUtils.get("menu.path.tip"));
            }
        } else {
            throw new BadRequestException(I18nMessagesUtils.get("menu.path.null.tip"));
        }

        if (StringUtils.isBlank(resources.getUrl())) {
            throw new BadRequestException(I18nMessagesUtils.get("menu.url.null.tip"));
        }
    }
}
