package com.jcweiho.scaffold.common.util.enums;

import cn.hutool.core.util.EnumUtil;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.ListUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 枚举类工具类
 *
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
    public <T extends java.lang.Enum<?> & Enum> List<EnumSelectVO> getEnumSelect(Class<T> clazz) {
        List<EnumSelectVO> selects = ListUtils.list(false);
        for (T o : clazz.getEnumConstants()) {
            selects.add(new EnumSelectVO(o.getKey(), o.getDisplay(), o.name()));
        }
        return selects;
    }

    /**
     * 根据传入的key寻找合适的枚举类对象返回(用于MVC对枚举类的自定义转化)
     * 必须实现Enum接口
     *
     * @param clazz 枚举类
     * @param key   枚举类的Key
     * @param <T>   泛型
     * @return 符合条件的枚举类
     */
    public <T extends java.lang.Enum<?> & Enum> T convertEnum(Class<T> clazz, Integer key) {
        for (T o : clazz.getEnumConstants()) {
            if (o.getKey().equals(key)) {
                return o;
            }
        }
        throw new BadRequestException("找不到该Key对应的枚举类");
    }
}
