package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Car;
import com.jcweiho.scaffold.system.entity.criteria.CarQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.CarVO;
import com.jcweiho.scaffold.system.service.CarService;
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
 * 业主车辆信息表信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-12-08
 */
@Api(tags = "业主车辆管理")
@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController extends CommonController<CarService, Car> {

    @ApiOperation("查询业主车辆列表")
    @PreAuthorize("@el.check('OwnerCar:list')")
    @GetMapping
    public Map<String, Object> getCarList(@Validated CarQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出业主车辆信息")
    @ApiOperation("导出业主车辆信息")
    @PreAuthorize("@el.check('OwnerCar:list')")
    @GetMapping("/download")
    public void download(@Validated CarQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)), response);
    }

    @Logging(title = "新增业主车辆信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增业主车辆信息")
    @PreAuthorize("@el.check('OwnerCar:add')")
    @PostMapping
    public Result addCar(@RequestBody @Validated CarVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addCar(resources));
    }

    @Logging(title = "修改业主车辆信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改业主车辆信息")
    @PreAuthorize("@el.check('OwnerCar:update')")
    @PutMapping
    public Result updateCar(@RequestBody @Validated CarVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateCar(resources));
    }

    @Logging(title = "删除业主车辆信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除业主车辆信息")
    @PreAuthorize("@el.check('OwnerCar:delete')")
    @DeleteMapping
    public Result deleteCar(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteCar(filterCollNullAndDecrypt(ids)));
    }
}
