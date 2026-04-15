package com.songchao.supplier.modules.inquiry.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.inquiry.dto.InquirySaveRequest;
import com.songchao.supplier.modules.inquiry.entity.Inquiry;
import com.songchao.supplier.modules.inquiry.service.InquiryService;
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
@RequestMapping("/inquiries")
public class InquiryController {
    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:inquiry:view")
    public ApiResponse<List<Inquiry>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return ApiResponse.ok(service.list(keyword, status));
    }

    @PostMapping
    @RequirePermission("supplier:inquiry:create")
    @OperationLogRecord(module = "询价比价", operation = "CREATE", bizType = "INQUIRY", description = "新增询价单")
    public ApiResponse<Inquiry> create(@Valid @RequestBody InquirySaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:inquiry:create")
    @OperationLogRecord(module = "询价比价", operation = "UPDATE", bizType = "INQUIRY", description = "编辑询价单")
    public ApiResponse<Inquiry> update(@PathVariable Long id, @Valid @RequestBody InquirySaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @PostMapping("/{id}/compare")
    @RequirePermission("supplier:inquiry:compare")
    @OperationLogRecord(module = "询价比价", operation = "COMPARE", bizType = "INQUIRY", description = "执行询价比价")
    public ApiResponse<Inquiry> compare(@PathVariable Long id) {
        return ApiResponse.ok(service.compare(id));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:inquiry:create")
    @OperationLogRecord(module = "询价比价", operation = "DELETE", bizType = "INQUIRY", description = "删除询价单")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
