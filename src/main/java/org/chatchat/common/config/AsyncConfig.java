package org.chatchat.common.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // 비동기 메서드에서 발생한 얘외를 처리하는 핸들러 설정
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 기본 스레드 풀 크기 설정 (유지되는 스레드 수)
        executor.setCorePoolSize(5);
        // 최대 스레드 풀 크기 설정 (최대 생성되는 스레드 수)
        executor.setMaxPoolSize(10);
        // 작업 큐 용량 설정(대기할 수 있는 작업의 최대 수)
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("NotificationAsync-");
        // 설정 적용 및 초기화
        executor.initialize();
        return executor;
    }
}
