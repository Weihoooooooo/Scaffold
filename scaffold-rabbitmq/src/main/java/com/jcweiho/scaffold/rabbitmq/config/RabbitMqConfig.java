package com.jcweiho.scaffold.rabbitmq.config;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
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
    /**
     * 连接工厂实例
     */
    private final CachingConnectionFactory connectionFactory;
    /**
     * 消息监听器所在容器工厂的配置实例
     */
    private final SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;
    private final ScaffoldSystemProperties properties;

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
     * 创建队列
     *
     * @return Queue
     */
    @Bean(name = "logQueue")
    public Queue logQueue() {
        return new Queue(properties.getRabbitMqProperties().getQueueName(), true);
    }

    /**
     * 创建交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange logExchange() {
        return new TopicExchange(properties.getRabbitMqProperties().getExchangeName(), true, false);
    }

    /**
     * 创建绑定
     *
     * @return Binding
     */
    @Bean
    public Binding logBinding() {
        return BindingBuilder.bind(logQueue()).to(logExchange()).with(properties.getRabbitMqProperties().getRoutingKeyName());
    }
}
