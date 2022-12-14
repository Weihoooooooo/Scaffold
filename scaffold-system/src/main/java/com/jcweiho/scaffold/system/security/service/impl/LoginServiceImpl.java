package com.jcweiho.scaffold.system.security.service.impl;

import cn.hutool.core.builder.GenericBuilder;
import cn.hutool.core.util.IdUtil;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.exception.CaptchaException;
import com.jcweiho.scaffold.common.exception.SecurityException;
import com.jcweiho.scaffold.common.util.CollUtils;
import com.jcweiho.scaffold.common.util.RsaUtils;
import com.jcweiho.scaffold.common.util.SecurityUtils;
import com.jcweiho.scaffold.common.util.StringUtils;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.redis.util.RedisUtils;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.security.service.LoginService;
import com.jcweiho.scaffold.system.security.service.OnlineUserService;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import com.jcweiho.scaffold.system.security.vo.AuthUserVO;
import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import com.jcweiho.scaffold.system.service.RoleService;
import com.jcweiho.scaffold.system.service.UserService;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final ScaffoldSystemProperties properties;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtils redisUtils;
    private final TokenUtils tokenUtils;
    private final OnlineUserService onlineUserService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public Map<String, Object> getVerifyCodeInfo() {
        Captcha captcha;
        //????????????(3?????????)
        try {
            captcha = (Captcha) Class.forName(properties.getCodeProperties().getType().getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new CaptchaException();
        }
        //?????????????????????
        captcha.setHeight(properties.getCodeProperties().getHeight());
        //?????????????????????
        captcha.setWidth(properties.getCodeProperties().getWidth());
        //???????????????3???
        captcha.setLen(3);
        //??????????????????
        String result;
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        //??????UUID????????????????????????????????????
        String uuid = properties.getCodeProperties().getCodeKey() + IdUtil.simpleUUID();
        //?????????Redis???
        redisUtils.set(uuid, result, properties.getCodeProperties().getExpiration(), TimeUnit.MINUTES);
        return GenericBuilder.of(HashMap<String, Object>::new)
                .with(Map::put, "uuid", uuid)
                .with(Map::put, "code", captcha.toBase64()).build();
    }

    @Override
    public Map<String, Object> login(AuthUserVO authUserVO, HttpServletRequest request) throws Exception {
        try {
            //??????RSA????????????
            String password = RsaUtils.decryptByPrivateKey(properties.getRsaProperties().getPrivateKey(), authUserVO.getPassword());
            //???????????????
            String code = (String) redisUtils.get(authUserVO.getUuid());
            //???????????????
            redisUtils.del(authUserVO.getUuid());
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException(I18nMessagesUtils.get("captcha.exception.not.found"));
            }
            if (StringUtils.isBlank(authUserVO.getCode()) || !authUserVO.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException(I18nMessagesUtils.get("captcha.exception.error"));
            }
            //????????????
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUserVO.getUsername(), password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            //??????Security???????????????
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //????????????????????????token??????
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = tokenUtils.generateToken(userDetails);
            final JwtUserVO jwtUserVO = (JwtUserVO) authentication.getPrincipal();
            //????????????????????????
            onlineUserService.save(jwtUserVO, token, request);
            //??????????????????????????????????????????????????????token
            if (properties.getJwtProperties().getSingleLogin()) {
                onlineUserService.checkLoginOnUser(authUserVO.getUsername(), token);
            }
            // ????????????????????????
            List<Role> roles = roleService.findListByUser(userService.findByUsername(SecurityUtils.getUsername()));
            return GenericBuilder.of(HashMap<String, Object>::new)
                    .with(Map::put, "token", properties.getJwtProperties().getTokenStartWith() + token)
                    .with(Map::put, "userInfo", jwtUserVO)
                    .with(Map::put, "maxLevel", CollUtils.min(roles.stream().map(Role::getLevel).collect(Collectors.toList())))
                    .build();
        } catch (IllegalStateException e) {
            throw new SecurityException(ResultCodeEnum.SYSTEM_FORBIDDEN, I18nMessagesUtils.get("login.error"));
        }
    }

    @Override
    public Result verifyAccount(String password) throws Exception {
        User user = userService.findByUsername(SecurityUtils.getUsername());
        // Password??????
        String decryptPass = RsaUtils.decryptByPrivateKey(properties.getRsaProperties().getPrivateKey(), password);
        if (passwordEncoder.matches(decryptPass, user.getPassword())) {
            return Result.of(ResultCodeEnum.SUCCESS);
        } else {
            return Result.of(ResultCodeEnum.SUCCESS, "Password error!");
        }
    }
}
