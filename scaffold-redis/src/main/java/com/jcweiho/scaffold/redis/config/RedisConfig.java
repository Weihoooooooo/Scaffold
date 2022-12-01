/* Copyright 2018 Elune,hu peng
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcweiho.scaffold.redis.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.jcweiho.scaffold.redis.serializer.CacheableRedisSerializer;
import com.jcweiho.scaffold.redis.serializer.FastJsonRedisSerializer;
import com.jcweiho.scaffold.redis.serializer.StringRedisSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Redis配置 (防止出现SpringBoot2.x缓存Redis序列化问题)
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
public class RedisConfig extends CachingConfigurerSupport {
    private final ApplicationContext applicationContext;

    /**
     * 设置 Redis 数据默认过期时间。默认1天
     * 设置 @Cacheable 序列化方式
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new CacheableRedisSerializer<>(Object.class)))
                .disableCachingNullValues();//设置不缓存空值
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(buildInitCaches())
                .build();
    }

    private Map<String, RedisCacheConfiguration> buildInitCaches() {
        HashMap<String, RedisCacheConfiguration> cacheConfigMap = new HashMap<>();
        Arrays.stream(applicationContext.getBeanNamesForType(Object.class))
                .map(applicationContext::getType).filter(Objects::nonNull)
                .forEach(clazz -> ReflectionUtils.doWithMethods(clazz, method -> {
                            ReflectionUtils.makeAccessible(method);
                            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                            if (ObjectUtil.isNotNull(cacheable)) {
                                for (String cache : cacheable.cacheNames()) {
                                    RedisSerializationContext.SerializationPair<Object> sp = RedisSerializationContext.SerializationPair
                                            .fromSerializer(new CacheableRedisSerializer<>(method.getGenericReturnType()));
                                    cacheConfigMap.put(cache, RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(sp).entryTtl(Duration.ofDays(1)));
                                }
                            }
                        })
                );
        return cacheConfigMap;
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    @SuppressWarnings("all")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // value值的序列化采用FastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 自定义缓存key生成策略，默认将使用该策略
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            Map<String, Object> container = new HashMap<>(4);
            Class<?> targetClassClass = target.getClass();
            // 类地址
            container.put("class", targetClassClass.toGenericString());
            // 方法名称
            container.put("methodName", method.getName());
            // 包名称
            container.put("package", targetClassClass.getPackage());
            // 参数列表
            for (int i = 0; i < params.length; i++) {
                container.put(String.valueOf(i), params[i]);
            }
            // 转为JSON字符串
            String jsonString = JSON.toJSONString(container);
            // 做SHA256 Hash计算，得到一个SHA256摘要作为Key
            return DigestUtils.sha256Hex(jsonString);
        };
    }

    /**
     * Redis 异常处理
     *
     * @return
     */
    @Bean
    @Override
    @SuppressWarnings("all")
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        log.info("Redis -> [Redis缓存错误处理程序初始化]");
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis -> Redis发生句柄缓存获取错误:key -> [{}],error -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis -> Redis发生句柄缓存放置错误:key -> [{}],value -> [{}],error -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis -> Redis发生句柄缓存逐出错误:key -> [{}],error -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis -> Redis发生句柄缓存清除错误:error -> [{}]", e);
            }
        };
    }

    /**
     * 读取 Lua 限流脚本
     */
    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/RateLimiterScript.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
