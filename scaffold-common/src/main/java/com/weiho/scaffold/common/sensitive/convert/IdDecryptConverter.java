package com.weiho.scaffold.common.sensitive.convert;

import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.sensitive.enums.IdDecryptStrategy;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 主键解密的全局转换器
 *
 * @author Weiho
 * @since 2022/11/22
 */
public class IdDecryptConverter implements Converter<String, Long>, ConditionalConverter {
    private IdDecryptStrategy strategy;

    /**
     * 要转换的条件 (目标类的字段添加了 @IdDecrypt 注解的字段)
     *
     * @param sourceType the type descriptor of the field we are converting from
     * @param targetType the type descriptor of the field we are converting to
     */
    @Override
    public boolean matches(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        IdDecrypt annotation = targetType.getAnnotation(IdDecrypt.class);
        if (Objects.nonNull(annotation)) {
            this.strategy = annotation.value();
            return targetType.hasAnnotation(IdDecrypt.class);
        } else {
            return false;
        }
    }

    @Override
    public Long convert(@Nullable String source) {
        return strategy.desensitize().apply(source);
    }
}
