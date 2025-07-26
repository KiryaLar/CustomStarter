package ru.larkin.customstarter.audit.service;

import ru.larkin.customstarter.audit.model.AuditEvent;

public interface AuditService {

    void audit(AuditEvent event);
}
