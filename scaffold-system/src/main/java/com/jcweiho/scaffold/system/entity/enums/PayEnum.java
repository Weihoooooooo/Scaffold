package com.jcweiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jcweiho.scaffold.common.annotation.EnumConvertMethod;
import com.jcweiho.scaffold.common.util.enums.Enum;
import com.jcweiho.scaffold.common.util.enums.EnumUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Weiho
 * @since 2023/1/7
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayEnum implements Enum {
    UNPAID(0, "未支付"),
    PAID(1, "已支付");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PayEnum convert(Integer key) {
        return EnumUtils.convertEnum(PayEnum.class, key);
    }
}
