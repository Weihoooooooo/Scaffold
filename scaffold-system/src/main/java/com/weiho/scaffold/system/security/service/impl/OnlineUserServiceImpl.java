package com.weiho.scaffold.system.security.service.impl;

import com.alibaba.fastjson2.JSON;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.exception.ResponsePackException;
import com.weiho.scaffold.common.util.date.DateUtils;
import com.weiho.scaffold.common.util.date.FormatEnum;
import com.weiho.scaffold.common.util.des.DesUtils;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.ip.IpUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.common.util.string.StringUtils;
import com.weiho.scaffold.redis.util.RedisUtils;
import com.weiho.scaffold.system.security.service.OnlineUserService;
import com.weiho.scaffold.system.security.vo.JwtUserVO;
import com.weiho.scaffold.system.security.vo.online.OnlineUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    @Override
    public void save(JwtUserVO jwtUser, String token, HttpServletRequest request) {
        //获取IP地址
        String ip = IpUtils.getIp(request);
        //获取用户登录时的浏览器
        String browser = IpUtils.getBrowser(request);
        //获取登录时候用户IP地址所在城市
        String address = IpUtils.getCityInfo(ip);
        OnlineUserVO onlineUser = null;
        try {
            //构造实体
            onlineUser = new OnlineUserVO(jwtUser.getUsername(),
                    browser, ip, address, DesUtils.desEncrypt(token),
                    DateUtils.getNowDateFormat(FormatEnum.YYYY_MM_DD_HH_MM_SS));
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
        List<OnlineUserVO> onlineUsers = new ArrayList<>();
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
        //清除缓存
        String onlineCacheKey = properties.getJwtProperties().getOnlineKey() + token;
        redisUtils.del(onlineCacheKey);
    }

    @Override
    public void download(List<OnlineUserVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
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
        if (onlineUsers == null || onlineUsers.isEmpty()) {
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
