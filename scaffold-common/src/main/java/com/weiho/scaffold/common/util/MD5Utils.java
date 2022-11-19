package com.weiho.scaffold.common.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加解密工具类
 *
 * @author <a href="https://www.cnblogs.com/huanzi-qch/">参考链接</a>
 */
@UtilityClass
public class MD5Utils {
    /**
     * 创建MD5加密字符串
     *
     * @param message 要加密的字符串
     * @return String
     */
    public String getMd5(String message) {
        String md5 = "";
        try {
            //创建一个md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            //获得MD5字节数组,16*8=128位
            byte[] md5Byte = md.digest(messageByte);
            //转换为16进制字符串
            StringBuilder hexStr = new StringBuilder(md5Byte.length);
            int num;
            for (byte aByte : md5Byte) {
                num = aByte;
                if (num < 0) {
                    num += 256;
                }
                if (num < 16) {
                    hexStr.append("0");
                }
                hexStr.append(Integer.toHexString(num));
            }
            md5 = hexStr.toString().toUpperCase();
        } catch (Exception ignored) {
        }
        return md5;
    }

    /**
     * 验证方法
     *
     * @param text 明文
     * @param md5  密文
     * @return 对比结果
     */
    private boolean verify(String text, String md5) {
        return md5.equals(getMd5(text));
    }
}
