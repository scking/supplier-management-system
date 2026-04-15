package com.songchao.supplier.modules.productattachment.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.productattachment.dto.ProductAttachmentSaveRequest;
import com.songchao.supplier.modules.productattachment.entity.ProductAttachment;
import com.songchao.supplier.modules.productattachment.service.ProductAttachmentService;
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
@RequestMapping("/product-attachments")
public class ProductAttachmentController {
    private final ProductAttachmentService service;

    public ProductAttachmentController(ProductAttachmentService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:product:attachment:view")
    public ApiResponse<List<ProductAttachment>> list(@RequestParam(required = false) Long productId) {
        return ApiResponse.ok(service.list(productId));
    }

    @PostMapping
    @RequirePermission("supplier:product:attachment:create")
    @OperationLogRecord(module = "产品附件", operation = "CREATE", bizType = "PRODUCT_ATTACHMENT", description = "新增产品附件")
    public ApiResponse<ProductAttachment> create(@Valid @RequestBody ProductAttachmentSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:product:attachment:create")
    @OperationLogRecord(module = "产品附件", operation = "UPDATE", bizType = "PRODUCT_ATTACHMENT", description = "编辑产品附件")
    public ApiResponse<ProductAttachment> update(@PathVariable Long id, @Valid @RequestBody ProductAttachmentSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:product:attachment:create")
    @OperationLogRecord(module = "产品附件", operation = "DELETE", bizType = "PRODUCT_ATTACHMENT", description = "删除产品附件")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
