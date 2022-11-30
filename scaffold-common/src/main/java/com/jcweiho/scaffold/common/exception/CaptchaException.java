package com.jcweiho.scaffold.common.exception;

import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;

/**
 * 验证码生成异常
 *
 * @author Weiho
 * @since 2022/8/1
 */
@Getter
public class CaptchaException extends RuntimeException {
    private final Integer code;
    private final String msg;

    /**
     * 默认错误信息
     */
    public CaptchaException() {
        super("Captcha -> [验证码生成异常]");
        this.code = ResultCodeEnum.FAILED.getCode();
        this.msg = I18nMessagesUtils.get("exception.captcha.error");
    }
}
