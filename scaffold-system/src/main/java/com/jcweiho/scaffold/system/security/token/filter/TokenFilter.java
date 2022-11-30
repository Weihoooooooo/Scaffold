package com.jcweiho.scaffold.system.security.token.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.util.StringUtils;
import com.jcweiho.scaffold.system.security.service.OnlineUserService;
import com.jcweiho.scaffold.system.security.service.impl.OnlineUserServiceImpl;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import com.jcweiho.scaffold.system.security.vo.online.OnlineUserVO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求的 Token 过滤器
 * 获取每个请求的 Token 并且判断是否合法
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Slf4j
public class TokenFilter extends GenericFilterBean {
    private final TokenUtils tokenUtils;

    //过滤器构造方法(注入TokenUtils)
    public TokenFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //获取请求对象
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //获取请求的URI
        String requestUri = httpServletRequest.getRequestURI();

        //初始化对象
        OnlineUserVO onlineUser = null;
        String authToken = "";

        //判断是否存在Token
        try {
            //获取Spring容器中的对象(由于过滤器在Spring容器外，通过工具类获取)
            ScaffoldSystemProperties properties = SpringUtil.getBean(ScaffoldSystemProperties.class);
            OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserServiceImpl.class);

            //获取请求头中的token
            authToken = tokenUtils.getTokenFromRequest(httpServletRequest);
            if (StringUtils.isBlank(authToken)) {
                //没有token则进入Spring Security的过滤链中抛出异常
                filterChain.doFilter(httpServletRequest, servletResponse);
                return;
            }
            //有token则根据token去缓存中查询用户信息
            onlineUser = onlineUserService.getOne(properties.getJwtProperties().getOnlineKey() + authToken, (HttpServletResponse) servletResponse);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        }

        //根据token去Spring Security进行授权
        //从token中获取用户名
        String username = StringUtils.isNotBlank(authToken) ? tokenUtils.getUsernameFromToken(authToken) : null;
        if (ObjectUtil.isNotNull(onlineUser) && StringUtils.isNotBlank(username) //Redis中有在线用户信息并且用户名不为空
                //Spring Security中还未进行授权
                && ObjectUtil.isNull(SecurityContextHolder.getContext().getAuthentication())
                // token是否与已经登录的一致(缓存中的token未过期)
                && tokenUtils.validateToken(authToken)) {
            //根据token获取Spring Security用户信息
            UserDetails userDetails = tokenUtils.getUserDetails(authToken);
            //Spring Security进行手动鉴权
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            //放入Spring Security安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security -> 将身份验证设置为安全上下文,身份:{{}},uri:{{}}", authentication.getName(), requestUri);
        } else {
            //否则移除缓存中的Token
            tokenUtils.removeToken(authToken);
            log.info("Security -> 找不到有效的 JWT 令牌,uri:{{}}", requestUri);
        }
        //进入Spring Security过滤器链
        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
