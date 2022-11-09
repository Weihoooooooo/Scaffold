package com.weiho.scaffold.tools.mail.controller;

import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.enums.EnumSelectVO;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.verify.VerifyUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.weiho.scaffold.tools.mail.entity.vo.VerificationCodeVO;
import com.weiho.scaffold.tools.mail.service.VerificationCodeService;
import com.weiho.scaffold.tools.rabbitmq.core.MqPublisher;
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
        mqPublisher.sendEmailMqMessage(emailVO);
        return Result.success(codeResult.get("uuid"));
    }

    @GetMapping("/options")
    @ApiOperation("获取前端的下拉列表")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getSelectList() {
        return verificationCodeService.getSelectList();
    }
}
