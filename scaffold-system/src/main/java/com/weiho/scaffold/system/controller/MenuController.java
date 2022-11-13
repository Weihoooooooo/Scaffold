package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.exception.SecurityException;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.ResultUtils;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.common.util.security.SecurityUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.redis.util.RedisUtils;
import com.weiho.scaffold.system.cache.service.CacheRefresh;
import com.weiho.scaffold.system.entity.Menu;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.convert.MenuDTOConvert;
import com.weiho.scaffold.system.entity.criteria.MenuQueryCriteria;
import com.weiho.scaffold.system.entity.dto.MenuDTO;
import com.weiho.scaffold.system.entity.vo.MenuTreeVO;
import com.weiho.scaffold.system.entity.vo.MenuVO;
import com.weiho.scaffold.system.service.MenuService;
import com.weiho.scaffold.system.service.RoleService;
import com.weiho.scaffold.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuDTOConvert menuDTOConvert;
    private final MenuService menuService;
    private final RoleService roleService;
    private final UserService userService;
    private final CacheRefresh cacheRefresh;
    private final RedisUtils redisUtils;

    @ApiOperation("获取前端所需菜单")
    @GetMapping("/build")
    public List<MenuVO> buildMenuList(HttpServletRequest request) {
        try {
            String username = SecurityUtils.getUsername();
            //获取当前登录的用户的ID
            User user = userService.findByUsername(username);
            //获取能访问的菜单列表
            List<MenuDTO> menuDTOList = menuService.findListByRoles(roleService.findListByUser(user), username);
            //构建菜单树
            List<MenuDTO> menuDTOTree = menuService.buildTree(menuDTOList);
            //递归转成前端所需的结构,返回
            return menuService.buildMenuList(menuDTOTree, request);
        } catch (Exception e) {
            throw new SecurityException(ResultCodeEnum.FAILED, I18nMessagesUtils.get("menu.error.tip"));
        }
    }

    @ApiOperation("获取菜单树")
    @GetMapping("/tree")
    @PreAuthorize("@el.check('Role:list','Menu:list')")
    public List<MenuTreeVO> getMenuTree(HttpServletRequest request) {
        return menuService.getMenuTree(menuService.findByParentId(0L).stream().filter(Menu::isEnabled).collect(Collectors.toList()), request);
    }

    @ApiOperation("查询菜单列表")
    @GetMapping
    @PreAuthorize("@el.check('Menu:list')")
    public Map<String, Object> getMenuList(@Validated MenuQueryCriteria criteria) {
        List<MenuDTO> menuDTOS = menuDTOConvert.toPojo(menuService.getAll(criteria));
        if (criteria.getEnabled() == null || criteria.getEnabled()) {
            return menuService.buildTreeForList(menuDTOS);
        } else {
            return PageUtils.toPageContainer(menuDTOS, menuDTOS.size());
        }
    }

    @Logging(title = "导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Menu:list')")
    public void download(HttpServletResponse response, @Validated MenuQueryCriteria criteria) throws IOException {
        menuService.download(menuDTOConvert.toPojo(menuService.getAll(criteria)), response);
    }

    @Logging(title = "新增菜单", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('Menu:add')")
    public Result addMenu(@Validated @RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("新增的菜单不能拥有ID");
        }
        return ResultUtils.addMessage(menuService.createMenu(resources));
    }

    @Logging(title = "修改菜单", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('Menu:update')")
    public Result updateMenu(@Validated @RequestBody Menu resources, HttpServletRequest request) {
        // 刷新缓存
        this.refreshMenuCache(menuService.updateMenu(resources), request);
        return Result.success(I18nMessagesUtils.get("update.success.tip"));
    }

    @Logging(title = "删除菜单", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('Menu:delete')")
    public Result deleteMenu(@RequestBody Set<Long> ids, HttpServletRequest request) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            // 查找子菜单
            List<Menu> menuChildren = menuService.findByParentId(id);
            menuSet.add(menuService.getById(id));
            menuSet = menuService.getLowerMenus(menuChildren, menuSet);
        }
        Set<Long> deleteIds = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        // 执行删除并且更新缓存
        this.refreshMenuCache(menuService.deleteMenu(deleteIds), request);
        return Result.success(I18nMessagesUtils.get("delete.success.tip"));
    }

    /**
     * 刷新菜单缓存(在删除和更新菜单后都要刷新缓存)
     *
     * @param flag    是否刷新
     * @param request 请求参数
     */
    public void refreshMenuCache(boolean flag, HttpServletRequest request) {
        String username = SecurityUtils.getUsername();
        Long userId = SecurityUtils.getUserId();

        String keyPermission = redisUtils.getRedisCommonsKey("Permission", username);
        String keyMenu = redisUtils.getRedisCommonsKey("Menus", username);
        String keyMenuTree = "Scaffold:System::MenuTree";

        if (flag) {
            if (redisUtils.hasKey(keyPermission) && redisUtils.hasKey(keyMenu)) {
                // 手动删除缓存
                redisUtils.del(keyMenu, keyPermission);
                // 调用更新缓存
                cacheRefresh.updateRolesCacheForGrantedAuthorities(userId, username);
                cacheRefresh.updateMenuCache(userId, username);
            }
        }
    }
}
