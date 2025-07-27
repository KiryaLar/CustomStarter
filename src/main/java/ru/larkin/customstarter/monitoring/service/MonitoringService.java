package ru.larkin.customstarter.monitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class MonitoringService {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger currentWorkload = new AtomicInteger();

    @PostConstruct
    public void setupMetrics() {
        Gauge.builder("android.workload", currentWorkload::get)
                .description("current workload")
                .register(meterRegistry);
    }

    public void publishTasksInQueueMetrics(int numberOfTasks) {
        currentWorkload.set(numberOfTasks);
    }

    public void publishCompletedTasksPerAuthorMetrics(String author) {
        Counter.builder("commands.completed")
                .tag("author", author)
                .description("Number of completed commands per author")
                .register(meterRegistry)
                .increment();
    }
}
