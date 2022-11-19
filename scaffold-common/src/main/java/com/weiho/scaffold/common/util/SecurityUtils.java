package com.weiho.scaffold.common.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security工具类
 *
 * @author Weiho
 * @since 2022/8/6
 */
@UtilityClass
public class SecurityUtils {
    /**
     * 获取当前登录的用户信息
     *
     * @return 用户信息
     */
    public UserDetails getUserDetails() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(ResultCodeEnum.SYSTEM_FORBIDDEN, I18nMessagesUtils.get("security.login.overdue"));
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
            return userDetailsService.loadUserByUsername(userDetails.getUsername());
        }
        throw new BadRequestException(ResultCodeEnum.SYSTEM_FORBIDDEN, I18nMessagesUtils.get("security.login.not.found"));
    }

    /**
     * 获取当前系统用户名称
     *
     * @return 系统用户名称
     */
    public String getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(ResultCodeEnum.SYSTEM_FORBIDDEN, I18nMessagesUtils.get("security.login.overdue"));
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 获取系统用户的ID
     *
     * @return 系统用户的ID
     */
    public Long getUserId() {
        return new JSONObject(getUserDetails()).get("id", Long.class);
    }
}
