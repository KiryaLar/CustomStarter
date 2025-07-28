package ru.larkin.bishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.larkin.bishop.service.BishopService;
import ru.larkin.starter.command.model.CommandDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bishop")
public class BishopController {

    private final BishopService bishopService;

    @PostMapping
    public ResponseEntity<String> receiveCommand(@Valid @RequestBody CommandDto commandDto) {
        bishopService.processCommand(commandDto);
        return ResponseEntity.ok("Command received and processed");
    }

}
