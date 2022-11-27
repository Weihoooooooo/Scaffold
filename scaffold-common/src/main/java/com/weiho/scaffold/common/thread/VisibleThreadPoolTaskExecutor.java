package com.weiho.scaffold.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@SuppressWarnings("all")
public class VisibleThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private void showThreadPoolInfo() {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        log.info(
                "\n线程池状况 -> 线程前缀为[{}]\n当前线程总数为[{}]\n完成的线程总数为[{}]\n" +
                        "当前正在处理任务的线程数为[{}]\n当前在队列中等待的线程数为[{}]",
                this.getThreadNamePrefix(),
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size()
        );
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo();
        return super.submit(task);
    }

}
