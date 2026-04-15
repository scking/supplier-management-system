package com.songchao.supplier.modules.qualification.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.qualification.dto.QualificationSaveRequest;
import com.songchao.supplier.modules.qualification.entity.SupplierQualification;
import com.songchao.supplier.modules.qualification.service.SupplierQualificationService;
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
@RequestMapping("/supplier-qualifications")
public class SupplierQualificationController {
    private final SupplierQualificationService service;

    public SupplierQualificationController(SupplierQualificationService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:qualification:view")
    public ApiResponse<List<SupplierQualification>> list(@RequestParam(required = false) Long supplierId) {
        return ApiResponse.ok(service.list(supplierId));
    }

    @PostMapping
    @RequirePermission("supplier:qualification:create")
    @OperationLogRecord(module = "资质管理", operation = "CREATE", bizType = "QUALIFICATION", description = "新增供应商资质")
    public ApiResponse<SupplierQualification> create(@Valid @RequestBody QualificationSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:qualification:create")
    @OperationLogRecord(module = "资质管理", operation = "UPDATE", bizType = "QUALIFICATION", description = "编辑供应商资质")
    public ApiResponse<SupplierQualification> update(@PathVariable Long id, @Valid @RequestBody QualificationSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:qualification:create")
    @OperationLogRecord(module = "资质管理", operation = "DELETE", bizType = "QUALIFICATION", description = "删除供应商资质")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
