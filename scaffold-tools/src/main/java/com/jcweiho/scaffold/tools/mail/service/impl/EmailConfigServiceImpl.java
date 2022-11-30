package com.jcweiho.scaffold.tools.mail.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.DesUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.tools.mail.entity.EmailConfig;
import com.jcweiho.scaffold.tools.mail.entity.convert.EmailConfigVOConvert;
import com.jcweiho.scaffold.tools.mail.entity.vo.EmailConfigVO;
import com.jcweiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.jcweiho.scaffold.tools.mail.mapper.EmailConfigMapper;
import com.jcweiho.scaffold.tools.mail.service.EmailConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-09-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class EmailConfigServiceImpl extends CommonServiceImpl<EmailConfigMapper, EmailConfig> implements EmailConfigService {
    private final EmailConfigVOConvert emailConfigVOConvert;

    @Override
    @Cacheable(value = "Scaffold:Mail", key = "'config'")
    public EmailConfig getConfig() {
        try {
            EmailConfig config = this.list().get(0);
            config.setPass(DesUtils.desDecrypt(config.getPass()));
            return config;
        } catch (Exception e) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.get.error"));
        }
    }

    @Override
    @CachePut(value = "Scaffold:Mail", key = "'config'")
    public boolean updateEmailConfig(EmailConfigVO newConfig) {
        EmailConfig oldConfig = getConfig();
        try {
            if (!newConfig.getPass().equals(oldConfig.getPass())) {
                newConfig.setPass(DesUtils.desEncrypt(newConfig.getPass()));
            }
        } catch (Exception e) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.update.error"));
        }
        return this.save(emailConfigVOConvert.toEntity(newConfig));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVO emailVO, EmailConfigVO config) {
        if (ObjectUtil.isNull(config)) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.error.tip"));
        }
        // 构建邮件体
        MailAccount account = new MailAccount();
        account.setHost(config.getHost());
        account.setPort(Integer.parseInt(config.getPort()));
        account.setAuth(true);
        account.setPass(config.getPass());
        account.setFrom(config.getUsername() + "<" + config.getFromUser() + ">");
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        String content = emailVO.getContent();
        // 发送
        try {
            int size = emailVO.getTos().size();
            Mail.create(account)
                    .setTos(emailVO.getTos().toArray(new String[size]))
                    .setTitle(emailVO.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    // 关闭Session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.send.error.tip"));
        }
    }
}
