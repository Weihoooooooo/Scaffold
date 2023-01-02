package com.jcweiho.scaffold.tools.mail.consumer;

import com.jcweiho.scaffold.tools.mail.entity.convert.EmailConfigVOConvert;
import com.jcweiho.scaffold.tools.mail.entity.vo.EmailVO;
import com.jcweiho.scaffold.tools.mail.service.EmailConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 发送邮件消费者
 *
 * @author Weiho
 * @since 2022/8/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationCodeConsumer {
    private final EmailConfigService emailConfigService;
    private final EmailConfigVOConvert emailConfigVOConvert;

    @Async
    @RabbitListener(queues = "Scaffold.rabbit.queue.email", containerFactory = "singleListenerContainer")
    public void consumerEmailMessage(@Payload EmailVO o) {
        try {
            emailConfigService.send(o, emailConfigVOConvert.toPojo(emailConfigService.getConfig()));
        } catch (Exception e) {
            log.error("RabbitMQ -> MQ消息接收内容：{},发生异常：{}", o, e.fillInStackTrace());
        }
    }
}
