package com.jcweiho.scaffold.common.sensitive;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.sensitive.enums.IdEncryptStrategy;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Weiho
 * @since 2022/11/13
 */
public class IdEncryptSerializer extends JsonSerializer<Long> implements ContextualSerializer {
    private IdEncryptStrategy strategy;

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(strategy.desensitize().apply(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        IdEncrypt annotation = property.getAnnotation(IdEncrypt.class);
        if (ObjectUtil.isNotNull(annotation) && Objects.equals(Long.class, property.getType().getRawClass())) {
            this.strategy = annotation.value();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
