package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.util.enums.EnumSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@RequiredArgsConstructor
public enum FeedbackResultEnum implements EnumSelect {
    UNRESOLVED(0, "未解决"),
    RESOLVED(1, "已解决"),
    OTHER(2, "其他");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
