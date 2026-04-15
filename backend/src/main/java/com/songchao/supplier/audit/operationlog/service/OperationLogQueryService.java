package com.songchao.supplier.audit.operationlog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.audit.operationlog.entity.OperationLog;
import com.songchao.supplier.audit.operationlog.mapper.OperationLogMapper;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.auth.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogQueryService {
    private final OperationLogMapper mapper;

    public OperationLogQueryService(OperationLogMapper mapper) {
        this.mapper = mapper;
    }

    public List<OperationLog> list(String moduleName, String operationType, String resultStatus) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (moduleName != null && !moduleName.isBlank()) {
            wrapper.like(OperationLog::getModuleName, moduleName);
        }
        if (operationType != null && !operationType.isBlank()) {
            wrapper.eq(OperationLog::getOperationType, operationType);
        }
        if (resultStatus != null && !resultStatus.isBlank()) {
            wrapper.eq(OperationLog::getResultStatus, resultStatus);
        }
        CurrentUser currentUser = AuthContext.get();
        if (currentUser != null && !currentUser.hasRole("SYSTEM_ADMIN")) {
            Integer dataScope = currentUser.dataScope();
            if (dataScope != null && dataScope < 5) {
                if (dataScope >= 3 && currentUser.deptId() != null) {
                    wrapper.eq(OperationLog::getOperatorDeptId, currentUser.deptId());
                } else if (currentUser.userId() != null) {
                    wrapper.eq(OperationLog::getOperatorId, currentUser.userId());
                }
            }
        }
        wrapper.orderByDesc(OperationLog::getId);
        return mapper.selectList(wrapper);
    }
}
