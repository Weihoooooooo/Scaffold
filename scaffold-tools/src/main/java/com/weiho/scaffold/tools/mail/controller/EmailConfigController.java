package com.weiho.scaffold.tools.mail.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.tools.mail.entity.EmailConfig;
import com.weiho.scaffold.tools.mail.entity.convert.EmailConfigVOConvert;
import com.weiho.scaffold.tools.mail.entity.vo.EmailConfigVO;
import com.weiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.weiho.scaffold.tools.mail.service.EmailConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-09-05
 */
@Api(tags = "邮件配置")
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailConfigController extends CommonController<EmailConfigService, EmailConfig> {
    private final EmailConfigVOConvert emailConfigVOConvert;

    @ApiOperation("获取邮箱配置")
    @GetMapping
    @RateLimiter(limitType = LimitType.IP)
    public Result getConfig() {
        return Result.success(this.getBaseService().getConfig());
    }

    @Logging(title = "修改邮件配置", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改邮件配置")
    public Result updateEmailConfig(@Validated @RequestBody EmailConfigVO config) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateEmailConfig(config));
    }

    @Logging(title = "发送邮件")
    @PostMapping
    @ApiOperation("发送邮件")
    public Result sendEmail(@Validated @RequestBody EmailVO emailVO) {
        this.getBaseService().send(emailVO, emailConfigVOConvert.toPojo(this.getBaseService().getConfig()));
        return Result.success(I18nMessagesUtils.get("mail.send.success.tips"));
    }
}
