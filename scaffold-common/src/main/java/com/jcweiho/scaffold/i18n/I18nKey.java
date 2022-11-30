package com.jcweiho.scaffold.i18n;

/**
 * 枚举类国际化接口
 * <p>
 * 当采用国际化枚举类时候需要重写Display的getter方法,并且加入JsonValue注解
 *
 * @author Weiho
 * @since 2022/11/19
 */
public interface I18nKey {
    /**
     * 获取I18n的Key
     */
    String getI18nKey();
}
