package com.songchao.supplier.modules.product.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.product.dto.ProductSaveRequest;
import com.songchao.supplier.modules.product.entity.Product;
import com.songchao.supplier.modules.product.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:product:view")
    public ApiResponse<List<Product>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.ok(service.list(keyword));
    }

    @PostMapping
    @RequirePermission("supplier:product:create")
    @OperationLogRecord(module = "产品管理", operation = "CREATE", bizType = "PRODUCT", description = "新增产品")
    public ApiResponse<Product> create(@Valid @RequestBody ProductSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:product:create")
    @OperationLogRecord(module = "产品管理", operation = "UPDATE", bizType = "PRODUCT", description = "编辑产品")
    public ApiResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:product:create")
    @OperationLogRecord(module = "产品管理", operation = "DELETE", bizType = "PRODUCT", description = "删除产品")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
