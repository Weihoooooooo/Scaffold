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
 * @since 2022/11/2
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OverdueEnum implements Enum {
    OVERDUE(1, "已过期"),
    NO_OVERDUE(0, "未过期");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OverdueEnum convert(Integer key) {
        return EnumUtils.convertEnum(OverdueEnum.class, key);
    }
}
