package com.weiho.scaffold.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 根据请求头获取相应的资源
     *
     * @param request 请求参数
     * @param i18n    资源实体
     * @return 对应的资源实体
     */
    public static String getNameForI18n(HttpServletRequest request, I18n i18n) {
        String language = request.getHeader("Accept-Language") == null ? "zh-CN" : request.getHeader("Accept-Language");
        if ("zh-CN".equals(language)) {
            return i18n.getNameZhCn();
        } else if ("zh-HK".equals(language)) {
            return i18n.getNameZhHk();
        } else if ("zh-TW".equals(language)) {
            return i18n.getNameZhTw();
        } else if ("en-US".equals(language)) {
            return i18n.getNameEnUs();
        }
        return i18n.getName();
    }
}
