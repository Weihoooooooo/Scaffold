package com.jcweiho.scaffold.redis.serializer;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Type;

/**
 * 专门为@Cacheable注解缓存设计的序列化器
 *
 * @author Weiho
 * @since 2022/8/4
 */
public class CacheableRedisSerializer<T> implements RedisSerializer<T> {
    private final Type type;

    public CacheableRedisSerializer(Type type) {
        super();
        this.type = type;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return JSON.toJSONBytes(t);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return JSON.parseObject(bytes, type);
    }
}
