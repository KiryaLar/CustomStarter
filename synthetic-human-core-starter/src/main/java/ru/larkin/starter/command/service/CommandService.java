package ru.larkin.starter.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.larkin.starter.command.exception.OverflowingQueueException;
import ru.larkin.starter.command.model.CommandDto;
import ru.larkin.starter.command.model.Priority;
import ru.larkin.starter.command.validator.CommandValidator;
import ru.larkin.starter.monitoring.service.MonitoringService;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

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

