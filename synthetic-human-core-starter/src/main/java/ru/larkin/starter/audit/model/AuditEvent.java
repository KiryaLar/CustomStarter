package ru.larkin.starter.audit.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AuditEvent {

    private String methodName;
    private String time;
    private Map<String, Param> params;
    private boolean isSuccess;
}
