package ru.larkin.customstarter;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@ConfigurationPropertiesScan
public class StarterConfig {

    @Bean
    public ThreadPoolExecutor executor() {
        return new ThreadPoolExecutor(
                2, 4, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100)
        );
    }
}
