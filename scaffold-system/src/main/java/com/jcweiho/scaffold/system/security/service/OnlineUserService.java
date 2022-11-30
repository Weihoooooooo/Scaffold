package com.jcweiho.scaffold.system.security.service;

import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import com.jcweiho.scaffold.system.security.vo.online.OnlineUserVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 在线用户业务接口
 *
 * @author Weiho
 * @since 2022/7/29
 */
public interface OnlineUserService {
    /**
     * 保存在线用户信息
     *
     * @param jwtUser 授权用户信息
     * @param token   token
     * @param request 请求对象
     */
    void save(JwtUserVO jwtUser, String token, HttpServletRequest request);

    /**
     * 查询全部数据，不分页
     *
     * @param filter 包含的内容
     * @return 结果列表
     */
    List<OnlineUserVO> getAll(String filter, int type);

    /**
     * 查询全部数据，分页
     *
     * @param filter   包含的内容
     * @param pageable 分页组件
     * @return 结果列表
     */
    Map<String, Object> getAll(String filter, int type, Pageable pageable);

    /**
     * 踢出用户
     *
     * @param key 用户的Redis缓存Key
     * @throws Exception 异常
     */
    void kickOut(String key) throws Exception;

    /**
     * 退出登录
     *
     * @param token token
     */
    void logout(String token);

    /**
     * 导出
     *
     * @param all      全部在线用户列表
     * @param response 响应实体
     * @throws IOException IO异常
     */
    void download(List<OnlineUserVO> all, HttpServletResponse response) throws IOException;

    /**
     * 查询用户
     *
     * @param key Redis中的Key
     * @return OnlineUserVO
     */
    OnlineUserVO getOne(String key, HttpServletResponse response);

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param username 用户名
     */
    void checkLoginOnUser(String username, String ignoreToken);
}
