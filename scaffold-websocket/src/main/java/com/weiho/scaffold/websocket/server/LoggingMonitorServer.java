package com.weiho.scaffold.websocket.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.util.DateUtils;
import com.weiho.scaffold.websocket.config.MyEndpointConfigure;
import com.weiho.scaffold.websocket.entity.WebSocketResult;
import com.weiho.scaffold.websocket.util.WebSocketErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实时控制台传输
 *
 * @author huanzi-qch <a href="https://www.cnblogs.com/huanzi-qch/">参考链接</a>
 * @since 2022/8/21
 */
@Slf4j
@Component
@SuppressWarnings("all")
@ServerEndpoint(value = "/websocket/logging", configurator = MyEndpointConfigure.class)
public class LoggingMonitorServer {
    @Value("${spring.application.name}")
    private String applicationName;

    private final AsyncTaskExecutor asyncTaskExecutor;
    private final ScaffoldSystemProperties properties;

    public LoggingMonitorServer(AsyncTaskExecutor asyncTaskExecutor, ScaffoldSystemProperties properties) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.properties = properties;
    }

    /**
     * 连接集合
     */
    private final static Map<String, Session> sessionMap = new ConcurrentHashMap<>(3);
    private final static Map<String, Integer> lengthMap = new ConcurrentHashMap<>(3);

    /**
     * 匹配日期开头加换行，2019-08-12 14:15:04
     */
    private final Pattern datePattern = Pattern.compile("[\\d+][\\d+][\\d+][\\d+]-[\\d+][\\d+]-[\\d+][\\d+] [\\d+][\\d+]:[\\d+][\\d+]:[\\d+][\\d+]");

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        //添加到集合中
        sessionMap.put(session.getId(), session);
        lengthMap.put(session.getId(), 1);//默认从第一行开始

        if (properties.getMonitorProperties().isLoggingMonitorEnabled()) {
            asyncTaskExecutor.submit(() -> {
                log.info("Monitor -> [LoggingMonitorServer 控制台日志监测任务开始]");
                boolean first = true;
                BufferedReader reader = null;
                FileReader fileReader = null;
                while (sessionMap.get(session.getId()) != null) {
                    try {
                        //日志文件，获取最新的
                        fileReader = new FileReader(System.getProperty("user.dir") + "/logs/" + DateUtils.getNowDateFormat(DateUtils.FormatEnum.YYYYMMDD) + "/" + applicationName + ".log");

                        //字符流
                        reader = new BufferedReader(fileReader);
                        Object[] lines = reader.lines().toArray();

                        //只取上次之后产生的日志
                        Object[] copyOfRange = Arrays.copyOfRange(lines, lengthMap.get(session.getId()), lines.length);

                        //对日志进行着色，更加美观  PS：注意，这里要根据日志生成规则来操作
                        for (int i = 0; i < copyOfRange.length; i++) {
                            String line = String.valueOf(copyOfRange[i]);
                            //先转义
                            line = line.replaceAll("&", "&amp;")
                                    .replaceAll("<", "&lt;")
                                    .replaceAll(">", "&gt;")
                                    .replaceAll("\"", "&quot;");

                            //处理等级
                            line = line.replace("DEBUG", "<span style='color: blue;'><b>DEBUG</b></span>");
                            line = line.replace("INFO", "<span style='color: green;'><b>INFO</b></span>");
                            line = line.replace("WARN", "<span style='color: orange;'><b>WARN</b></span>");
                            line = line.replace("ERROR", "<span style='color: red;'><b>ERROR</b></span>");

                            //处理类名(核心算法)
                            String[] split = line.split("]");
                            StringBuilder sb = new StringBuilder();

                            if (split.length == 2) {
                                // length = 2,无]结尾
                                String[] split1 = split[1].split("-");
                                if (split1.length >= 2) {
                                    sb.append(split[0]).append("]").append("<span style='color: #298a8a;'>").append(split1[0]).append("</span>");
                                    for (int j = 1; j <= split1.length - 1; j++) {
                                        sb.append("-").append(split1[j]);
                                    }
                                }
                                if (line.endsWith("]")) {
                                    // length = 2,]结尾
                                    sb.append("]");
                                }
                                line = sb.toString();
                            } else if (split.length > 2) {
                                StringBuilder flag = new StringBuilder();
                                // length > 2
                                sb.append(split[0]).append("]").append("<span style='color: #298a8a;'>");
                                for (int j = 1; j <= split.length - 1; j++) {
                                    if (!split[j].contains("-")) {
                                        sb.append(split[j]).append("]");
                                    } else {
                                        flag.append(split[j]);
                                    }
                                }
                                sb.append("</span>");
                                sb.append(flag);
                                line = sb.toString();
                            }

                            // 匹配日期开头加换行，2019-08-12 14:15:04
                            Matcher m = datePattern.matcher(line);
                            if (m.find()) {
                                //找到下标
                                int start = m.start();
                                //插入
                                StringBuilder sbbr = new StringBuilder(line);
                                sbbr.insert(start, "<br/><br/><span style='color:red;'><b>");
                                sbbr.insert(start + 62, "</b></span>");
                                line = sbbr.toString();
                            }

                            copyOfRange[i] = line;
                        }
                        //存储最新一行开始
                        lengthMap.replace(session.getId(), lines.length);

                        //第一次如果太大，截取最新的100行就够了，避免传输的数据太大
                        if (first && copyOfRange.length > 100) {
                            copyOfRange = Arrays.copyOfRange(copyOfRange, copyOfRange.length - 100, copyOfRange.length);
                            first = false;
                        }

                        String result = join(copyOfRange, "<br/>");

                        //发送
                        send(session, new ObjectMapper().writeValueAsString(new WebSocketResult<String>(10005, result)));

                        //休眠一秒
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        //输出到日志文件中
                        log.error(WebSocketErrorUtils.errorInfoToString(e));
                    }
                }
                try {
                    reader.close();
                    fileReader.close();
                } catch (IOException e) {
                    //输出到日志文件中
                    log.error(WebSocketErrorUtils.errorInfoToString(e));
                }
                log.info("Monitor -> [LoggingMonitorServer 控制台日志监测任务结束]");
            });
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        lengthMap.remove(session.getId());
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

    /**
     * 插入分隔符，处理信息
     *
     * @param target
     * @param separator
     * @return
     */
    public static String join(Object[] target, String separator) {
        if (target == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            if (target.length > 0) {
                sb.append(target[0]);
                for (int i = 1; i < target.length; ++i) {
                    sb.append(separator);
                    sb.append(target[i]);
                }
            }
            return sb.toString();
        }
    }
}
