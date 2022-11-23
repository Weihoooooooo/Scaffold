package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
}
