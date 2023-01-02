package com.jcweiho.scaffold.logging.consumer;

import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Weiho
 * @since 2022/12/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingConsumer {
    private final LogService logService;

    @Async
    @RabbitListener(queues = "Scaffold.rabbit.queue.log", containerFactory = "singleListenerContainer")
    public void consumerLogMessage(@Payload Log logging) {
        try {
            logService.saveLogInfo(logging);
        } catch (Exception e) {
            log.error("RabbitMQ -> MQ消息接收内容：{},发生异常：{}", logging, e.fillInStackTrace());
        }
    }
}
