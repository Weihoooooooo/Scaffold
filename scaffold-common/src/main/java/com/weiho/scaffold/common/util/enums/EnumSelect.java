package com.weiho.scaffold.common.util.enums;

/**
 * @author Weiho
 * @since 2022/11/3
 */
public interface EnumSelect {
    /**
     * 获取业务枚举类的Key
     *
     * @return int
     */
    Integer getKey();

    /**
     * 获取业务枚举类的描述
     *
     * @return String
     */
    String getDisplay();
}
