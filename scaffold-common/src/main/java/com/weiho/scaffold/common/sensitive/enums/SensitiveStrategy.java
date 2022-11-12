package com.weiho.scaffold.common.sensitive.enums;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.function.Function;

/**
 * 脱敏策略
 *
 * @author Weiho
 * @since 2022/8/24
 */
public enum SensitiveStrategy {
    /**
     * 用户名
     */
    USERNAME(DesensitizedUtil::chineseName),

    /**
     * 身份证号
     */
    ID_CARD(idCard -> DesensitizedUtil.desensitized(idCard, DesensitizedUtil.DesensitizedType.ID_CARD)),

    /**
     * 电话号码
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 邮箱
     */
    EMAIL(DesensitizedUtil::email),

    /**
     * 地址
     */
    ADDRESS(address -> DesensitizedUtil.desensitized(address, DesensitizedUtil.DesensitizedType.ADDRESS)),

    /**
     * 车牌号
     */
    CARD(DesensitizedUtil::carLicense);

    private final Function<String, String> desensitize;

    SensitiveStrategy(Function<String, String> desensitize) {
        this.desensitize = desensitize;
    }

    public Function<String, String> desensitize() {
        return desensitize;
    }
}
