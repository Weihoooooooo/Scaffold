package com.jcweiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jcweiho.scaffold.common.annotation.EnumConvertMethod;
import com.jcweiho.scaffold.common.util.enums.Enum;
import com.jcweiho.scaffold.common.util.enums.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/12/14
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IsLiveEnum implements Enum {
    IS_LIVE(1, "已居住"),

    NO_LIVE(0, "未居住");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static IsLiveEnum convert(Integer key) {
        return EnumUtils.convertEnum(IsLiveEnum.class, key);
    }
}
