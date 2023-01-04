package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.Village;
import com.jcweiho.scaffold.system.service.VillageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 小区基本信息参数 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2023-01-04
 */
@Api(tags = "小区参数管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/villages")
public class VillageController extends CommonController<VillageService, Village> {
    private final VillageService villageService;

    @ApiOperation("获取小区参数")
    @GetMapping
    @PreAuthorize("el.check('Village:use')")
    @RateLimiter(limitType = LimitType.IP)
    public Village getVillage() {
        return villageService.getVillage();
    }

    @Logging(title = "修改小区参数", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改小区参数")
    @PutMapping
    @PreAuthorize("@el.check('Village:use,Village:update')")
    public Result updateVillage(@Validated @RequestBody Village village) {
        Village village1 = this.getBaseService().getById(village.getId());
        if (!village1.toString().equals(village.toString())) {
            return resultMessage(Operate.UPDATE, this.getBaseService().updateVillage(village));
        } else {
            return Result.success(I18nMessagesUtils.get("update.success.tip"));
        }
    }
}
