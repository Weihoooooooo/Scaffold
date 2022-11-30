package com.jcweiho.scaffold.common.util.result;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcweiho.scaffold.common.util.result.code.StatusCode;
import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 自定义返回体
 *
 * @author Weiho
 */
@Data
@ApiModel("统一返回体")
public class Result {
    @ApiModelProperty("当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("状态信息")
    private String msg;

    @ApiModelProperty("返回对象")
    private Object data;

    /**
     * 通过静态方法返回默认成功的代码和信息以及携带一个data对象
     *
     * @param data 返回的数据对象
     * @return Json
     */
    public static Result success(Object data) {
        return new Result(data);
    }

    /**
     * 通过静态方法返回自定义代码与自定义信息
     *
     * @param code 错误码实体(枚举类)
     * @return Json
     */
    public static Result of(StatusCode code) {
        return new Result(code);
    }

    /**
     * 通过静态方法返回自定义代码与自定义信息以及携带一个data对象
     *
     * @param code 错误码实体(枚举类)
     * @param data 返回的数据对象
     * @return Json
     */
    public static Result of(StatusCode code, Object data) {
        return new Result(code, data);
    }

    /**
     * 通过静态方法手动设置返回对象
     *
     * @param code 要返回的状态码
     * @param msg  要返回的状态信息
     * @param data 要返回的数据对象
     * @return Json
     */
    public static Result of(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    @Deprecated
    public Result() {
    }

    // 默认返回成功状态码，数据对象
    public Result(Object data) {
        this.time = LocalDateTime.now();
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = I18nMessagesUtils.get("result.msg.SUCCESS");
        this.data = data;
    }

    // 只返回状态码
    public Result(StatusCode code) {
        this.time = LocalDateTime.now();
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = null;
    }

    // 返回指定状态码，数据对象
    public Result(StatusCode code, Object data) {
        this.time = LocalDateTime.now();
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    //手动设置返回对象
    public Result(int code, String msg, Object data) {
        this.time = LocalDateTime.now();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
