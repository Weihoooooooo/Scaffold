package com.jcweiho.scaffold.common.util.result.enums;

import com.jcweiho.scaffold.common.util.result.code.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义状态码
 *
 * @author Weiho
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum implements StatusCode {
    SUCCESS(1200, "请求成功"),
    SYSTEM_FORBIDDEN(1403, "拒绝访问"),//限流
    BAD_REQUEST_ERROR(1400, "请求参数错误"),
    FAILED(1500, "服务器错误"),
    VALIDATE_ERROR(1406, "参数校验失败"),
    RESPONSE_PACK_ERROR(1502, "response返回包装失败");

    private final int code;
    private final String msg;
}
