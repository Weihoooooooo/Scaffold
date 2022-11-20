package com.weiho.scaffold.common.exception;

import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;

/**
 * 错误请求的统一异常处理
 *
 * @author Weiho
 */
@Getter
public class BadRequestException extends RuntimeException {
    private final Integer code;
    private final String msg;

    /**
     * 默认状态码
     *
     * @param message 错误信息
     */
    public BadRequestException(String message) {
        // message用于用户设置抛出错误详情
        super(message);
        //状态码
        this.code = ResultCodeEnum.BAD_REQUEST_ERROR.getCode();
        //状态码配套msg
        this.msg = I18nMessagesUtils.get("result.msg.BAD_REQUEST_ERROR");
    }

    /**
     * 手动输入错误信息
     *
     * @param code    手动选择状态码
     * @param message 手动填入错误信息
     */
    public BadRequestException(ResultCodeEnum code, String message) {
        // message用于用户设置抛出错误详情
        super(message);
        //状态码
        this.code = code.getCode();
        //状态码配套msg
        this.msg = I18nMessagesUtils.get("result.msg." + code.name());
    }

}
