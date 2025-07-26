package ru.larkin.customstarter.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.larkin.customstarter.audit.model.AuditEvent;
import org.springframework.kafka.core.KafkaTemplate;

@Service
@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    @Value("${audit.kafka.topic:audit-topic}")
    private final String topic;

    @Override
    public void audit(AuditEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
