package com.jcweiho.scaffold.logging.aspect;

import cn.hutool.core.date.TimeInterval;
import com.jcweiho.scaffold.common.util.DateUtils;
import com.jcweiho.scaffold.common.util.IpUtils;
import com.jcweiho.scaffold.common.util.SecurityUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.enums.LogTypeEnum;
import com.jcweiho.scaffold.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Weiho
 * @since 2022/8/7
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
    private final LogService logService;
    private Log logInfo;

    @Before(value = "@annotation(logging)")
    public void deBefore(Logging logging) {
        logInfo = new Log();
        log.info("Log -> 日志对象创建");
    }

    @Around(value = "@annotation(logging)")
    public Object doAround(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        Object result;
        TimeInterval timer = DateUtils.timer();
        result = joinPoint.proceed();
        long time = timer.interval();
        logInfo.setTime(time);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(logging)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Logging logging, Object jsonResult) {
        logInfo.setLogType(LogTypeEnum.INFO.getMsg());
        //获取HttpServletRequest对象
        HttpServletRequest request = IpUtils.getHttpServletRequest();
        setLogInfoIp(logInfo, request);
        logService.saveLogInfo(joinPoint, request, logging, logInfo, null, jsonResult);
    }

    @AfterThrowing(value = "@annotation(logging)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Logging logging, Exception e) {
        logInfo.setLogType(LogTypeEnum.ERROR.getMsg());
        //获取HttpServletRequest对象
        HttpServletRequest request = IpUtils.getHttpServletRequest();
        setLogInfoIp(logInfo, request);
        logService.saveLogInfo(joinPoint, request, logging, logInfo, e, null);
    }

    /**
     * 设置Log对象中关于HttpServletRequest的信息
     *
     * @param logInfo Log对象
     * @param request 请求对象
     */
    public void setLogInfoIp(Log logInfo, HttpServletRequest request) {
        //设置操作用户
        logInfo.setUsername(getUsername());
        //获取IP
        String ip = IpUtils.getIp(request);
        //设置请求的方法
        logInfo.setRequestMethod(request.getMethod());
        //设置请求的URL
        logInfo.setRequestUrl(request.getRequestURI());
        //设置请求的IP
        logInfo.setRequestIp(ip);
        //设置请求的浏览器
        logInfo.setBrowser(IpUtils.getBrowser(request));
        //设置IP所在地
        logInfo.setAddress(IpUtils.getCityInfo(ip));
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
