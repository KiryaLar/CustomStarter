package ru.larkin.customstarter.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.larkin.customstarter.command.exception.OverflowingQueueException;
import ru.larkin.customstarter.command.model.CommandDto;
import ru.larkin.customstarter.command.model.Priority;
import ru.larkin.customstarter.command.validator.CommandValidator;
import ru.larkin.customstarter.monitoring.config.MonitoringService;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommandService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final CommandValidator commandValidator;
    private final MonitoringService monitoringService;

    @DurationUnit(ChronoUnit.SECONDS)
    @Value("${fixed.delay:3}")
    private final Duration fixedDelay;

    public void processCommand(CommandDto command) {
        commandValidator.validate(command);
        monitoringService.publishMetrics(command.getAuthor());

        if (command.getPriority().equals(Priority.COMMON)) {
            processCommonCommand(command);
        } else {
            log.info("Исполнение критической команды \"{}\", автор - {}, время - {}", command.getDescription(), command.getAuthor(), command.getTime());
        }
    }

    @Scheduled
    public void publishQueueSizeMetric() {
        monitoringService.publishTasksNumberMetrics(threadPoolExecutor.getQueue().size());
    }

    public void processCommonCommand(CommandDto command) {
        try {
            threadPoolExecutor.execute(() -> log.info("Исполнение обычной команды \"{}\", автор - {}, время - {}", command.getDescription(), command.getAuthor(), command.getTime()));
        } catch (RejectedExecutionException e) {
            throw new OverflowingQueueException("Невозможно выполнить команду "+command.getDescription()+", очередь переполнена");
        }
    }
}

