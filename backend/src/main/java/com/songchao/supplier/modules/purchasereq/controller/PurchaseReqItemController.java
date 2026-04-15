package com.songchao.supplier.modules.purchasereq.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.purchasereq.dto.PurchaseReqItemSaveRequest;
import com.songchao.supplier.modules.purchasereq.entity.PurchaseReqItem;
import com.songchao.supplier.modules.purchasereq.service.PurchaseReqItemService;
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
@RequestMapping("/purchase-request-items")
public class PurchaseReqItemController {
    private final PurchaseReqItemService service;

    public PurchaseReqItemController(PurchaseReqItemService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:purchase:item:view")
    public ApiResponse<List<PurchaseReqItem>> list(@RequestParam(required = false) Long reqId) {
        return ApiResponse.ok(service.list(reqId));
    }

    @PostMapping
    @RequirePermission("supplier:purchase:item:create")
    @OperationLogRecord(module = "采购需求", operation = "CREATE_ITEM", bizType = "PURCHASE_REQ_ITEM", description = "新增采购需求明细")
    public ApiResponse<PurchaseReqItem> create(@Valid @RequestBody PurchaseReqItemSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:purchase:item:create")
    @OperationLogRecord(module = "采购需求", operation = "UPDATE_ITEM", bizType = "PURCHASE_REQ_ITEM", description = "编辑采购需求明细")
    public ApiResponse<PurchaseReqItem> update(@PathVariable Long id, @Valid @RequestBody PurchaseReqItemSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:purchase:item:create")
    @OperationLogRecord(module = "采购需求", operation = "DELETE_ITEM", bizType = "PURCHASE_REQ_ITEM", description = "删除采购需求明细")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
