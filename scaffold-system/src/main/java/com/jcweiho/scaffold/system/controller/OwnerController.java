package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.MapUtils;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Owner;
import com.jcweiho.scaffold.system.entity.convert.OwnerVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.OwnerVO;
import com.jcweiho.scaffold.system.service.OwnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业主表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Api(tags = "业主服务接口")
@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController extends CommonController<OwnerService, Owner> {
    private final OwnerVOConvert ownerVOConvert;

    @ApiOperation("查询业主列表")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    @GetMapping
    public Map<String, Object> getOwnerList(@Validated OwnerQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().getOwnerList(criteria, pageable);
    }

    @Logging("导出业主信息")
    @ApiOperation("导出业主信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    public void download(HttpServletResponse response, @Validated OwnerQueryCriteria criteria) throws IOException {
        this.getBaseService().download(ownerVOConvert.toPojo(this.getBaseService().findAll(criteria)), response);
    }

    @Logging(title = "新增业主信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增业主信息")
    @PostMapping
    @PreAuthorize("@el.check('OwnerInfo:add')")
    public Result createOwner(@Validated @RequestBody OwnerVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().createOwner(resources));
    }

    @Logging(title = "修改业主信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改业主信息")
    @PutMapping
    @PreAuthorize("@el.check('OwnerInfo:update')")
    public Result updateOwner(@Validated @RequestBody OwnerVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateOwner(resources));
    }

    @ApiOperation("获取单个业主的信息")
    @PostMapping("/owner")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    @ApiImplicitParam(paramType = "query", name = "id", value = "业主主键", dataType = "Long", dataTypeClass = Long.class, required = true)
    public Result getOwnerForId(@ApiIgnore @RequestBody Map<String, Object> map) {
        filterMapNull(map, I18nMessagesUtils.get("param.error"));
        return Result.success(this.getBaseService().getById(filterNullAndDecrypt(MapUtils.getStr(map, "id"), I18nMessagesUtils.get("param.null"))));
    }

    @Logging(title = "重置业主密码", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("重置业主密码")
    @PutMapping("/reset")
    @PreAuthorize("@el.check('OwnerInfo:update')")
    @ApiImplicitParam(paramType = "query", name = "id", value = "业主主键", dataType = "Long", dataTypeClass = Long.class, required = true)
    public Result resetPass(@ApiIgnore @RequestBody Map<String, Object> map) {
        filterMapNull(map, I18nMessagesUtils.get("param.error"));
        return resultMessage(Operate.OPERATE,
                this.getBaseService().resetPassword(filterNullAndDecrypt(MapUtils.getStr(map, "id"), I18nMessagesUtils.get("param.null"))));
    }

    @Logging(title = "删除业主", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除业主")
    @DeleteMapping
    @PreAuthorize("@el.check('OwnerInfo:delete')")
    public Result deleteOwner(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteOwner(filterCollNullAndDecrypt(ids)));
    }
}
