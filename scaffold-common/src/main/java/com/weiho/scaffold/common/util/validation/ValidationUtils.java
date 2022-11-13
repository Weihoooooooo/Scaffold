package com.weiho.scaffold.common.util.validation;

import cn.hutool.core.util.ObjectUtil;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import lombok.experimental.UtilityClass;

/**
 * @author Weiho
 * @since 2022/8/30
 */
@UtilityClass
public class ValidationUtils {
    /**
     * 验证空
     */
    public void isNull(Object obj, String entity, String parameter, Object value) {
        if (ObjectUtil.isNull(obj)) {
            String msg = entity + " 不存在: " + parameter + " is " + value;
            throw new BadRequestException(msg);
        }
    }

    public static void main(String[] args) {
        String str = IdSecureUtils.des().encrypt(1L);
        System.err.println(str);
        System.err.println(IdSecureUtils.des().decrypt(str));
    }
}
