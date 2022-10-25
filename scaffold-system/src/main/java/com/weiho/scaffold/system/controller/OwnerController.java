package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
import com.weiho.scaffold.system.entity.vo.OwnerPassVO;
import com.weiho.scaffold.system.entity.vo.OwnerVO;
import com.weiho.scaffold.system.service.OwnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

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
public class OwnerController {
    private final OwnerService ownerService;

    @ApiOperation("查询业主列表")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    @GetMapping
    public Map<String, Object> getOwnerList(OwnerQueryCriteria criteria, Pageable pageable) {
        return ownerService.getOwnerList(criteria, pageable);
    }

    @Logging(title = "导出业主信息")
    @ApiOperation("导出业主信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    public void download(HttpServletResponse response, OwnerQueryCriteria criteria) throws IOException {
        ownerService.download(ownerService.findAll(criteria), response);
    }

    @Logging(title = "新增业主信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增业主信息")
    @PostMapping
    @PreAuthorize("@el.check('OwnerInfo:add')")
    public Result createOwner(@Validated @RequestBody OwnerVO resources) {
        if (ownerService.createOwner(resources)) {
            return Result.success(I18nMessagesUtils.get("add.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("add.fail.tip"));
        }
    }

    @Logging(title = "修改业主信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改业主信息")
    @PutMapping
    @PreAuthorize("@el.check('OwnerInfo:update')")
    public Result updateOwner(@RequestBody OwnerVO resources) {
        System.err.println(resources);
        return Result.success(I18nMessagesUtils.get("update.success.tip"));
    }

    @ApiOperation("获取单个业主的信息")
    @PostMapping("/owner")
    @PreAuthorize("@el.check('OwnerInfo:list')")
    public Result getOwnerForId(@RequestBody Map<String, Object> map) {
        return Result.success(ownerService.getById((Serializable) map.get("id")));
    }

    @Logging(title = "重置业主密码", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("重置业主密码")
    @PutMapping("/reset")
    @PreAuthorize("@el.check('OwnerInfo:update')")
    public Result resetPass(@RequestBody OwnerPassVO ownerPassVO) {
        ownerService.resetPassword(ownerPassVO);
        return Result.success(I18nMessagesUtils.get("update.success.tip"));
    }
}
