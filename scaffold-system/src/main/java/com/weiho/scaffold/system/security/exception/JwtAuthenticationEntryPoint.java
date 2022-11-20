package com.weiho.scaffold.system.security.exception;

import com.weiho.scaffold.i18n.I18nMessagesUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, I18nMessagesUtils.get("no.authentication"));
    }
}
