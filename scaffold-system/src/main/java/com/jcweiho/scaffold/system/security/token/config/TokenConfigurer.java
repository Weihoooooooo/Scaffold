package com.jcweiho.scaffold.system.security.token.config;

import com.jcweiho.scaffold.system.security.token.filter.TokenFilter;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 将TokenFilter过滤器添加到Spring Security的过滤链最高优先级中
 *
 * @author Weiho
 * @since 2022/7/29
 */
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenUtils tokenUtils;

    //构造方法注入TokenUtils
    public TokenConfigurer(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TokenFilter customFilter = new TokenFilter(tokenUtils);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
