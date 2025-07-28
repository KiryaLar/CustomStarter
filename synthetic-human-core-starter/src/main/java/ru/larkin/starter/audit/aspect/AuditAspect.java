package ru.larkin.starter.audit.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.larkin.starter.audit.model.AuditEvent;
import ru.larkin.starter.audit.model.Param;
import ru.larkin.starter.audit.service.AuditService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(ru.larkin.starter.audit.annotation.WeylandWatchingYou)")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String currentTime = Instant.now().toString();

        Object[] args = joinPoint.getArgs();
        Map<String, Param> paramsMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            paramsMap.put(parameter.getName(), new Param(parameter.getType().getSimpleName(), args[i]));
        }

        AuditEvent auditEvent = AuditEvent.builder()
                .methodName(method.getName())
                .time(currentTime)
                .params(paramsMap)
                .build();

        try {
            Object result = joinPoint.proceed();
            auditEvent.setSuccess(true);
            return result;
        } catch (Throwable throwable) {
            auditEvent.setSuccess(false);
            throw throwable;
        } finally {
            auditService.audit(auditEvent);
        }
    }
}