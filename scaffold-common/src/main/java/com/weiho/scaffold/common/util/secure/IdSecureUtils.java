package com.weiho.scaffold.common.util.secure;

import lombok.experimental.UtilityClass;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@UtilityClass
public class IdSecureUtils {

    /**
     * 获取 AES 实例
     *
     * @return AES实例
     */
    public AES aes() {
        return new AES();
    }

    /**
     * 获取 DES 实例
     *
     * @return DES实例
     */
    public DES des() {
        return new DES();
    }
}
