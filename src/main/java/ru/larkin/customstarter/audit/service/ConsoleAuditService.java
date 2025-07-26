package ru.larkin.customstarter.audit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.larkin.customstarter.audit.model.AuditEvent;

@Slf4j
@Service
public class ConsoleAuditService implements AuditService {

    @Override
    public void audit(AuditEvent event) {
        log.info("Event: method - {}, parameters - {}, time - {}",
                event.getMethodName(),
                event.getParams(),
                event.getTime());
    }
}
