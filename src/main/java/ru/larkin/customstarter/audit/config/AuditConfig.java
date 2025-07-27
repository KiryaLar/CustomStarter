package ru.larkin.customstarter.audit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import ru.larkin.customstarter.audit.model.AuditEvent;
import ru.larkin.customstarter.audit.service.AuditService;
import ru.larkin.customstarter.audit.service.ConsoleAuditService;
import ru.larkin.customstarter.audit.service.KafkaAuditService;

@Configuration
public class AuditConfig {

    @Bean
    @Primary
    public AuditService auditService(@Value("${starter.audit.mode:console}") String mode,
                                     KafkaTemplate<String, AuditEvent> kafkaTemplate,
                                     @Value("${starter.audit.kafka.topic:audit-topic}") String kafkaTopic) {
        if ("kafka".equalsIgnoreCase(mode)) {
            return new KafkaAuditService(kafkaTemplate, kafkaTopic);
        } else if ("console".equalsIgnoreCase(mode)) {
            return new ConsoleAuditService();
        } else {
            throw new IllegalArgumentException("Invalid audit.mode: " + mode + ". Supported values: console, kafka");
        }
    }
}
