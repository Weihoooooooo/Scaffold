package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Elevator;
import com.jcweiho.scaffold.system.entity.criteria.ElevatorQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.ElevatorMaintainVO;
import com.jcweiho.scaffold.system.entity.vo.ElevatorVO;
import com.jcweiho.scaffold.system.service.ElevatorService;
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
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 电梯信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
@Api(tags = "电梯管理")
@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
public class ElevatorController extends CommonController<ElevatorService, Elevator> {
    @ApiOperation("查询电梯列表")
    @GetMapping
    @PreAuthorize("@el.check('Elevator:list')")
    public Map<String, Object> getElevatorList(@Validated ElevatorQueryCriteria criteria, HttpServletRequest request, Pageable pageable) {
        return this.getBaseService().getElevatorList(criteria, request, pageable);
    }

    @Logging("导出电梯信息")
    @ApiOperation("导出电梯信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Elevator:list')")
    public void download(HttpServletResponse response, HttpServletRequest request, @Validated ElevatorQueryCriteria criteria) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria), request), response, request);
    }

    @Logging(title = "添加电梯信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加电梯信息")
    @PostMapping
    @PreAuthorize("@el.check('Elevator:add')")
    public Result addElevator(@Validated @RequestBody ElevatorVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addElevator(resources));
    }

    @Logging(title = "修改电梯信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改电梯信息")
    @PutMapping
    @PreAuthorize("@el.check('Elevator:update')")
    public Result updateElevator(@Validated @RequestBody ElevatorVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateElevator(resources));
    }

    @Logging(title = "删除电梯信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除电梯信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Elevator:delete')")
    public Result deleteElevator(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteElevator(filterCollNullAndDecrypt(ids)));
    }

    @Logging(title = "维护电梯", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("维护电梯")
    @PutMapping("/maintain")
    @PreAuthorize("@el.check('Elevator:list')")
    public Result maintainElevator(@Validated @RequestBody ElevatorMaintainVO resources) {
        return resultMessage(Operate.OPERATE, this.getBaseService().maintainElevator(resources));
    }
}
