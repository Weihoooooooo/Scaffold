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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用了MapStruct进行对象转换的时候，Enum枚举默认将属性名带去转化，而不是key或者value
 * 在书写DTO的时候最好是使用枚举类型而不是String类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SexEnum implements Enum, I18nKey {
    WOMAN(0, "女", "enums.female"),
    MAN(1, "男", "enums.male");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    private final String i18nKey;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SexEnum convert(Integer key) {
        return EnumUtils.convertEnum(SexEnum.class, key);
    }

    public String getDisplay() {
        return I18nMessagesUtils.get(i18nKey);
    }
}
