package com.songchao.supplier.modules.supplier.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.supplier.dto.SupplierSaveRequest;
import com.songchao.supplier.modules.supplier.entity.Supplier;
import com.songchao.supplier.modules.supplier.service.SupplierService;
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
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    @RequirePermission("supplier:supplier:view")
    public ApiResponse<List<Supplier>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return ApiResponse.ok(supplierService.list(keyword, status));
    }

    @GetMapping("/{id}")
    @RequirePermission("supplier:supplier:view")
    public ApiResponse<Supplier> detail(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.detail(id));
    }

    @PostMapping
    @RequirePermission("supplier:supplier:create")
    @OperationLogRecord(module = "供应商管理", operation = "CREATE", bizType = "SUPPLIER", description = "新增供应商")
    public ApiResponse<Supplier> create(@Valid @RequestBody SupplierSaveRequest request) {
        return ApiResponse.ok(supplierService.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:supplier:edit")
    @OperationLogRecord(module = "供应商管理", operation = "UPDATE", bizType = "SUPPLIER", description = "编辑供应商")
    public ApiResponse<Supplier> update(@PathVariable Long id, @Valid @RequestBody SupplierSaveRequest request) {
        return ApiResponse.ok(supplierService.update(id, request));
    }

    @PostMapping("/{id}/freeze")
    @RequirePermission("supplier:supplier:status")
    @OperationLogRecord(module = "供应商管理", operation = "FREEZE", bizType = "SUPPLIER", description = "冻结供应商")
    public ApiResponse<Supplier> freeze(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.changeStatus(id, "FROZEN"));
    }

    @PostMapping("/{id}/unfreeze")
    @RequirePermission("supplier:supplier:status")
    @OperationLogRecord(module = "供应商管理", operation = "UNFREEZE", bizType = "SUPPLIER", description = "解冻供应商")
    public ApiResponse<Supplier> unfreeze(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.changeStatus(id, "NORMAL"));
    }

    @PostMapping("/{id}/blacklist")
    @RequirePermission("supplier:supplier:blacklist")
    @OperationLogRecord(module = "供应商管理", operation = "BLACKLIST", bizType = "SUPPLIER", description = "拉黑供应商")
    public ApiResponse<Supplier> blacklist(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.changeStatus(id, "BLACKLIST"));
    }

    @PostMapping("/{id}/remove-blacklist")
    @RequirePermission("supplier:supplier:blacklist")
    @OperationLogRecord(module = "供应商管理", operation = "REMOVE_BLACKLIST", bizType = "SUPPLIER", description = "移出黑名单")
    public ApiResponse<Supplier> removeBlacklist(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.changeStatus(id, "NORMAL"));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:supplier:edit")
    @OperationLogRecord(module = "供应商管理", operation = "DELETE", bizType = "SUPPLIER", description = "删除供应商")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ApiResponse.ok(true);
    }
}
