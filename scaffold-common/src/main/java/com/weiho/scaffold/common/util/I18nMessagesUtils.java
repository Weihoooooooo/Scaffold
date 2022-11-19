package com.weiho.scaffold.common.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化资源获取类
 *
 * @author Weiho
 * @since 2022/8/16
 */
@Component
public class I18nMessagesUtils {
    private static MessageSource messageSource;

    public I18nMessagesUtils(MessageSource messageSource) {
        I18nMessagesUtils.messageSource = messageSource;
    }

    /**
     * 获取国际化值
     *
     * @param messageKey key
     * @return 资源
     */
    public static String get(String messageKey) {
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化值
     *
     * @param messageKey key
     * @param objects    国际化值的参数列表
     * @return 资源
     */
    public static String get(String messageKey, Object... objects) {
        return messageSource.getMessage(messageKey, objects, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化值
     *
     * @param messageKey key
     * @param defaultMsg 国际化值空时的默认值
     * @param objects    国际化值的参数列表
     * @return 资源
     */
    public static String get(String messageKey, String defaultMsg, Object... objects) {
        return messageSource.getMessage(messageKey, objects, defaultMsg, LocaleContextHolder.getLocale());
    }
}
