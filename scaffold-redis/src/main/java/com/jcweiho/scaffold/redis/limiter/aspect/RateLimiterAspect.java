package com.jcweiho.scaffold.redis.limiter.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.RateLimitException;
import com.jcweiho.scaffold.common.util.IpUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流AOP
 *
 * @author <a href="https://mp.weixin.qq.com/s/rzz2tgBBJpWz7gjmEfz2XQ">参考链接</a>
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAspect {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisScript<Long> limitScript;
    private final ScaffoldSystemProperties properties;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        try {
            if (properties.getRateLimiterProperties().isEnabled()) {
                Long number = redisTemplate.execute(limitScript, keys, count, time);
                if (ObjectUtil.isNull(number) || number.intValue() > count) {
                    throw new RateLimitException(I18nMessagesUtils.get("exception.rate.limit.error"));
                }
            }
        } catch (RateLimitException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    @SuppressWarnings("all")
    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtils.getIp(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())).append(":");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append(":").append(method.getName());
        return stringBuffer.toString();
    }
}
