package com.jcweiho.scaffold.redis.serializer;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.jcweiho.scaffold.common.util.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.util.annotation.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 重新序列化器
 *
 * @author yshopmall - <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
public class StringRedisSerializer implements RedisSerializer<Object> {
    private final Charset charset;

    public StringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    private StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (ObjectUtil.isNull(bytes) ? null : new String(bytes, charset));
    }

    @Override
    public @Nullable
    byte[] serialize(Object object) {
        String string = JSON.toJSONString(object);
        if (StringUtils.isBlank(string)) {
            return null;
        }
        string = string.replace("\"", "");
        return string.getBytes(charset);
    }
}
