package com.jcweiho.scaffold.rabbitmq.queue;

import org.springframework.stereotype.Component;

/**
 * @author Weiho
 * @since 2023/1/2
 */
@Component
public class EmailQueue implements Queue {
    @Override
    public String getQueueName() {
        return "Scaffold.rabbit.queue.email";
    }

    @Override
    public String getExchangeName() {
        return "Scaffold.rabbit.exchange.email";
    }

    @Override
    public String getRoutingKeyName() {
        return "Scaffold.rabbit.routing.key.email";
    }
}
