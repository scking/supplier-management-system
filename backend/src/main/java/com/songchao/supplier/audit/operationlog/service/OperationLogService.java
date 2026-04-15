package com.songchao.supplier.audit.operationlog.service;

import com.songchao.supplier.audit.operationlog.entity.OperationLog;
import com.songchao.supplier.audit.operationlog.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {
    private final OperationLogMapper mapper;

    public OperationLogService(OperationLogMapper mapper) {
        this.mapper = mapper;
    }

    public void save(OperationLog entity) {
        mapper.insert(entity);
    }
}
