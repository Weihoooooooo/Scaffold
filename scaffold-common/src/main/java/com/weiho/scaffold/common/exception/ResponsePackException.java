package com.weiho.scaffold.common.exception;

import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;

/**
 * Json打包异常
 * 用于ControllerResponseAdvice统一包装类
 */
@Getter
public class ResponsePackException extends RuntimeException {
    private final Integer code;
    private final String msg;

    /**
     * 默认错误信息
     */
    public ResponsePackException() {
        super("Jackson -> [响应体数据打包错误]");
        this.code = ResultCodeEnum.SYSTEM_FORBIDDEN.getCode();
        this.msg = I18nMessagesUtils.get("result.msg.SYSTEM_FORBIDDEN");
    }

    /**
     * 自定义错误信息
     */
    public ResponsePackException(String message) {
        super(message);
        this.code = ResultCodeEnum.SYSTEM_FORBIDDEN.getCode();
        this.msg = I18nMessagesUtils.get("result.msg.SYSTEM_FORBIDDEN");
    }
}
