package com.weiho.scaffold.tools.mail.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.util.enums.EnumSelectVO;
import com.weiho.scaffold.common.util.enums.EnumUtils;
import com.weiho.scaffold.redis.util.RedisUtils;
import com.weiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.weiho.scaffold.tools.mail.entity.vo.VerificationCodeVO;
import com.weiho.scaffold.tools.mail.enums.EmailTypeEnum;
import com.weiho.scaffold.tools.mail.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Weiho
 * @since 2022/9/6
 */
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final RedisUtils redisUtils;
    private final ScaffoldSystemProperties properties;

    @Override
    public Map<String, Object> generatorEmailInfo(VerificationCodeVO codeVO) {
        EmailVO emailVO;
        String content;
        // 加载邮箱模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        // 生成验证码
        String code = RandomUtil.randomNumbers(properties.getEmailProperties().getCodeLength());
        // 生成验证码的唯一UUID
        String key = properties.getEmailProperties().getCodeKey() + IdUtil.simpleUUID();
        // 将验证码放入模板中
        content = template.render(Dict.create().set("code", code));
        // 装入VO中
        emailVO = new EmailVO(Collections.singletonList(codeVO.getAccount() + codeVO.getSuffix().getDisplay()), "Scaffold-管理系统", content);
        // 将验证码保存到Redis中
        redisUtils.set(key, code, properties.getEmailProperties().getExpiration(), TimeUnit.MINUTES);
        return new HashMap<String, Object>(2) {{
            put("uuid", key);
            put("emailVO", emailVO);
        }};
    }

    @Override
    public List<EnumSelectVO> getSelectList() {
        return EnumUtils.getEnumSelect(EmailTypeEnum.class);
    }
}
