package ru.larkin.customstarter.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.larkin.customstarter.command.exception.OverflowingQueueException;
import ru.larkin.customstarter.command.model.CommandDto;
import ru.larkin.customstarter.command.model.Priority;
import ru.larkin.customstarter.command.util.CommandValidator;

import java.util.concurrent.*;

@Slf4j
@Service
public class CommandService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final CommandValidator commandValidator;

    public CommandService(CommandValidator commandValidator) {
        this.commandValidator = commandValidator;
        threadPoolExecutor = new ThreadPoolExecutor(2, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());
    }

    public void processCommand(CommandDto command) {
        commandValidator.validate(command);
        if (command.getPriority().equals(Priority.COMMON)) {
            processCommonCommand(command);
        } else {
            log.info("Исполнение критической команды \"{}\", автор - {}, время - {}", command.getDescription(), command.getAuthor(), command.getTime());
        }
    }

    public void processCommonCommand(CommandDto command) {
        try {
            threadPoolExecutor.execute(() -> log.info("Исполнение обычной команды \"{}\", автор - {}, время - {}", command.getDescription(), command.getAuthor(), command.getTime()));
        } catch (RejectedExecutionException e) {
            throw new OverflowingQueueException("Невозможно выполнить команду "+command.getDescription()+", очередь переполнена");
        }
    }
}

