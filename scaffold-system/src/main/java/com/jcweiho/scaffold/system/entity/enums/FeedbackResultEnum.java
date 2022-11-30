package com.jcweiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jcweiho.scaffold.common.annotation.EnumConvertMethod;
import com.jcweiho.scaffold.common.util.enums.Enum;
import com.jcweiho.scaffold.common.util.enums.EnumUtils;
import com.jcweiho.scaffold.i18n.I18nKey;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FeedbackResultEnum implements Enum, I18nKey {
    UNRESOLVED(0, "未解决", "enums.unresolved"),
    RESOLVED(1, "已解决", "enums.resolved"),
    OTHER(2, "其他", "enums.other");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    private final String i18nKey;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FeedbackResultEnum convert(Integer key) {
        return EnumUtils.convertEnum(FeedbackResultEnum.class, key);
    }

    public String getDisplay() {
        return I18nMessagesUtils.get(i18nKey);
    }
}
