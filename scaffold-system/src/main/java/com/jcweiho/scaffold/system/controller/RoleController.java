package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.criteria.RoleQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.RoleVO;
import com.jcweiho.scaffold.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Api(tags = "系统角色接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController extends CommonController<RoleService, Role> {
    @GetMapping("/select")
    @ApiOperation("获取所有角色列表(带国际化)")
    @PreAuthorize("@el.check('Role:list')")
    public Map<String, Object> getAllRoles(@Validated RoleQueryCriteria criteria, HttpServletRequest request,
                                           @PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable, request);
    }

    @GetMapping
    @ApiOperation("获取所有角色列表(不带国际化)")
    @PreAuthorize("@el.check('Role:list')")
    public Map<String, Object> getAll(@Validated RoleQueryCriteria criteria,
                                      @PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @GetMapping("/levelScope")
    @ApiOperation("获取角色权限等级范围")
    @RateLimiter(limitType = LimitType.IP)
    public Map<String, Integer> getLevelScope() {
        return this.getBaseService().getLevelScope();
    }

    @Logging(title = "导出角色数据")
    @ApiOperation("导出角色数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Role:list')")
    public void download(@Validated RoleQueryCriteria criteria, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToDTOForLanguage(this.getBaseService().findAll(criteria), request), response);
    }

    @Logging(title = "修改角色信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改角色信息")
    @PutMapping
    @PreAuthorize("@el.check('Role:update')")
    public Result update(@Validated @RequestBody Role resource) {
        this.getBaseService().checkLevel(resource.getLevel());
        return resultMessage(Operate.UPDATE, this.getBaseService().updateRole(resource));
    }

    @Logging(title = "删除角色信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除角色信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Role:delete')")
    public Result delete(@RequestBody Set<String> idStrings) {
        Set<Long> ids = filterCollNullAndDecrypt(idStrings);
        for (Long id : ids) {
            this.getBaseService().checkLevel(this.getBaseService().getById(id).getLevel());
        }
        return resultMessage(Operate.DELETE, this.getBaseService().deleteRole(ids));
    }

    @ApiOperation("获取单个Role")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('Role:list')")
    public RoleVO getRoleById(@PathVariable String id) {
        return this.getBaseService().findById(IdSecureUtils.des().decrypt(id));
    }

    @Logging(title = "修改角色菜单", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改角色菜单")
    @PutMapping("/menus")
    @PreAuthorize("@el.check('Role:update')")
    public Result updateMenus(@RequestBody RoleVO resource) {
        RoleVO roleVO = this.getBaseService().findById(resource.getId());
        this.getBaseService().checkLevel(roleVO.getLevel());
        return resultMessage(Operate.OPERATE, this.getBaseService().updateMenu(resource));
    }

    @Logging(title = "新增角色", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('Role:add')")
    public Result createRole(@Validated @RequestBody Role resource) {
        if (resource.getId() != null) {
            throw new BadRequestException(I18nMessagesUtils.get("role.add.error"));
        }
        this.getBaseService().checkLevel(resource.getLevel());
        return resultMessage(Operate.ADD, this.getBaseService().createRole(resource));
    }
}
