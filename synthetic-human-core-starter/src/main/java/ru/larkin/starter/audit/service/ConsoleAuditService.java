package ru.larkin.starter.audit.service;

import lombok.extern.slf4j.Slf4j;
import ru.larkin.starter.audit.model.AuditEvent;

@Slf4j
public class ConsoleAuditService implements AuditService {

    @Override
    public void audit(AuditEvent event) {
        log.info("Event: method - {}, parameters - {}, time - {}",
                event.getMethodName(),
                event.getParams(),
                event.getTime());
    }
}
