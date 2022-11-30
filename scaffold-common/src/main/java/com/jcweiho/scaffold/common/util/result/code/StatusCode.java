package com.jcweiho.scaffold.common.util.result.code;

/**
 * 自定义状态码接口
 *
 * @author Weiho
 */
public interface StatusCode {
    /**
     * 获取状态码
     *
     * @return 状态码
     */
    int getCode();

    /**
     * 获取状态信息
     *
     * @return 状态信息
     */
    String getMsg();
}
