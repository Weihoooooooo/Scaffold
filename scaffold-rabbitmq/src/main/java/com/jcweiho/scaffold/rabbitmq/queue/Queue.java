package com.jcweiho.scaffold.rabbitmq.queue;

/**
 * MQ队列接口
 *
 * @author Weiho
 * @since 2023/1/1
 */
public interface Queue {
    /**
     * 获取队列名称
     */
    String getQueueName();

    /**
     * 获取交换机名称
     */
    String getExchangeName();

    /**
     * 获取路由Key名称
     */
    String getRoutingKeyName();
}
