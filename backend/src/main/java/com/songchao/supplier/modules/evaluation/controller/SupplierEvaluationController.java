package com.songchao.supplier.modules.evaluation.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.evaluation.dto.EvaluationSaveRequest;
import com.songchao.supplier.modules.evaluation.entity.SupplierEvaluation;
import com.songchao.supplier.modules.evaluation.service.SupplierEvaluationService;
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
@RequestMapping("/evaluations")
public class SupplierEvaluationController {
    private final SupplierEvaluationService service;

    public SupplierEvaluationController(SupplierEvaluationService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:evaluation:view")
    public ApiResponse<List<SupplierEvaluation>> list(@RequestParam(required = false) Long supplierId) {
        return ApiResponse.ok(service.list(supplierId));
    }

    @PostMapping
    @RequirePermission("supplier:evaluation:create")
    @OperationLogRecord(module = "供应商评价", operation = "CREATE", bizType = "EVALUATION", description = "新增供应商评价")
    public ApiResponse<SupplierEvaluation> create(@Valid @RequestBody EvaluationSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:evaluation:create")
    @OperationLogRecord(module = "供应商评价", operation = "UPDATE", bizType = "EVALUATION", description = "编辑供应商评价")
    public ApiResponse<SupplierEvaluation> update(@PathVariable Long id, @Valid @RequestBody EvaluationSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:evaluation:create")
    @OperationLogRecord(module = "供应商评价", operation = "DELETE", bizType = "EVALUATION", description = "删除供应商评价")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
