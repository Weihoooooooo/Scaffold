package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Getter
@RequiredArgsConstructor
public enum OverdueEnum {
    OVERDUE(1, "已过期"),
    NO_OVERDUE(0, "未过期");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
