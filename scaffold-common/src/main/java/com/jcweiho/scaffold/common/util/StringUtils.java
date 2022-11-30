package com.jcweiho.scaffold.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * 字符串工具类
 *
 * @author <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
@UtilityClass
public class StringUtils extends StrUtil {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 分割符
     */
    private static final char SEPARATOR = '_';

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para 驼峰命名的字符串
     */
    public String toUnderline(String para) {
        StringBuilder sb = builder(para);
        int temp = 0;//定位
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                    sb.replace(i + temp, i + temp + 1, String.valueOf(Character.toLowerCase(para.charAt(i))));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名法工具(下划线转驼峰)
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public String toCamelCase(String s) {
        if (isBlankIfStr(s)) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = builder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名法工具(下划线转驼峰,首字母大写)
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public String toCapitalizeCamelCase(String s) {
        if (isBlankIfStr(s)) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (isBlankIfStr(str)) {
            return NULLSTR;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return NULLSTR;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    /**
     * 字符串反转(递归实现)
     *
     * @param originStr 传入字符串
     * @return 反转后
     */
    public String reverse(String originStr) {
        if (isBlankIfStr(originStr)) {
            return originStr;
        }
        return reverse(originStr.substring(1)) + originStr.charAt(0);
    }
}
