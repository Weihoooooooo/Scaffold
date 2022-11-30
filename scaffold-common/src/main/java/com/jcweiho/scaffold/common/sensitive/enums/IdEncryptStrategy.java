package com.jcweiho.scaffold.common.sensitive.enums;

import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@RequiredArgsConstructor
public enum IdEncryptStrategy {
    DES(id -> IdSecureUtils.des().encrypt(id)),
    AES(id -> IdSecureUtils.aes().encrypt(id));

    private final Function<Long, String> desensitize;

    public Function<Long, String> desensitize() {
        return desensitize;
    }
}
