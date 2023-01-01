package com.jcweiho.scaffold.tools.mail.controller;

import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.VerifyUtils;
import com.jcweiho.scaffold.common.util.enums.EnumSelectVO;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.rabbitmq.core.MqPublisher;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.jcweiho.scaffold.tools.mail.entity.vo.VerificationCodeVO;
import com.jcweiho.scaffold.tools.mail.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Weiho
 * @since 2022/9/6
 */
@Api(tags = "邮箱验证码管理")
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class VerificationCodeController {
    private final VerificationCodeService verificationCodeService;
    private final MqPublisher mqPublisher;

    @Logging(title = "请求发送邮箱验证码", saveRequestData = false)
    @PostMapping(value = "/code")
    @ApiOperation("请求发送邮箱验证码")
    @RateLimiter(count = 1, limitType = LimitType.IP)// 一分钟之内只能请求1次
    public Result getEmailCode(@Validated @RequestBody VerificationCodeVO codeVO) {
        System.err.println(codeVO.toString());
        if (!VerifyUtils.isEmail(codeVO.getAccount() + codeVO.getSuffix().getDisplay())) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.error.no.email"));
        }
        Map<String, Object> codeResult = verificationCodeService.generatorEmailInfo(codeVO);
        EmailVO emailVO = (EmailVO) codeResult.get("emailVO");
        // 放入MQ消息队列
        mqPublisher.sendMqMessage(emailVO);
        return Result.success(codeResult.get("uuid"));
    }

    @GetMapping("/options")
    @ApiOperation("获取前端的下拉列表")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getSelectList() {
        return verificationCodeService.getSelectList();
    }
}
