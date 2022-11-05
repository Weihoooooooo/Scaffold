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
public enum FeedbackTypeEnum implements EnumSelect {
    REPAIR(0, "报修"),
    COMPLAINT(1, "投诉"),
    PROPOSAL(2, "建议");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
