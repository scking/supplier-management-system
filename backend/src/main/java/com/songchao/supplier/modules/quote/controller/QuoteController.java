package com.songchao.supplier.modules.quote.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.inquiry.entity.Inquiry;
import com.songchao.supplier.modules.quote.dto.QuoteSaveRequest;
import com.songchao.supplier.modules.quote.entity.Quote;
import com.songchao.supplier.modules.quote.service.QuoteService;
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
@RequestMapping("/quotes")
public class QuoteController {
    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:quote:view")
    public ApiResponse<List<Quote>> list(@RequestParam(required = false) Long inquiryId) {
        return ApiResponse.ok(service.list(inquiryId));
    }

    @PostMapping
    @RequirePermission("supplier:quote:create")
    @OperationLogRecord(module = "询价比价", operation = "CREATE_QUOTE", bizType = "QUOTE", description = "录入供应商报价")
    public ApiResponse<Quote> create(@Valid @RequestBody QuoteSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:quote:create")
    @OperationLogRecord(module = "询价比价", operation = "UPDATE_QUOTE", bizType = "QUOTE", description = "编辑供应商报价")
    public ApiResponse<Quote> update(@PathVariable Long id, @Valid @RequestBody QuoteSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @PostMapping("/recommend/{inquiryId}")
    @RequirePermission("supplier:quote:recommend")
    @OperationLogRecord(module = "询价比价", operation = "RECOMMEND", bizType = "INQUIRY", description = "推荐中选供应商")
    public ApiResponse<Inquiry> recommend(@PathVariable Long inquiryId) {
        return ApiResponse.ok(service.recommend(inquiryId));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:quote:create")
    @OperationLogRecord(module = "询价比价", operation = "DELETE_QUOTE", bizType = "QUOTE", description = "删除供应商报价")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
