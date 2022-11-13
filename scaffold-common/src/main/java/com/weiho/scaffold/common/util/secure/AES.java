package com.weiho.scaffold.common.util.secure;

import cn.hutool.core.convert.Convert;
import com.weiho.scaffold.common.util.aes.AesUtils;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@NoArgsConstructor
public class AES {
    /**
     * 基于 Hutool 和 AES 封装的主键加密方法
     *
     * @param id 主键
     * @return 密文
     */
    public String encrypt(Long id) {
        return AesUtils.encrypt(Convert.toStr(id));
    }

    /**
     * 基于 Hutool 和 AES 封装的主键解密方法
     *
     * @param encryptTxt 密文
     * @return 明文
     */
    public Long decrypt(String encryptTxt) {
        return Convert.toLong(AesUtils.decrypt(encryptTxt));
    }

    /**
     * 基于 Hutool 和 AES 封装的主键批量解密方法
     *
     * @param encryptSet 密文
     * @return 明文
     */
    public Set<Long> decrypt(Set<String> encryptSet) {
        return encryptSet.stream().map(this::decrypt).collect(Collectors.toSet());
    }
}
