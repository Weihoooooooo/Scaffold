package com.weiho.scaffold.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weiho.scaffold.common.sensitive.SensitiveJsonSerializer;
import com.weiho.scaffold.common.sensitive.enums.SensitiveStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段脱敏(姓名,身份证,电话号码),需要用在返回前端的VO中
 *
 * @author Weiho
 * @date 2022/8/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Desensitize {
    /**
     * 脱敏策略(姓名,身份证,电话号码)
     */
    SensitiveStrategy strategy();
}
