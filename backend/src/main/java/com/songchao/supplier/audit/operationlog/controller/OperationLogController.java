package com.songchao.supplier.audit.operationlog.controller;

import com.songchao.supplier.audit.operationlog.entity.OperationLog;
import com.songchao.supplier.audit.operationlog.service.OperationLogQueryService;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.security.permission.RequirePermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operation-logs")
public class OperationLogController {
    private final OperationLogQueryService service;

    public OperationLogController(OperationLogQueryService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:operation-log:view")
    public ApiResponse<List<OperationLog>> list(
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String resultStatus
    ) {
        return ApiResponse.ok(service.list(moduleName, operationType, resultStatus));
    }
}
