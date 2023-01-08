package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdPayQueryCriteria;
import com.jcweiho.scaffold.system.service.HouseholdPayService;
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
 * 业主缴费信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2023-01-07
 */
@Api(tags = "业主缴费管理")
@RestController
@RequestMapping("/api/v1/householdPays")
@RequiredArgsConstructor
public class HouseholdPayController extends CommonController<HouseholdPayService, HouseholdPay> {

    @ApiOperation("查询业主缴费列表")
    @PreAuthorize("@el.check('HouseholdPay:list')")
    @GetMapping
    public Map<String, Object> getHouseholdPayList(@Validated HouseholdPayQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出业主缴费信息")
    @ApiOperation("导出业主缴费信息")
    @PreAuthorize("@el.check('HouseholdPay:list')")
    @GetMapping("/download")
    public void download(@Validated HouseholdPayQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)), response);
    }

    @Logging(title = "删除业主缴费信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除业主缴费信息")
    @PreAuthorize("@el.check('HouseholdPay:delete')")
    @DeleteMapping
    public Result deleteHouseholdPay(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteHouseholdPay(filterCollNullAndDecrypt(ids)));
    }
}
