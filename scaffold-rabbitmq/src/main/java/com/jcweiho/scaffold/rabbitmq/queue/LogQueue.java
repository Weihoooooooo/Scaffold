package com.jcweiho.scaffold.rabbitmq.queue;

import org.springframework.stereotype.Component;

/**
 * @author Weiho
 * @since 2023/1/1
 */
@Component
public class LogQueue implements Queue {
    @Override
    public String getQueueName() {
        return "Scaffold.rabbit.queue.log";
    }

    @Override
    public String getExchangeName() {
        return "Scaffold.rabbit.exchange.log";
    }

    @Override
    public String getRoutingKeyName() {
        return "Scaffold.rabbit.routing.key.log";
    }
}
