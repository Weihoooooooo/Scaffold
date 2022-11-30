package com.jcweiho.scaffold.common.util.secure;

import java.util.Set;

/**
 * @author Weiho
 * @since 2022/11/19
 */
public interface SecureType {
    /**
     * 基于 Hutool 和 AES 封装的主键加密方法
     *
     * @param id 主键
     * @return 密文
     */
    String encrypt(Long id);

    /**
     * 基于 Hutool 和 AES 封装的主键解密方法
     *
     * @param encryptTxt 密文
     * @return 明文
     */
    Long decrypt(String encryptTxt);

    /**
     * 基于 Hutool 和 AES 封装的主键批量解密方法
     *
     * @param encryptSet 密文
     * @return 明文
     */
    Set<Long> decrypt(Set<String> encryptSet);
}
