package com.jcweiho.scaffold.redis.limiter.annotation;

import com.jcweiho.scaffold.redis.limiter.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解(基于Redis与Lua实现)
 *
 * @author <a href="https://mp.weixin.qq.com/s/rzz2tgBBJpWz7gjmEfz2XQ">参考链接</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default "Scaffold:Rate:Limit:";

    /**
     * 限流时间  默认60
     */
    int time() default 60;

    /**
     * 限流时间内能访问多少次 默认80
     */
    int count() default 80;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
