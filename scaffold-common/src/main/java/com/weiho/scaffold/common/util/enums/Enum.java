package com.weiho.scaffold.common.util.enums;

/**
 * @author Weiho
 * @since 2022/11/3
 */
public interface Enum {
    /**
     * 获取业务枚举类的Key
     * 存入数据库的值
     *
     * @return int
     */
    Integer getKey();

    /**
     * 获取业务枚举类的描述
     * 原来要显示的值
     *
     * @return String
     */
    String getDisplay();
}
