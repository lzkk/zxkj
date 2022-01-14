package com.zxkj.order.config;

import com.zxkj.common.context.CustomerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class ExecutorConfig {

    /**
     * SPRING的ThreadPoolTaskExecutor自定义Executor
     *
     * @return
     */
    @Bean
    public Executor springExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("orderExecutor-");
        executor.initialize();
        return CustomerContext.executor(executor);
    }


    /**
     * JDK的ThreadPoolExecutor自定义Executor
     *
     * @return
     */
//    @Bean
    public Executor jdkCustomExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(60));
        executor.setThreadFactory(new CustomizableThreadFactory("JDKCustomExecutor-"));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return CustomerContext.executor(executor);
    }

}
