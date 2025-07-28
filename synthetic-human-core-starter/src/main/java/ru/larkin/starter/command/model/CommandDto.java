package ru.larkin.starter.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandDto {
    @Size(max = 1000, message = "Длина описания не должна быть больше 1000 символов.")
    @NotBlank(message = "Требуется описание.")
    private String description;
    @NotNull(message = "Требуется приоритет.")
    private Priority priority;
    @Size(max = 100, message = "Длина автора не должна быть больше 100 символов.")
    @NotBlank(message = "Требуется автор.")
    private String author;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})?)?$", message = "Время должно соответствовать формату ISO-8601")
    private String time;
}
