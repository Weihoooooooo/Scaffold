package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.ResultUtils;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.system.entity.Building;
import com.weiho.scaffold.system.entity.criteria.BuildingQueryCriteria;
import com.weiho.scaffold.system.service.BuildingService;
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
import java.util.Set;

/**
 * <p>
 * 楼宇信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-07
 */
@Api(tags = "楼宇管理")
@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final BuildingService buildingService;

    @ApiOperation("查询楼宇列表")
    @GetMapping
    @PreAuthorize("@el.check('Building:list')")
    public Map<String, Object> getBuildingList(BuildingQueryCriteria criteria, Pageable pageable) {
        return buildingService.getBuildingList(criteria, pageable);
    }

    @Logging(title = "导出楼宇信息")
    @ApiOperation("导出楼宇信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Building:list')")
    public void download(HttpServletResponse response, @Validated BuildingQueryCriteria criteria) throws IOException {
        buildingService.download(response, buildingService.findAll(criteria));
    }

    @Logging(title = "添加楼宇信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加楼宇信息")
    @PostMapping
    @PreAuthorize("@el.check('Building:add')")
    public Result addBuilding(@Validated @RequestBody Building resources) {
        return ResultUtils.addMessage(buildingService.addBuilding(resources));
    }

    @Logging(title = "修改楼宇信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改楼宇信息")
    @PutMapping
    @PreAuthorize("@el.check('Building:update')")
    public Result updateBuilding(@Validated @RequestBody Building resources) {
        return ResultUtils.updateMessage(buildingService.updateBuilding(resources));
    }

    @Logging(title = "删除楼宇信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除楼宇信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Building:delete')")
    public Result deleteBuilding(@RequestBody Set<String> ids) {
        return ResultUtils.deleteMessages(ids, buildingService.deleteBuilding(IdSecureUtils.des().decrypt(ids)));
    }

}
