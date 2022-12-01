package com.jcweiho.scaffold.logging.annotation;

import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author Weiho
 * @since 2022/8/6
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
    /**
     * 模块名称
     */
    @AliasFor("title")
    String value() default "";

    /**
     * 模块名称
     */
    @AliasFor("value")
    String title() default "";

    /**
     * 业务类型 ( 0-其他,1-新增,2-修改,3-删除 )
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean saveRequestData() default true;

    /**
     * 是否保存响应的结果
     */
    boolean saveResponseData() default true;
}
