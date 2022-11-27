package com.weiho.scaffold.common.util;

import cn.hutool.core.lang.Validator;
import lombok.experimental.UtilityClass;

/**
 * 对于数据库加密字段模糊搜索算法
 *
 * @author Weiho
 * @since 2022/10/21
 */
@UtilityClass
public class LikeCipherUtils {
    /**
     * 对姓名等敏感字段进行加密的算法(可以进行模糊查询)
     *
     * @param str 待加密串
     * @return 密文
     */
    public String likeEncrypt(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        StringBuilder sb = StringUtils.builder("$");
        for (int i = 0; i < str.length(); i++) {
            sb.append(AesUtils.encrypt(String.valueOf(str.charAt(i)))).append("$");
        }
        return sb.toString();
    }

    /**
     * 对姓名等敏感字段进行解密的算法
     *
     * @param str 密文
     * @return 明文
     */
    public String likeDecrypt(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (str.charAt(0) != '$' || str.charAt(str.length() - 1) != '$') {
            return null;
        }
        StringBuilder sb = StringUtils.builder();
        String[] ss = str.split("\\$");
        for (String s : ss) {
            sb.append(AesUtils.decrypt(s));
        }
        return sb.toString();
    }

    /**
     * 专对手机号进行模糊查询检索的加密算法
     *
     * @param str 待加密手机号
     * @return 密文
     */
    public String phoneLikeEncrypt(String str) {
        if (!Validator.isMobile(str)) {
            return null;
        }
        return "$" + AesUtils.encrypt(str.substring(0, 7)) + likeEncrypt(str.substring(7));
    }

    /**
     * 专对手机号进行模糊查询检索的解密算法
     *
     * @param str 密文
     * @return 明文
     */
    public String phoneLikeDecrypt(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (str.charAt(0) != '$' || str.charAt(str.length() - 1) != '$') {
            return null;
        }
        String[] ss = str.substring(1, str.length() - 2).split("\\$");
        StringBuilder sb = StringUtils.builder();
        for (String s : ss) {
            sb.append(AesUtils.decrypt(s));
        }
        return sb.toString();
    }
}
