package com.weiho.scaffold.common.sensitive.enums;

import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@RequiredArgsConstructor
public enum IdDecryptStrategy {
    DES(id -> IdSecureUtils.des().decrypt(id)),
    AES(id -> IdSecureUtils.aes().decrypt(id));

    private final Function<String, Long> desensitize;

    public Function<String, Long> desensitize() {
        return desensitize;
    }
}
