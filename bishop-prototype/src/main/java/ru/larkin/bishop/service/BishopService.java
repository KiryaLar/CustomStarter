package ru.larkin.bishop.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larkin.starter.audit.annotation.WeylandWatchingYou;
import ru.larkin.starter.command.model.CommandDto;
import ru.larkin.starter.command.service.CommandService;

@Service
@RequiredArgsConstructor
public class BishopService {

    private final CommandService commandService;

    @WeylandWatchingYou
    public void processCommand(@Valid CommandDto commandDto) {
        commandService.processCommand(commandDto);
    }
}
