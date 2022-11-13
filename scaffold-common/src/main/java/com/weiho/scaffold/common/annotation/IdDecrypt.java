package com.weiho.scaffold.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.weiho.scaffold.common.sensitive.enums.IdDecryptStrategy;
import com.weiho.scaffold.common.sensitive.id.IdDecryptDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonDeserialize(using = IdDecryptDeserializer.class)
public @interface IdDecrypt {
    /**
     * 主键解密策略，默认DES解密
     */
    IdDecryptStrategy value() default IdDecryptStrategy.DES;
}
