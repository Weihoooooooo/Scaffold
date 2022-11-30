package com.jcweiho.scaffold.common.handler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcweiho.scaffold.common.annotation.NotControllerResponseAdvice;
import com.jcweiho.scaffold.common.exception.ResponsePackException;
import com.jcweiho.scaffold.common.util.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Controller返回值默认打包成Result.success(Object data)
 * 若想请求失败或者调用Result的其他重载方法需要加上注解@NotControllerResponseAdvice
 *
 * @author Weiho
 */
@SuppressWarnings("all")
@RestControllerAdvice(basePackages = {"com.jcweiho.scaffold"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // response是Result类型，或者注释了NotControllerResponseAdvice都不进行包装
        return !(returnType.getParameterType().isAssignableFrom(Result.class)
                || returnType.hasMethodAnnotation(NotControllerResponseAdvice.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // String类型不能直接包装
        // 由于Spring官方序列化是Jackson,对Controller的返回体进行包装,此处只能用Jackson
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //解决Jackson不支持java8的时间类型，添加一个时间模块
                objectMapper.registerModule(new JavaTimeModule());
                // 将数据包装在Result里后转换为json串进行返回
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (JsonProcessingException e) {
                //抛出打包异常错误
                throw new ResponsePackException();
            }
        }
        return Result.success(body);
    }
}
