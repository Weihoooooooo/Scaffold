package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import com.jcweiho.scaffold.system.service.HouseholdService;
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
 * 梯户信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-12-13
 */
@Api(tags = "梯户管理")
@RestController
@RequestMapping("/api/v1/households")
@RequiredArgsConstructor
public class HouseholdController extends CommonController<HouseholdService, Household> {

    @ApiOperation("查询梯户列表")
    @PreAuthorize("@el.check('Household:list')")
    @GetMapping
    public Map<String, Object> getHouseholdList(@Validated HouseholdQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出梯户信息")
    @ApiOperation("导出梯户信息")
    @PreAuthorize("@el.check('Household:list')")
    @GetMapping("/download")
    public void download(@Validated HouseholdQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)), response);
    }

    @Logging(title = "新增梯户信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增梯户信息")
    @PreAuthorize("@el.check('Household:add')")
    @PostMapping
    public Result addHousehold(@RequestBody @Validated HouseholdVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addHousehold(resources));
    }

    @Logging(title = "修改梯户信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改梯户信息")
    @PreAuthorize("@el.check('Household:update')")
    @PutMapping
    public Result updateHousehold(@RequestBody @Validated HouseholdVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateHousehold(resources));
    }

    @Logging(title = "删除梯户信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除梯户信息")
    @PreAuthorize("@el.check('Household:delete')")
    @DeleteMapping
    public Result deleteHousehold(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteHousehold(filterCollNullAndDecrypt(ids)));
    }
}
