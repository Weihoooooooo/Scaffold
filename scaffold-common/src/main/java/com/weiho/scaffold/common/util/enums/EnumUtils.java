package com.weiho.scaffold.common.util.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/3
 */
@UtilityClass
public class EnumUtils extends EnumUtil {

    /**
     * 将枚举类转换成前端所需的下拉Select数据
     *
     * @param clazz 枚举类
     * @param <T>   泛型
     * @return /
     */
    public <T extends Enum<?> & EnumSelect> List<EnumSelectVO> getEnumSelect(Class<T> clazz) {
        List<EnumSelectVO> selects = new ArrayList<>();
        for (T o : clazz.getEnumConstants()) {
            selects.add(new EnumSelectVO(o.getKey(), o.getDisplay(), o.name()));
        }
        return selects;
    }
}
