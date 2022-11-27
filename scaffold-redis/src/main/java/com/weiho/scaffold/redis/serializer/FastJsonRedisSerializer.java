package com.weiho.scaffold.redis.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * 重写FastJson的序列化器
 *
 * @param <T>
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    private final Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) {
        if (ObjectUtil.isNull(bytes) || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(str, clazz);
    }

}

