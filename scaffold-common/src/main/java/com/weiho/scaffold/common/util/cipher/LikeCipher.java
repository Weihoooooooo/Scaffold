package com.weiho.scaffold.common.util.cipher;

import com.weiho.scaffold.common.util.aes.AesUtils;
import com.weiho.scaffold.common.util.verify.VerifyUtils;
import lombok.experimental.UtilityClass;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@UtilityClass
public class LikeCipher {
    /**
     * 对姓名等敏感字段进行加密的算法(可以进行模糊查询)
     *
     * @param str 待加密串
     * @return 密文
     */
    public String likeEncrypt(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("$");
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
        if (str == null) {
            return null;
        }
        if (str.charAt(0) != '$' || str.charAt(str.length() - 1) != '$') {
            return null;
        }
        StringBuilder sb = new StringBuilder();
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
        if (str == null || !VerifyUtils.isMobileExact(str)) {
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
        if (str == null) {
            return null;
        }
        if (str.charAt(0) != '$' || str.charAt(str.length() - 1) != '$') {
            return null;
        }
        String[] ss = str.substring(1, str.length() - 2).split("\\$");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(AesUtils.decrypt(s));
        }
        return sb.toString();
    }
}
