package com.songchao.supplier.modules.performance.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.performance.dto.PerformanceSaveRequest;
import com.songchao.supplier.modules.performance.entity.Performance;
import com.songchao.supplier.modules.performance.service.PerformanceService;
import com.songchao.supplier.security.permission.RequirePermission;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/performances")
public class PerformanceController {
    private final PerformanceService service;

    public PerformanceController(PerformanceService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:performance:view")
    public ApiResponse<List<Performance>> list(
            @RequestParam(required = false) Long contractId,
            @RequestParam(required = false) String status
    ) {
        return ApiResponse.ok(service.list(contractId, status));
    }

    @PostMapping
    @RequirePermission("supplier:performance:create")
    @OperationLogRecord(module = "履约管理", operation = "CREATE", bizType = "PERFORMANCE", description = "新增履约节点")
    public ApiResponse<Performance> create(@Valid @RequestBody PerformanceSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:performance:create")
    @OperationLogRecord(module = "履约管理", operation = "UPDATE", bizType = "PERFORMANCE", description = "编辑履约节点")
    public ApiResponse<Performance> update(@PathVariable Long id, @Valid @RequestBody PerformanceSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:performance:create")
    @OperationLogRecord(module = "履约管理", operation = "DELETE", bizType = "PERFORMANCE", description = "删除履约节点")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
