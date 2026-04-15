package com.songchao.supplier.modules.quote.controller;

import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.quote.entity.QuoteItem;
import com.songchao.supplier.modules.quote.service.QuoteItemService;
import com.songchao.supplier.security.permission.RequirePermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quote-items")
public class QuoteItemController {
    private final QuoteItemService service;

    public QuoteItemController(QuoteItemService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:quote:view")
    public ApiResponse<List<QuoteItem>> list(
            @RequestParam(required = false) Long inquiryId,
            @RequestParam(required = false) Long quoteId
    ) {
        return ApiResponse.ok(service.list(inquiryId, quoteId));
    }
}
