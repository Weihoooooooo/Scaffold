package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Park;
import com.jcweiho.scaffold.system.entity.criteria.ParkQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.ParkVO;
import com.jcweiho.scaffold.system.service.ParkService;
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
 * 车位信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-12-05
 */
@Api(tags = "车位管理")
@RestController
@RequestMapping("/api/v1/parks")
@RequiredArgsConstructor
public class ParkController extends CommonController<ParkService, Park> {
    @ApiOperation("查询车位列表")
    @PreAuthorize("@el.check('Park:list')")
    @GetMapping
    public Map<String, Object> getParkList(@Validated ParkQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出车位信息")
    @ApiOperation("导出车位信息")
    @PreAuthorize("@el.check('Park:list')")
    @GetMapping("/download")
    public void download(@Validated ParkQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(response, this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)));
    }

    @Logging(title = "新增车位信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增车位信息")
    @PreAuthorize("@el.check('Park:add')")
    @PostMapping
    public Result addPark(@RequestBody @Validated ParkVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addPark(resources));
    }

    @Logging(title = "修改车位信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改车位信息")
    @PreAuthorize("@el.check('Park:update')")
    @PutMapping
    public Result updatePark(@RequestBody @Validated ParkVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updatePark(resources));
    }

    @Logging(title = "删除车位信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除车位信息")
    @PreAuthorize("@el.check('Park:delete')")
    @DeleteMapping
    public Result deletePark(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deletePark(filterCollNullAndDecrypt(ids)));
    }

}
