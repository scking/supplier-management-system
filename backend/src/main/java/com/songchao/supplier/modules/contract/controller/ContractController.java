package com.songchao.supplier.modules.contract.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.contract.dto.ContractDetailResponse;
import com.songchao.supplier.modules.contract.dto.ContractReminderResponse;
import com.songchao.supplier.modules.contract.dto.ContractSaveRequest;
import com.songchao.supplier.modules.contract.entity.Contract;
import com.songchao.supplier.modules.contract.service.ContractService;
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
@RequestMapping("/contracts")
public class ContractController {
    private final ContractService service;

    public ContractController(ContractService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:contract:view")
    public ApiResponse<List<Contract>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String contractStatus
    ) {
        return ApiResponse.ok(service.list(keyword, contractStatus));
    }

    @GetMapping("/{id}")
    @RequirePermission("supplier:contract:view")
    public ApiResponse<ContractDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(service.detail(id));
    }

    @GetMapping("/reminders")
    @RequirePermission("supplier:contract:view")
    public ApiResponse<List<ContractReminderResponse>> reminders() {
        return ApiResponse.ok(service.reminders());
    }

    @PostMapping
    @RequirePermission("supplier:contract:create")
    @OperationLogRecord(module = "合同管理", operation = "CREATE", bizType = "CONTRACT", description = "新增合同")
    public ApiResponse<Contract> create(@Valid @RequestBody ContractSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:contract:create")
    @OperationLogRecord(module = "合同管理", operation = "UPDATE", bizType = "CONTRACT", description = "编辑合同")
    public ApiResponse<Contract> update(@PathVariable Long id, @Valid @RequestBody ContractSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:contract:create")
    @OperationLogRecord(module = "合同管理", operation = "DELETE", bizType = "CONTRACT", description = "删除合同")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
