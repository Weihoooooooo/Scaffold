package com.jcweiho.scaffold.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于匿名访问方法
 *
 * @author yshopmall - <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Anonymous {
}
