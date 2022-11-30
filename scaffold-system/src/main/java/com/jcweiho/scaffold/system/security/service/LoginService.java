package com.jcweiho.scaffold.system.security.service;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.system.security.vo.AuthUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginService {
    /**
     * 获取验证码的Base64编码和uuid
     *
     * @return 验证码Base64编码和uuid, Map的key分别为 "code_src" 和 "uuid"
     */
    Map<String, Object> getVerifyCodeInfo() throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * 登录授权
     *
     * @param authUserVO 前端VO
     * @param request    请求
     * @return 返回token和用户信息给前端
     */
    Map<String, Object> login(AuthUserVO authUserVO, HttpServletRequest request) throws Exception;

    /**
     * 验证当前登录用户的密码
     *
     * @param password 密码
     * @return /
     */
    Result verifyAccount(String password) throws Exception;
}
