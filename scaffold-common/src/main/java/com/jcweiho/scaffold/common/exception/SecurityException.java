package com.jcweiho.scaffold.common.exception;

import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;

/**
 * @author Weiho
 * @since 2022/9/18
 */
@Getter
public class SecurityException extends RuntimeException {
    private final Integer code;
    private final String msg;

    public SecurityException(ResultCodeEnum code, String message) {
        super(message);
        this.code = code.getCode();
        this.msg = I18nMessagesUtils.get("result.msg." + code.name());
    }
}
