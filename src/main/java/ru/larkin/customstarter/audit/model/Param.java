package ru.larkin.customstarter.audit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Param {
    private String name;
    private Object value;
}
