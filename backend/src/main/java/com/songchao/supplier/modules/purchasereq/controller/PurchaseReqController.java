package com.songchao.supplier.modules.purchasereq.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.purchasereq.dto.PurchaseReqSaveRequest;
import com.songchao.supplier.modules.purchasereq.entity.PurchaseReq;
import com.songchao.supplier.modules.purchasereq.service.PurchaseReqService;
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
@RequestMapping("/purchase-requests")
public class PurchaseReqController {
    private final PurchaseReqService service;

    public PurchaseReqController(PurchaseReqService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:purchase:view")
    public ApiResponse<List<PurchaseReq>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String reqStatus
    ) {
        return ApiResponse.ok(service.list(keyword, reqStatus));
    }

    @PostMapping
    @RequirePermission("supplier:purchase:create")
    @OperationLogRecord(module = "采购需求", operation = "CREATE", bizType = "PURCHASE_REQ", description = "新增采购需求")
    public ApiResponse<PurchaseReq> create(@Valid @RequestBody PurchaseReqSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:purchase:create")
    @OperationLogRecord(module = "采购需求", operation = "UPDATE", bizType = "PURCHASE_REQ", description = "编辑采购需求")
    public ApiResponse<PurchaseReq> update(@PathVariable Long id, @Valid @RequestBody PurchaseReqSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @PostMapping("/{id}/submit")
    @RequirePermission("supplier:purchase:submit")
    @OperationLogRecord(module = "采购需求", operation = "SUBMIT", bizType = "PURCHASE_REQ", description = "提交采购需求")
    public ApiResponse<PurchaseReq> submit(@PathVariable Long id) {
        return ApiResponse.ok(service.submit(id));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:purchase:create")
    @OperationLogRecord(module = "采购需求", operation = "DELETE", bizType = "PURCHASE_REQ", description = "删除采购需求")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
