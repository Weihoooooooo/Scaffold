package com.weiho.scaffold.common.exception;

import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 验证码生成异常
 *
 * @author Weiho
 * @date 2022/8/1
 */
@Getter
public class CaptchaException extends RuntimeException {
    private final Integer code;
    private final String msg;

    /**
     * 默认错误信息
     */
    public CaptchaException(String message) {
        super("Captcha -> 验证码生成异常");
        this.code = ResultCodeEnum.FAILED.getCode();
        this.msg = message;
    }
}