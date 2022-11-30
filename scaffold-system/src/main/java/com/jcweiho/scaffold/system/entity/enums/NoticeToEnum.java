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
 * @since 2022/11/2
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NoticeToEnum implements Enum, I18nKey {
    ALL_OWNER(0, "全体业主", "enums.all.owner"),
    ALL_USER(1, "全体员工", "enums.all.user"),
    ALL(2, "全体人员", "enums.all");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    private final String i18nKey;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NoticeToEnum convert(Integer key) {
        return EnumUtils.convertEnum(NoticeToEnum.class, key);
    }

    public String getDisplay() {
        return I18nMessagesUtils.get(i18nKey);
    }
}
