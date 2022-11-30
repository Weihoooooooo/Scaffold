package com.jcweiho.scaffold.common.annotation;

import java.lang.annotation.*;

/**
 * 标记该方法为枚举类转化方法
 *
 * @author Weiho
 * @since 2022/11/20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumConvertMethod {
}
