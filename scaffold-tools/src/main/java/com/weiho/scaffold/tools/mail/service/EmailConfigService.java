package com.weiho.scaffold.tools.mail.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.tools.mail.entity.EmailConfig;
import com.weiho.scaffold.tools.mail.entity.vo.EmailConfigVO;
import com.weiho.scaffold.tools.mail.entity.vo.EmailVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-09-05
 */
public interface EmailConfigService extends CommonService<EmailConfig> {
    /**
     * 获取邮件配置
     *
     * @return 邮件配置对象
     */
    EmailConfig getConfig();

    /**
     * 修改邮箱配置
     *
     * @param newConfig 邮箱配置
     */
    boolean updateEmailConfig(EmailConfigVO newConfig);

    /**
     * 发送邮件
     *
     * @param emailVO 邮件发送的内容，收件人，主题
     * @param config  邮件配置
     */
    void send(EmailVO emailVO, EmailConfigVO config);
}
