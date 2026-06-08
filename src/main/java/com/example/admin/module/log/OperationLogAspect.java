package com.example.admin.module.log;

import com.example.admin.security.CurrentUser;
import com.example.admin.security.CurrentUserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class OperationLogAspect {

    private static final String UNKNOWN = "unknown";

    private final LogPublisher logPublisher;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(LogPublisher logPublisher) {
        this.logPublisher = logPublisher;
        this.objectMapper = new ObjectMapper();
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            saveLog(joinPoint, operationLog, result, System.currentTimeMillis() - start, 1, null);
            return result;
        } catch (Throwable ex) {
            saveLog(joinPoint, operationLog, null, System.currentTimeMillis() - start, 0, ex.getMessage());
            throw ex;
        }
    }

    private void saveLog(
            ProceedingJoinPoint joinPoint,
            OperationLog operationLog,
            Object result,
            long duration,
            int status,
            String errorMsg
    ) {
        HttpServletRequest request = currentRequest();
        SysLog sysLog = new SysLog(
                null,
                currentUsername(),
                operationLog.module(),
                operationLog.operation(),
                request == null ? UNKNOWN : request.getMethod(),
                request == null ? UNKNOWN : request.getRequestURI(),
                request == null ? UNKNOWN : request.getRemoteAddr(),
                toJson(joinPoint.getArgs()),
                result == null ? null : toJson(result),
                duration,
                status,
                errorMsg,
                null
        );

        logPublisher.publish(sysLog);
    }

    private String currentUsername() {
        CurrentUser currentUser = CurrentUserContext.get();
        if (currentUser == null) {
            return UNKNOWN;
        }
        return currentUser.username();
    }

    private HttpServletRequest currentRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest();
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return String.valueOf(value);
        }
    }
}
