package com.jcweiho.scaffold.mp.enums;

/**
 * 查询方式枚举
 *
 * @author Weiho
 */
public enum QueryTypeEnum {
    // 等值查询
    EQUAL
    // 大于等于
    , GREATER_THAN
    // 小于等于
    , LESS_THAN
    // 中模糊查询
    , INNER_LIKE
    // 左模糊查询
    , LEFT_LIKE
    // 右模糊查询
    , RIGHT_LIKE
    // 小于
    , LESS_THAN_NQ
    // 包含
    , IN
    // 不等于
    , NOT_EQUAL
    // between
    , BETWEEN
    // 不为空
    , NOT_NULL
    // 查询时间
    , UNIX_TIMESTAMP
}
