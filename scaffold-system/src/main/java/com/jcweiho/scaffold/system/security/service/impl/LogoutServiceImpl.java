package com.jcweiho.scaffold.system.security.service.impl;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.system.security.service.LogoutService;
import com.jcweiho.scaffold.system.security.service.OnlineUserService;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统注销
 *
 * @author Weiho
 * @since 2022/8/5
 */
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final OnlineUserService onlineUserService;
    private final TokenUtils tokenUtils;

    @Override
    public Result logout(HttpServletRequest request) {
        onlineUserService.logout(tokenUtils.getTokenFromRequest(request));
        return Result.success(I18nMessagesUtils.get("logout.success.tip"));
    }
}
