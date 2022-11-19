package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
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
import java.util.List;
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
public class BuildingController extends CommonController<BuildingService, Building> {
    @ApiOperation("查询楼宇列表")
    @GetMapping
    @PreAuthorize("@el.check('Building:list')")
    public Map<String, Object> getBuildingList(@Validated BuildingQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().getBuildingList(criteria, pageable);
    }

    @Logging(title = "导出楼宇信息")
    @ApiOperation("导出楼宇信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Building:list')")
    public void download(HttpServletResponse response, @Validated BuildingQueryCriteria criteria) throws IOException {
        this.getBaseService().download(response, this.getBaseService().findAll(criteria));
    }

    @Logging(title = "添加楼宇信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加楼宇信息")
    @PostMapping
    @PreAuthorize("@el.check('Building:add')")
    public Result addBuilding(@Validated @RequestBody Building resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addBuilding(resources));
    }

    @Logging(title = "修改楼宇信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改楼宇信息")
    @PutMapping
    @PreAuthorize("@el.check('Building:update')")
    public Result updateBuilding(@Validated @RequestBody Building resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateBuilding(resources));
    }

    @Logging(title = "删除楼宇信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除楼宇信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Building:delete')")
    public Result deleteBuilding(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteBuilding(filterCollNullAndDecrypt(ids)));
    }

    @ApiOperation("获取楼宇下拉选择框")
    @GetMapping("/buildingNums")
    @PreAuthorize("@el.check('Building:list')")
    @RateLimiter(limitType = LimitType.IP)
    public List<VueSelectVO> getDistinctBuildingSelect() {
        return this.getBaseService().getDistinctBuildingSelect();
    }

}
