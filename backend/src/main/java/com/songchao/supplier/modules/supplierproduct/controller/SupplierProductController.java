package com.songchao.supplier.modules.supplierproduct.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.supplierproduct.dto.SupplierProductSaveRequest;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProduct;
import com.songchao.supplier.modules.supplierproduct.service.SupplierProductService;
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
@RequestMapping("/supplier-products")
public class SupplierProductController {
    private final SupplierProductService service;

    public SupplierProductController(SupplierProductService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:supplier-product:view")
    public ApiResponse<List<SupplierProduct>> list(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long productId
    ) {
        return ApiResponse.ok(service.list(supplierId, productId));
    }

    @PostMapping
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "CREATE", bizType = "SUPPLIER_PRODUCT", description = "新增产品供应关系")
    public ApiResponse<SupplierProduct> create(@Valid @RequestBody SupplierProductSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "UPDATE", bizType = "SUPPLIER_PRODUCT", description = "编辑产品供应关系")
    public ApiResponse<SupplierProduct> update(@PathVariable Long id, @Valid @RequestBody SupplierProductSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:supplier-product:create")
    @OperationLogRecord(module = "产品供应关系", operation = "DELETE", bizType = "SUPPLIER_PRODUCT", description = "删除产品供应关系")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
