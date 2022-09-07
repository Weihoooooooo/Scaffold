package com.weiho.scaffold.tools.rabbitmq.core;

import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.tools.mail.entity.vo.EmailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 消息生产者
 *
 * @author Weiho
 * @date 2022/8/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ScaffoldSystemProperties properties;

    /**
     * 发送MQ发送邮件任务
     *
     * @param o Email实体
     */
    @Async
    public void sendEmailMqMessage(EmailVO o) {
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(properties.getRabbitMqProperties().getExchangeName());
            rabbitTemplate.setRoutingKey(properties.getRabbitMqProperties().getRoutingKeyName());
            rabbitTemplate.convertAndSend(o, message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, o.getClass());
                return message;
            });
        } catch (Exception e) {
            log.error("RabbitMQ -> MQ消息发送内容：{},发生异常：{}", o, e.fillInStackTrace());
        }
    }
}
