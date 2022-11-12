package com.weiho.scaffold.common.util.verify;

import cn.hutool.core.lang.Validator;
import com.weiho.scaffold.common.util.string.StringUtils;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * 通过正则表达式判断字段是否正确
 * (手机号,固定电话,身份证号码,邮箱,url,车牌号,日期,ip地址,mac,人名)
 *
 * @author Weiho
 * @since 2022/8/24
 */
@UtilityClass
public class VerifyUtils extends Validator {
    /**
     * 正则:固话(带区号)7位或者8位
     */
    private static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";
    private static final Pattern PATTERN_REGEX_TEL = Pattern.compile(REGEX_TEL);

    /**
     * 正则：yyyy-MM-dd格式的日期校验,已考虑平闰年
     */
    private static final String REGEX_DATE = "^(?:(?!0000)\\d{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    private static final Pattern PATTERN_REGEX_DATE = Pattern.compile(REGEX_DATE);


    /**
     * 正则：mac地址
     */
    private static final String REGEX_MAC = "([A-Fa-f\\d]{2}-){5}[A-Fa-f\\d]{2}";
    private static final Pattern PATTERN_REGEX_MAC = Pattern.compile(REGEX_MAC);

    /**
     * 验证固定电话号码
     */
    public boolean isTel(String tel) {
        return isMatch(PATTERN_REGEX_TEL, tel);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验,已考虑平闰年
     */
    public boolean isDate(String date) {
        return isMatch(PATTERN_REGEX_DATE, date);
    }

    /**
     * 验证mac
     */
    public boolean isMac(String mac) {
        return isMatch(PATTERN_REGEX_MAC, mac);
    }

    /**
     * 正则判断
     *
     * @param pattern 正则表达式
     * @param str     输入字符串
     * @return boolean
     */
    public boolean isMatch(Pattern pattern, String str) {
        return StringUtils.isNoneEmpty(str) && pattern.matcher(str).matches();
    }

}
