package com.jcweiho.scaffold.system.security.config;

import cn.hutool.core.util.ObjectUtil;
import com.jcweiho.scaffold.common.annotation.Anonymous;
import com.jcweiho.scaffold.system.security.exception.JwtAccessDeniedHandler;
import com.jcweiho.scaffold.system.security.exception.JwtAuthenticationEntryPoint;
import com.jcweiho.scaffold.system.security.token.config.TokenConfigurer;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Spring Security配置文件
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//开启Spring Security注解支持
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenUtils tokenUtils;
    private final CorsFilter corsFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ApplicationContext applicationContext;

    /**
     * 去除 ROLE_ 前缀
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    /**
     * 密码加密方式
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 将 Token 过滤器引入
     */
    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(tokenUtils);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //扫描标记 URL ：@Anonymous
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                ((RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping")).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            Anonymous anonymous = handlerMethod.getMethodAnnotation(Anonymous.class);
            if (ObjectUtil.isNotNull(anonymous)) {
                assert infoEntry.getKey().getPatternsCondition() != null;
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }

        return httpSecurity
                //禁用 CSRF
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                //授权异常
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //防止iframe造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()

                //不创建会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //放行某些资源
                .and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        // WebSocket资源
                        "/websocket/**",
                        // 头像
                        "/local/avatar/**",
                        // Postman测试用脚本
                        "/local/postman/forge.js",
                        // 系统Logo
                        "/local/logo/logo.png",
                        // ICON列表的资源
                        "/local/iconList/**",
                        // 本地存储资源
                        "/local/storage/**"
                ).permitAll()
                //放行Swagger文档
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                .antMatchers("/v2/api-docs-ext").permitAll()

                // 放行Actuator
                .antMatchers("/actuator/**").permitAll()

                // 放行Monitor
                .antMatchers("/monitor/**").permitAll()

                //放行公开对外的api
                .antMatchers("/scaffold-open/api/v1/**").permitAll()

                //放行Druid连接池管理页面
                .antMatchers("/druid/**").permitAll()

                //放行 OPTION 请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                //放行favicon.ico图标
                .antMatchers("/favicon.ico").permitAll()

                //放行添加了 @Anonymous 注解的方法
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()

                //所有请求都需要验证
                .anyRequest().authenticated()

                //将Token过滤器加入Spring Security
                .and().apply(securityConfigurerAdapter()).and().build();
    }
}
