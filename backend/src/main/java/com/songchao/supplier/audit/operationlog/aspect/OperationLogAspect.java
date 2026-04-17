package com.songchao.supplier.audit.operationlog.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.audit.operationlog.entity.OperationLog;
import com.songchao.supplier.audit.operationlog.service.OperationLogService;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.auth.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class OperationLogAspect {
    private static final int JSON_COLUMN_MAX_LENGTH = 2000;
    private static final int JSON_PREVIEW_MAX_LENGTH = 1200;

    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(OperationLogService operationLogService, ObjectMapper objectMapper) {
        this.operationLogService = operationLogService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(record)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLogRecord record) throws Throwable {
        OperationLog log = buildBaseLog(record, joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.setResultStatus("SUCCESS");
            log.setAfterData(toJson(result));
            log.setBizId(extractBizId(result));
            operationLogService.save(log);
            return result;
        } catch (Throwable ex) {
            log.setResultStatus("FAILED");
            log.setErrorMessage(truncate(ex.getMessage(), 500));
            operationLogService.save(log);
            throw ex;
        }
    }

    private OperationLog buildBaseLog(OperationLogRecord record, Object[] args) {
        OperationLog log = new OperationLog();
        log.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        log.setModuleName(record.module());
        log.setBizType(record.bizType());
        log.setOperationType(record.operation());
        log.setOperationDesc(record.description());
        log.setBeforeData(toJson(sanitizeArgs(args)));
        log.setCreatedAt(LocalDateTime.now().toString());
        CurrentUser currentUser = AuthContext.get();
        if (currentUser != null) {
            log.setOperatorId(currentUser.userId());
            log.setOperatorDeptId(currentUser.deptId());
            log.setOperatorName(currentUser.realName());
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.setRequestMethod(request.getMethod());
            log.setRequestUri(request.getRequestURI());
            log.setOperatorIp(request.getRemoteAddr());
        }
        return log;
    }

    private Long extractBizId(Object result) {
        if (result == null) {
            return null;
        }
        try {
            Object data = result.getClass().getMethod("data").invoke(result);
            if (data == null) {
                return null;
            }
            return (Long) data.getClass().getMethod("getId").invoke(data);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Object[] sanitizeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        List<Object> sanitized = new ArrayList<>();
        for (Object arg : Arrays.stream(args).toList()) {
            if (arg == null) {
                sanitized.add(null);
                continue;
            }
            if (arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse
                    || arg instanceof BindingResult) {
                sanitized.add(Map.of("type", arg.getClass().getSimpleName()));
                continue;
            }
            if (arg instanceof MultipartFile file) {
                Map<String, Object> fileInfo = new LinkedHashMap<>();
                fileInfo.put("type", file.getClass().getSimpleName());
                fileInfo.put("name", file.getName());
                fileInfo.put("originalFilename", file.getOriginalFilename());
                fileInfo.put("size", file.getSize());
                sanitized.add(fileInfo);
                continue;
            }
            sanitized.add(arg);
        }
        return sanitized.toArray();
    }

    private String toJson(Object value) {
        try {
            return serializeForJsonColumn(value);
        } catch (JsonProcessingException e) {
            try {
                Map<String, Object> fallback = new LinkedHashMap<>();
                fallback.put("unserializable", String.valueOf(value));
                fallback.put("error", e.getOriginalMessage());
                return serializeForJsonColumn(fallback);
            } catch (JsonProcessingException ignored) {
                return "{\"unserializable\":\"serialization_failed\"}";
            }
        }
    }

    private String serializeForJsonColumn(Object value) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        if (json.length() <= JSON_COLUMN_MAX_LENGTH) {
            return json;
        }
        Map<String, Object> truncated = new LinkedHashMap<>();
        truncated.put("truncated", true);
        truncated.put("originalLength", json.length());
        truncated.put("preview", truncate(json, JSON_PREVIEW_MAX_LENGTH));
        String truncatedJson = objectMapper.writeValueAsString(truncated);
        if (truncatedJson.length() <= JSON_COLUMN_MAX_LENGTH) {
            return truncatedJson;
        }

        Map<String, Object> minimal = new LinkedHashMap<>();
        minimal.put("truncated", true);
        minimal.put("originalLength", json.length());
        return objectMapper.writeValueAsString(minimal);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
