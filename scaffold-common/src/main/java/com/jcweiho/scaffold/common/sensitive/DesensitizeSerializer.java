package com.jcweiho.scaffold.common.sensitive;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.jcweiho.scaffold.common.annotation.Desensitize;
import com.jcweiho.scaffold.common.sensitive.enums.DesensitizeStrategy;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化注解自定义实现
 *
 * @author Weiho
 * @since 2022/8/24
 */
public class DesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeStrategy strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(strategy.desensitize().apply(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Desensitize annotation = property.getAnnotation(Desensitize.class);
        if (ObjectUtil.isNotNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.value();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
