package com.jcweiho.scaffold.system.security.service;

import com.jcweiho.scaffold.common.util.result.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统注销*
 *
 * @author Weiho
 * @since 2022/8/5
 */
public interface LogoutService {
    /**
     * 用户注销
     *
     * @param request 请求对象
     * @return Result
     */
    Result logout(HttpServletRequest request);
}
