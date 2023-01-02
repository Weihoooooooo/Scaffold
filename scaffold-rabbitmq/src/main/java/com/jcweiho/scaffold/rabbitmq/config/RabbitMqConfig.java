package com.jcweiho.scaffold.rabbitmq.config;

import com.jcweiho.scaffold.rabbitmq.queue.EmailQueue;
import com.jcweiho.scaffold.rabbitmq.queue.LogQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ 配置类
 *
 * @author Weiho
 * @since 2022/8/28
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final LogQueue logQueue;
    private final EmailQueue emailQueue;
    /**
     * 连接工厂实例
     */
    private final CachingConnectionFactory connectionFactory;
    /**
     * 消息监听器所在容器工厂的配置实例
     */
    private final SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     *
     * @return /
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 初始化消息的数量
        factory.setConcurrentConsumers(1);
        // 最大消费数量
        factory.setMaxConcurrentConsumers(1);
        // 每个实例获取消息的数量
        factory.setPrefetchCount(1);
        return factory;
    }

    /**
     * 多个消费者
     *
     * @return /
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        // 监听消息所在的工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //set 工厂所在的容器
        factoryConfigurer.configure(factory, connectionFactory);
        // 消息传输的格式
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(15);
        factory.setPrefetchCount(10);
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        return factory;
    }

    /**
     * RabbitMQ发送消息的操作组件实例
     *
     * @return /
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, b, s) -> log.info("RabbitMQ -> [消息发送成功:correlationData({}),ack({}),cause({})]", correlationData, b, s));
        return rabbitTemplate;
    }

    /**
     * 日志队列
     */
    @Bean
    public Queue logRabbitQueue() {
        return new Queue(logQueue.getQueueName(), true, false, false, null);
    }

    /**
     * 日志交换机
     */
    @Bean
    public TopicExchange logRabbitExchange() {
        return new TopicExchange(logQueue.getExchangeName(), true, false, null);
    }

    /**
     * 日志路由Key
     */
    @Bean
    public Binding logRabbitBinding() {
        return BindingBuilder.bind(logRabbitQueue()).to(logRabbitExchange()).with(logQueue.getRoutingKeyName());
    }

    /**
     * 邮件队列
     */
    @Bean
    public Queue emailRabbitQueue() {
        return new Queue(emailQueue.getQueueName(), true, false, false, null);
    }

    /**
     * 邮件交换机
     */
    @Bean
    public TopicExchange emailRabbitExchange() {
        return new TopicExchange(emailQueue.getExchangeName(), true, false, null);
    }

    /**
     * 邮件路由Key
     */
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailRabbitQueue()).to(emailRabbitExchange()).with(emailQueue.getRoutingKeyName());
    }
}
