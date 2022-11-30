package com.jcweiho.scaffold.common.thread;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池装配类
 *
 * @author PlayInJava <a href="https://juejin.im/entry/5abb8f6951882555677e9da2">参考链接</a>
 */
@Configuration
@Slf4j
public class TaskExecutePool implements AsyncConfigurer {
    private final ScaffoldSystemProperties properties;

    public TaskExecutePool(ScaffoldSystemProperties properties) {
        this.properties = properties;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(properties.getThreadPoolProperties().getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(properties.getThreadPoolProperties().getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(properties.getThreadPoolProperties().getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(properties.getThreadPoolProperties().getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix(properties.getThreadPoolProperties().getNamePrefix());
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
