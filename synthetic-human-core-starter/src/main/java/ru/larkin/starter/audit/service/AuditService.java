package ru.larkin.starter.audit.service;

import ru.larkin.starter.audit.model.AuditEvent;

public interface AuditService {

    void audit(AuditEvent event);
}
