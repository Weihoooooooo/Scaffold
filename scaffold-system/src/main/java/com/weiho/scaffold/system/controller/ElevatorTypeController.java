package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.entity.criteria.ElevatorTypeQueryCriteria;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@Api(tags = "电梯类型管理")
@RestController
@RequestMapping("/api/v1/elevator-types")
@RequiredArgsConstructor
public class ElevatorTypeController extends CommonController<ElevatorTypeService, ElevatorType> {
    @ApiOperation("获取电梯类型下拉框")
    @GetMapping("/select")
    @RateLimiter(limitType = LimitType.IP)
    public List<VueSelectVO> getElevatorTypeSelect(HttpServletRequest request) {
        return this.getBaseService().getElevatorTypeSelect(request);
    }

    @ApiOperation("获取电梯类型列表")
    @GetMapping
    @PreAuthorize("@el.check('Elevator:list')")
    public Map<String, Object> getElevatorList(@Validated ElevatorTypeQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging(title = "修改电梯类型", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改电梯类型")
    @PutMapping
    @PreAuthorize("@el.check('Elevator:update')")
    public Result updateElevatorType(@Validated @RequestBody ElevatorType resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateElevatorType(resources));
    }

    @Logging(title = "添加电梯类型", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加电梯类型")
    @PostMapping
    @PreAuthorize("@el.check('Elevator:add')")
    public Result addElevatorType(@Validated @RequestBody ElevatorType resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addElevatorType(resources));
    }

    @Logging(title = "删除电梯类型", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除电梯类型")
    @DeleteMapping
    @PreAuthorize("@el.check('Elevator:delete')")
    public Result deleteElevatorType(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteElevatorType(filterCollNullAndDecrypt(ids)));
    }

    @Logging(title = "导出电梯类型数据")
    @ApiOperation("导出电梯类型数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Elevator:list')")
    public void download(HttpServletResponse response, @Validated ElevatorTypeQueryCriteria criteria) throws IOException {
        this.getBaseService().download(this.getBaseService().findAll(criteria), response);
    }
}
