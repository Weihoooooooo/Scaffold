package com.jcweiho.scaffold.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jcweiho.scaffold.common.util.DateUtils;
import com.jcweiho.scaffold.common.util.MapUtils;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import com.jcweiho.scaffold.system.entity.Village;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import com.jcweiho.scaffold.system.service.HouseholdPayService;
import com.jcweiho.scaffold.system.service.HouseholdService;
import com.jcweiho.scaffold.system.service.VillageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    private final VillageService villageService;
    private final HouseholdPayService householdPayService;

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
        Map<String, Object> map = this.getBaseService().updateHousehold(resources);
        Boolean updateFlag = MapUtils.get(map, "flag", Boolean.class);
        Household household = MapUtils.get(map, "household", Household.class);

        // 先判断该年该月是否存在记录
        Date now = DateUtils.getNowDate();
        Long year = Convert.toLong(DateUtils.year(now));
        Integer month = DateUtils.month(now) + 1;
        HouseholdPay flag = householdPayService.getOne(new LambdaQueryWrapper<HouseholdPay>()
                .eq(HouseholdPay::getHouseholdId, household.getId())
                .eq(HouseholdPay::getYear, year).eq(HouseholdPay::getMonth, month));

        // 该年该月没有记录则插入
        if (ObjectUtil.isNull(flag)) {
            if (resources.getMeterWater() > household.getMeterWater() || resources.getMeterElectric() > household.getMeterWater()) {
                // 修改上一次的水表读数
                Double lastMeterWater = household.getMeterWater();
                // 放入本次修改的水表读数
                Double meterWater = resources.getMeterWater();
                // 修改上一次的电表读数
                Double lastMeterElectric = household.getMeterElectric();
                // 放入本次修改的电表读数
                Double meterElectric = resources.getMeterElectric();

                // 更新
                this.getBaseService().lambdaUpdate().set(Household::getLastMeterWater, lastMeterWater)
                        .set(Household::getMeterWater, meterWater)
                        .set(Household::getLastMeterElectric, lastMeterElectric)
                        .set(Household::getMeterElectric, meterElectric)
                        .eq(Household::getId, household.getId()).update();

                // 计算价格
                Village village = villageService.getVillage();

                // 该年该月没有记录则插入
                HouseholdPay householdPay = new HouseholdPay();
                householdPay.setHouseholdId(resources.getId());
                householdPay.setPayWater(Convert.toDouble(NumberUtil.round(NumberUtil.mul(Convert.toDouble(NumberUtil.sub(meterWater, lastMeterWater)), village.getWater()), 2)));
                householdPay.setPayElectric(Convert.toDouble(NumberUtil.round(NumberUtil.mul(Convert.toDouble(NumberUtil.sub(meterElectric, lastMeterElectric)), village.getElectric()), 2)));
                householdPay.setPayProperty(Convert.toDouble(NumberUtil.round(NumberUtil.mul(household.getArea(), village.getProperty()), 2)));
                householdPay.setYear(year);
                householdPay.setMonth(month);
                return resultMessage(Operate.UPDATE, updateFlag && householdPayService.addHouseholdPay(householdPay));
            }
        }
        return resultMessage(Operate.UPDATE, updateFlag);
    }

    @Logging(title = "删除梯户信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除梯户信息")
    @PreAuthorize("@el.check('Household:delete')")
    @DeleteMapping
    public Result deleteHousehold(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteHousehold(filterCollNullAndDecrypt(ids)));
    }

    @ApiOperation("根据楼宇ID获取梯户列表")
    @GetMapping("/buildingNum/{id}")
    @PreAuthorize("@el.check('Building:list','Household:list')")
    @RateLimiter(limitType = LimitType.IP)
    public List<VueSelectVO> getDistinctHouseholdSelect(@PathVariable String id) {
        return this.getBaseService().getDistinctHouseholdSelect(filterNullAndDecrypt(id));
    }
}
