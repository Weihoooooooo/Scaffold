package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.SysSetting;
import com.jcweiho.scaffold.system.entity.convert.SysSettingVOConvert;
import com.jcweiho.scaffold.system.entity.vo.SysSettingVO;
import com.jcweiho.scaffold.system.service.SysSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-09-19
 */
@Api(tags = "系统参数管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
public class SysSettingController extends CommonController<SysSettingService, SysSetting> {
    private final SysSettingVOConvert sysSettingVOConvert;

    @ApiOperation("获取系统Logo和标题")
    @GetMapping("/logo")
    @RateLimiter(limitType = LimitType.IP)
    public Map<String, Object> getSysSettings(HttpServletRequest request) {
        return this.getBaseService().getLogoAndTitle(request, this.getBaseService().getSysSettings());
    }

    @ApiOperation("获取系统参数")
    @GetMapping
    @PreAuthorize("@el.check('SystemSettings:use')")
    @RateLimiter(limitType = LimitType.IP)
    public SysSettingVO getSysSetting() {
        return sysSettingVOConvert.toPojo(this.getBaseService().getSysSettings());
    }

    @Logging(title = "修改系统参数", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改系统参数")
    @PutMapping
    @PreAuthorize("@el.check('SystemSettings:use')")
    public Result updateSys(@Validated @RequestBody SysSettingVO sysSettingVO) {
        SysSettingVO sysSettingVO1 = sysSettingVOConvert.toPojo(this.getBaseService().getById(sysSettingVO.getId()));
        if (!sysSettingVO1.toString().equals(sysSettingVO.toString())) {
            return resultMessage(Operate.UPDATE, this.getBaseService().updateSysSettings(sysSettingVO));
        } else {
            return Result.success(I18nMessagesUtils.get("update.success.tip"));
        }
    }
}
