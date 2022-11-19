package com.weiho.scaffold.common.exception;

import com.weiho.scaffold.common.util.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.code.StatusCode;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 限流的自定义异常处理
 *
 * @author Weiho
 */
@Getter
public class RateLimitException extends RuntimeException {
    private final Integer code;
    private final String msg;

    /**
     * 默认状态码
     *
     * @param message 错误信息
     */
    public RateLimitException(String message) {
        // message用于用户设置抛出错误详情
        super(message);
        //状态码
        this.code = ResultCodeEnum.SYSTEM_FORBIDDEN.getCode();
        //状态码配套msg
        this.msg = I18nMessagesUtils.get("result.msg.SYSTEM_FORBIDDEN");
    }

    /**
     * 手动输入错误信息
     *
     * @param code    手动选择状态码
     * @param message 手动填入错误信息
     */
    public RateLimitException(StatusCode code, String message) {
        // message用于用户设置抛出错误详情
        super(message);
        //状态码
        this.code = code.getCode();
        //状态码配套msg
        this.msg = code.getMsg();
    }
}
