package com.weiho.scaffold.common.util.secure;

import cn.hutool.core.convert.Convert;
import com.weiho.scaffold.common.util.des.DesUtils;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@NoArgsConstructor
public class DES {

    /**
     * 基于 Hutool 和 DES 封装的主键加密方法
     *
     * @param id 主键
     * @return 密文
     */
    public String encrypt(Long id) {
        try {
            return DesUtils.desEncrypt(Convert.toStr(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 基于 Hutool 和 DES 封装的主键解密方法
     *
     * @param encryptTxt 密文
     * @return 明文
     */
    public Long decrypt(String encryptTxt) {
        try {
            return Convert.toLong(DesUtils.desDecrypt(encryptTxt));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 基于 Hutool 和 DES 封装的主键批量解密方法
     *
     * @param encryptSet 密文
     * @return 明文
     */
    public Set<Long> decrypt(Set<String> encryptSet) {
        return encryptSet.stream().map(this::decrypt).collect(Collectors.toSet());
    }
}
