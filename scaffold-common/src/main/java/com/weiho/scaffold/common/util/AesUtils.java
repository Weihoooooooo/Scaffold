package com.weiho.scaffold.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加解密工具类
 *
 * @author Weiho
 * @since 2022/8/23
 */
@UtilityClass
public class AesUtils {
    // 密匙
    private static final String KEY = "LwHaiLjCbb!cj@Jc";
    // 偏移量
    private static final String OFFSET = "5e8y6w45ju8w9jq8";
    // 编码
    private static final String ENCODING = "UTF-8";
    //算法
    private static final String ALGORITHM = "AES";
    // 默认的加密算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static void main(String[] args) {
//        String str = "Hello World!";
//        System.out.println("加密前:" + str);
//        System.out.println("加密后:" + encrypt(str));
//        System.out.println("解密后:" + decrypt(encrypt(str)));
    }

    /**
     * AES加密
     *
     * @param data 需要加密的内容
     * @return 加密后的字符串
     */
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
            return Base64.encodeBase64String(encrypted);//此处使用BASE64做转码。
        } catch (Exception e) {
            ThrowableUtils.getStackTrace(e);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param data 需要解密的内容
     * @return 解密后字符串
     */
    public String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] buffer = Base64.decodeBase64(data);
            byte[] encrypted = cipher.doFinal(buffer);
            return new String(encrypted, ENCODING);//此处使用BASE64做转码。
        } catch (Exception e) {
            ThrowableUtils.getStackTrace(e);
        }
        return null;
    }

}
