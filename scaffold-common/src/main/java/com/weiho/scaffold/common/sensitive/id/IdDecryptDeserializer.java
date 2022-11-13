package com.weiho.scaffold.common.sensitive.id;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.sensitive.enums.IdDecryptStrategy;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Weiho
 * @since 2022/11/13
 */
public class IdDecryptDeserializer extends JsonDeserializer<Long> implements ContextualDeserializer {
    private IdDecryptStrategy strategy;

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return strategy.desensitize().apply(p.getText());
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        IdDecrypt annotation = property.getAnnotation(IdDecrypt.class);
        if (Objects.nonNull(annotation) && Objects.equals(Long.class, property.getType().getRawClass())) {
            this.strategy = annotation.value();
            return this;
        }
        return ctxt.findContextualValueDeserializer(property.getType(), property);
    }
}
