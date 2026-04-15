package com.songchao.supplier.modules.supplierproduct.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.supplierproduct.dto.SupplierProductPriceHistorySaveRequest;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProductPriceHistory;
import com.songchao.supplier.modules.supplierproduct.service.SupplierProductPriceHistoryService;
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
@RequestMapping("/supplier-product-price-histories")
public class SupplierProductPriceHistoryController {
    private final SupplierProductPriceHistoryService service;

    public SupplierProductPriceHistoryController(SupplierProductPriceHistoryService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:supplier-product:view")
    public ApiResponse<List<SupplierProductPriceHistory>> list(@RequestParam(required = false) Long supplierProductId) {
        return ApiResponse.ok(service.list(supplierProductId));
    }

    @PostMapping
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "CREATE_PRICE_HISTORY", bizType = "SUPPLIER_PRODUCT_PRICE", description = "新增历史价格记录")
    public ApiResponse<SupplierProductPriceHistory> create(@Valid @RequestBody SupplierProductPriceHistorySaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "UPDATE_PRICE_HISTORY", bizType = "SUPPLIER_PRODUCT_PRICE", description = "编辑历史价格记录")
    public ApiResponse<SupplierProductPriceHistory> update(@PathVariable Long id, @Valid @RequestBody SupplierProductPriceHistorySaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "DELETE_PRICE_HISTORY", bizType = "SUPPLIER_PRODUCT_PRICE", description = "删除历史价格记录")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
