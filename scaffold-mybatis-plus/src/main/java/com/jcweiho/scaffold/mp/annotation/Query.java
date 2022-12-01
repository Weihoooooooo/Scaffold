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
package com.jcweiho.scaffold.mp.annotation;

import com.jcweiho.scaffold.mp.enums.QueryTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于注解的查询条件构造
 * <p>
 * 当对枚举类使用该注解时候,前端需要传递枚举类的 name(不是key，也不是display) 才能映射, 否则就无法映射
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    /**
     * 数据库的字段名
     */
    String propName() default "";

    /**
     * 查询类型（默认等值查询）
     */
    QueryTypeEnum type() default QueryTypeEnum.EQUAL;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     * 填写Java的驼峰命名
     */
    String blurry() default "";
}
