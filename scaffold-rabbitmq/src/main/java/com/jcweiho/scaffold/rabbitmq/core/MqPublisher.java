package com.jcweiho.scaffold.rabbitmq.core;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.rabbitmq.queue.Queue;
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
 * @since 2022/8/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ScaffoldSystemProperties properties;

    /**
     * 发送MQ任务
     *
     * @param o 实体
     */
    @Async
    public <T> void sendMqMessage(T o, Queue queue) {
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(queue.getExchangeName());
            rabbitTemplate.setRoutingKey(queue.getRoutingKeyName());
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
