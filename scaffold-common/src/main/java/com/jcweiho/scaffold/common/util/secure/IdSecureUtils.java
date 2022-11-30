package com.jcweiho.scaffold.common.util.secure;

import cn.hutool.core.util.ObjectUtil;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import lombok.experimental.UtilityClass;

/**
 * 主键加解密工具类
 *
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

    /**
     * 验证主键不能为空
     */
    public void verifyIdNotNull(Long id) {
        if (ObjectUtil.isNull(id)) {
            throw new BadRequestException(I18nMessagesUtils.get("id.not.null.tip"));
        }
    }

    /**
     * 验证主键一定为空
     */
    public void verifyIdNull(Long id) {
        if (ObjectUtil.isNotNull(id)) {
            throw new BadRequestException(I18nMessagesUtils.get("id.null.tip"));
        }
    }
}
