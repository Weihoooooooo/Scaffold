package com.weiho.scaffold.common.el;

import com.weiho.scaffold.common.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限判断
 *
 * @author Weiho
 * @since 2022/8/29
 */
@Service(value = "el")
public class ElPermissionConfig {
    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getUserDetails().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("root") || Arrays.stream(permissions).allMatch(elPermissions::contains);
    }
}
