package com.jcweiho.scaffold.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jcweiho.scaffold.common.sensitive.IdEncryptSerializer;
import com.jcweiho.scaffold.common.sensitive.enums.IdEncryptStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键加密注解
 *
 * @author Weiho
 * @since 2022/11/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = IdEncryptSerializer.class)
public @interface IdEncrypt {
    /**
     * 主键加密策略，默认DES加密
     */
    IdEncryptStrategy value() default IdEncryptStrategy.DES;
}
