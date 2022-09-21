package com.weiho.scaffold.system.security.service.impl;

import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.security.vo.JwtUserVO;
import com.weiho.scaffold.system.service.RoleService;
import com.weiho.scaffold.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Weiho
 * @since 2022/7/29
 */
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new BadRequestException(I18nMessagesUtils.get("user.not.found"));
        } else {
            if (!user.isEnabled()) {
                throw new BadRequestException(I18nMessagesUtils.get("user.not.enabled"));
            }
            return createJwtUserVO(user);
        }
    }

    /**
     * 创建JwtUserVO对象
     *
     * @param user 用户初始对象
     * @return UserDetails
     */
    private UserDetails createJwtUserVO(User user) {
        JwtUserVO jwtUserVO = userService.getBaseJwtUserVO(user);
        jwtUserVO.setAuthorities(roleService.mapToGrantedAuthorities(user.getId(), user.getUsername()));
        return jwtUserVO;
    }
}
