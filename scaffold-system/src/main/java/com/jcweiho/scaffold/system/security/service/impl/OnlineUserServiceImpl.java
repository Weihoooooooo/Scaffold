package com.jcweiho.scaffold.system.security.service.impl;

import com.alibaba.fastjson2.JSON;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.ResponsePackException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.redis.util.RedisUtils;
import com.jcweiho.scaffold.system.security.service.OnlineUserService;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import com.jcweiho.scaffold.system.security.vo.online.OnlineUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线用户业务实现类
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {
    private final ScaffoldSystemProperties properties;
    private final RedisUtils redisUtils;
    private final TokenUtils tokenUtils;

    @Override
    public void save(JwtUserVO jwtUser, String token, HttpServletRequest request) {
        //获取IP地址
        String ip = IpUtils.getIp(request);
        //获取用户登录时的浏览器
        String browser = IpUtils.getBrowser(request);
        //获取登录时候用户IP地址所在城市
        String address = IpUtils.getCityInfo(ip);
        OnlineUserVO onlineUser;
        try {
            //构造实体
            onlineUser = new OnlineUserVO(jwtUser.getUsername(),
                    browser, ip, address, DesUtils.desEncrypt(token),
                    DateUtils.getNowDateFormat(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS));
            //存入Redis
            redisUtils.set(properties.getJwtProperties().getOnlineKey() + token,
                    onlineUser, properties.getJwtProperties().getTokenValidityInSeconds() / 1000);
        } catch (Exception e) {
            throw new ResponsePackException();
        }
    }

    @Override
    public List<OnlineUserVO> getAll(String filter, int type) {
        List<String> keys;
        if (type == 1) {
            keys = redisUtils.scan(":OnlineUser:*");
        } else {
            keys = redisUtils.scan(properties.getJwtProperties().getOnlineKey() + "*");
        }
        Collections.reverse(keys);
        List<OnlineUserVO> onlineUsers = ListUtils.list(false);
        for (String key : keys) {
            OnlineUserVO onlineUser = JSON.parseObject(redisUtils.getString(key), OnlineUserVO.class);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUser.toString().contains(filter)) {
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    @Override
    public Map<String, Object> getAll(String filter, int type, Pageable pageable) {
        List<OnlineUserVO> onlineUserVOS = getAll(filter, type);
        return PageUtils.toPageContainer(
                PageUtils.toPage(pageable.getPageNumber(), pageable.getPageSize(), onlineUserVOS),
                onlineUserVOS.size()
        );
    }

    @Override
    public void kickOut(String key) throws Exception {
        key = properties.getJwtProperties().getOnlineKey() + DesUtils.desDecrypt(key);
        redisUtils.del(key);
    }

    @Override
    public void logout(String token) {
        String username = tokenUtils.getUsernameFromToken(token);
        //清除缓存
        String onlineCacheKey = properties.getJwtProperties().getOnlineKey() + token;
        String avatarKey = redisUtils.getRedisCommonsKey("Avatar", username);
        String menusKey = redisUtils.getRedisCommonsKey("Menus", username);
        String permissionKey = redisUtils.getRedisCommonsKey("Permission", username);
        String rolesKey = redisUtils.getRedisCommonsKey("Roles", username);
        String userKey = redisUtils.getRedisCommonsKey("User", username);
        String detailsKey = properties.getJwtProperties().getDetailKey() + username;
        String tokenKey = properties.getJwtProperties().getTokenKey() + username + ":" + token;
        redisUtils.del(onlineCacheKey, avatarKey, menusKey, permissionKey, rolesKey, userKey, detailsKey, tokenKey);
    }

    @Override
    public void download(List<OnlineUserVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (OnlineUserVO user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUsername());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public OnlineUserVO getOne(String key, HttpServletResponse response) {
        return JSON.parseObject(redisUtils.getString(key), OnlineUserVO.class);
    }

    @Override
    public void checkLoginOnUser(String username, String ignoreToken) {
        List<OnlineUserVO> onlineUsers = getAll(username, 0);
        if (CollUtils.isEmpty(onlineUsers)) {
            return;
        }
        for (OnlineUserVO onlineUser : onlineUsers) {
            if (onlineUser.getUsername().equals(username)) {
                try {
                    String token = DesUtils.desDecrypt(onlineUser.getKey());
                    if (StringUtils.isNotBlank(ignoreToken) && !ignoreToken.equals(token)) {
                        this.kickOut(onlineUser.getKey());
                    } else if (StringUtils.isBlank(ignoreToken)) {
                        this.kickOut(onlineUser.getKey());
                    }
                } catch (Exception e) {
                    log.error("JWT -> 检查用户是否登录错误[{}]", e.getMessage());
                }
            }
        }
    }

}
