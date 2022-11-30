package com.jcweiho.scaffold.system.security.controller;

import com.jcweiho.scaffold.common.annotation.Anonymous;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.system.security.service.LogoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统注销
 *
 * @author Weiho
 */
@Api(tags = "系统注销")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogoutController {
    private final LogoutService logoutService;

    @Anonymous
    @ApiOperation("注销登录")
    @DeleteMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return logoutService.logout(request);
    }
}
