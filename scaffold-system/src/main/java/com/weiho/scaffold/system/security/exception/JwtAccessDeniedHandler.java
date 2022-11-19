package com.weiho.scaffold.system.security.exception;

import com.weiho.scaffold.common.util.ExceptionResponseUtils;
import com.weiho.scaffold.common.util.I18nMessagesUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        ExceptionResponseUtils.sendResponse(response, I18nMessagesUtils.get("access.not.denied"));
    }
}
