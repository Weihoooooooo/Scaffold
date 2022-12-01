package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.ParkLot;
import com.jcweiho.scaffold.system.entity.criteria.ParkLotQueryCriteria;
import com.jcweiho.scaffold.system.service.ParkLotService;
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
 * 停车场信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
@Api(tags = "停车场管理")
@RestController
@RequestMapping("/api/v1/park-lots")
@RequiredArgsConstructor
public class ParkLotController extends CommonController<ParkLotService, ParkLot> {

    @ApiOperation("查询停车场列表")
    @PreAuthorize("@el.check('ParkLot:list')")
    @GetMapping
    public Map<String, Object> getParkLotList(@Validated ParkLotQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出停车场信息")
    @ApiOperation("导出停车场信息")
    @PreAuthorize("@el.check('ParkLot:list')")
    @GetMapping("/download")
    public void download(@Validated ParkLotQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().findAll(criteria), response);
    }

    @Logging(title = "新增停车场信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增停车场信息")
    @PreAuthorize("@el.check('ParkLot:add')")
    @PostMapping
    public Result addParkLot(@RequestBody @Validated ParkLot resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addParkLot(resources));
    }

    @Logging(title = "修改停车场信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改停车场信息")
    @PreAuthorize("@el.check('ParkLot:update')")
    @PutMapping
    public Result updateParkLot(@RequestBody @Validated ParkLot resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateParkLot(resources));
    }

    @Logging(title = "删除停车场信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除停车场信息")
    @PreAuthorize("@el.check('ParkLot:delete')")
    @DeleteMapping
    public Result deleteParkLot(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteParkLot(filterCollNullAndDecrypt(ids)));
    }
}
