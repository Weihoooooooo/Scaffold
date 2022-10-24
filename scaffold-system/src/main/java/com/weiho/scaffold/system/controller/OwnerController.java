package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
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

    @Logging(title = "新增业主信息")
    @ApiOperation("新增业主信息")
    @PostMapping
    @PreAuthorize("@el.check('OwnerInfo:add')")
    public Result createOwner(@Validated @RequestBody OwnerVO resources) {
        ownerService.createOwner(resources);
        return Result.success(I18nMessagesUtils.get("add.success.tip"));
    }
}
