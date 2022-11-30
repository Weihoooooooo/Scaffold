package com.jcweiho.scaffold.system.security.controller;

import com.jcweiho.scaffold.common.annotation.Anonymous;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.security.service.LoginService;
import com.jcweiho.scaffold.system.security.vo.AuthUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "系统授权")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;

    @Anonymous
    @ApiOperation("登录授权接口")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody @Validated AuthUserVO authUserVO, HttpServletRequest request) throws Exception {
        return loginService.login(authUserVO, request);
    }

    @Anonymous
    @ApiOperation("获取验证码")
    @GetMapping(value = "/verifyCode")
    @RateLimiter(limitType = LimitType.IP)
    public Map<String, Object> getVerifyCode() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //获取验证码信息并打包成Json
        return loginService.getVerifyCodeInfo();
    }
}
