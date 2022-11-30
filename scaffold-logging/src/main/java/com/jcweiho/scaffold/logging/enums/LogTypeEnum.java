package com.jcweiho.scaffold.logging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Weiho
 * @since 2022/8/7
 */
@Getter
@AllArgsConstructor
public enum LogTypeEnum {
    INFO("INFO"),
    ERROR("ERROR");

    private final String msg;
}
