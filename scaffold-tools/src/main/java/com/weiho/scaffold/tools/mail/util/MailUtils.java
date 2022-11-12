package com.weiho.scaffold.tools.mail.util;

import cn.hutool.extra.mail.MailUtil;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.tools.mail.enums.EmailTypeEnum;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Weiho
 * @since 2022/10/24
 */
@UtilityClass
public class MailUtils extends MailUtil {
    /**
     * 检查邮箱是否符合系统的枚举类
     *
     * @param email 检查的邮箱
     */
    public void checkEmail(String email) {
        String suffix = email.substring(email.indexOf('@'));
        EmailTypeEnum[] emailTypeEnums = EmailTypeEnum.values();
        Set<String> set = Arrays.stream(emailTypeEnums).map(EmailTypeEnum::getDisplay).collect(Collectors.toSet());
        if (!set.contains(suffix)) {
            throw new BadRequestException(I18nMessagesUtils.get("email.suffix.error"));
        }
    }
}
