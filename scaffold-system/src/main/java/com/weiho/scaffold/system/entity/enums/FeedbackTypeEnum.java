package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.annotation.EnumConvertMethod;
import com.weiho.scaffold.common.util.enums.Enum;
import com.weiho.scaffold.common.util.enums.EnumUtils;
import com.weiho.scaffold.i18n.I18nKey;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FeedbackTypeEnum implements Enum, I18nKey {
    REPAIR(0, "报修", "enums.repair"),
    COMPLAINT(1, "投诉", "enums.complaint"),
    PROPOSAL(2, "建议", "enums.proposal");

    @EnumValue
    @JsonValue
    private final Integer key;

    private final String display;

    private final String i18nKey;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FeedbackTypeEnum convert(Integer key) {
        return EnumUtils.convertEnum(FeedbackTypeEnum.class, key);
    }

    public String getDisplay() {
        return I18nMessagesUtils.get(i18nKey);
    }
}
