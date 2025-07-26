package ru.larkin.customstarter.command.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.larkin.customstarter.command.model.CommandDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandValidator {

    private final Validator validator;

    public void validate(CommandDto command) {
        Set<ConstraintViolation<CommandDto>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
