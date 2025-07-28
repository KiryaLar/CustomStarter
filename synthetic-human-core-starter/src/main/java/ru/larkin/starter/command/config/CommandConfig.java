package ru.larkin.starter.command.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CommandConfig {

    @Bean
    public ThreadPoolExecutor executor(@Value("${starter.threadPool.coreSize:1}") int coreSize,
                                       @Value("${starter.threadPool.maxSize:4}") int maxSize,
                                       @Value("${starter.threadPool.queueCapacity:10}") int queueCapacity,
                                       @Value("${starter.threadPool.keepAliveTime:10}") long keepAliveTime) {
        return new ThreadPoolExecutor(
                coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity)
        );
    }
}
