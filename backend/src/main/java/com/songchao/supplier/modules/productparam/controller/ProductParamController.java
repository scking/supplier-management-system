package com.songchao.supplier.modules.productparam.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.productparam.dto.ProductParamSaveRequest;
import com.songchao.supplier.modules.productparam.entity.ProductParam;
import com.songchao.supplier.modules.productparam.service.ProductParamService;
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
@RequestMapping("/product-params")
public class ProductParamController {
    private final ProductParamService service;

    public ProductParamController(ProductParamService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:product:param:view")
    public ApiResponse<List<ProductParam>> list(@RequestParam(required = false) Long productId) {
        return ApiResponse.ok(service.list(productId));
    }

    @PostMapping
    @RequirePermission("supplier:product:param:create")
    @OperationLogRecord(module = "产品管理", operation = "CREATE_PARAM", bizType = "PRODUCT_PARAM", description = "新增产品参数")
    public ApiResponse<ProductParam> create(@Valid @RequestBody ProductParamSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:product:param:create")
    @OperationLogRecord(module = "产品管理", operation = "UPDATE_PARAM", bizType = "PRODUCT_PARAM", description = "编辑产品参数")
    public ApiResponse<ProductParam> update(@PathVariable Long id, @Valid @RequestBody ProductParamSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:product:param:create")
    @OperationLogRecord(module = "产品管理", operation = "DELETE_PARAM", bizType = "PRODUCT_PARAM", description = "删除产品参数")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
