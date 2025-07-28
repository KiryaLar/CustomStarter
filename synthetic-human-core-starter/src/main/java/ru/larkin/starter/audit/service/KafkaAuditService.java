package ru.larkin.starter.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import ru.larkin.starter.audit.model.AuditEvent;

@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    private final String topic;

    @Override
    public void audit(AuditEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
