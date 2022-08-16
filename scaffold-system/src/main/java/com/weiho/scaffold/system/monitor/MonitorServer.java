package com.weiho.scaffold.system.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.util.monitor.SystemMonitorUtils;
import com.weiho.scaffold.common.util.websocket.WebSocketErrorUtils;
import com.weiho.scaffold.system.monitor.websocket.MyEndpointConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket获取实时系统监控并输出到Web页面
 */
@Slf4j
@Component
@SuppressWarnings("all")
@ServerEndpoint(value = "/websocket/monitor", configurator = MyEndpointConfigure.class)
public class MonitorServer {
    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    private ScaffoldSystemProperties properties;
    /**
     * 连接集合
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>(3);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        //添加到集合中
        sessionMap.put(session.getId(), session);
        if (properties.getMonitorProperties().isEnabled()) {
            //获取系统监控信息
            asyncTaskExecutor.submit(() -> {
                log.info("MonitorServer系统检测任务开始");
                while (sessionMap.get(session.getId()) != null) {
                    try {
                        //获取系统监控信息 发送
                        send(session, new ObjectMapper().writeValueAsString(SystemMonitorUtils.getSysMonitor()));
                        //休眠一秒
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        //输出到日志文件中
                        log.error(WebSocketErrorUtils.errorInfoToString(e));
                    }
                }
                log.info("MonitorServer系统检测任务结束");
            });
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从集合中删除
        sessionMap.remove(session.getId());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //输出到日志文件中
        log.error(WebSocketErrorUtils.errorInfoToString(error));
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            //输出到日志文件中
            log.error(WebSocketErrorUtils.errorInfoToString(e));
        }
    }
}
