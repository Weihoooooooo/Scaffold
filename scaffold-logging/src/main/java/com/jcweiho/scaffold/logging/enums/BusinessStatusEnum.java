package com.jcweiho.scaffold.logging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志操作状态(  0-正常  ,  1-失败)
 *
 * @author Weiho
 * @since 2022/8/6
 */
@Getter
@AllArgsConstructor
public enum BusinessStatusEnum {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 失败
     */
    FAIL(1, "FAIL");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String value;
}
