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
import ru.larkin.customstarter.monitoring.service.MonitoringService;

import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommandService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final CommandValidator commandValidator;
    private final MonitoringService monitoringService;

    public void processCommand(CommandDto command) {
        commandValidator.validate(command);

        Runnable task = () -> {
            try {
                monitoringService.publishTasksInQueueMetrics(threadPoolExecutor.getQueue().size());
                log.info("Исполнение {} команды \"{}\", автор - {}, время - {}", command.getPriority(), command.getDescription(), command.getAuthor(), command.getTime());
                monitoringService.publishCompletedTasksPerAuthorMetrics(command.getAuthor());
            } finally {
                monitoringService.publishTasksInQueueMetrics(threadPoolExecutor.getQueue().size());
            }
        };

        if (command.getPriority().equals(Priority.COMMON)) {
            try {
                threadPoolExecutor.execute(task);
            } catch (RejectedExecutionException e) {
                throw new OverflowingQueueException("Невозможно выполнить команду " + command.getDescription() + ", очередь переполнена");
            }
        } else {
            task.run();
        }
    }
}

