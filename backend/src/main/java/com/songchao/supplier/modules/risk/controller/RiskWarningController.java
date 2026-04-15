package com.songchao.supplier.modules.risk.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.risk.dto.RiskHandleRequest;
import com.songchao.supplier.modules.risk.entity.RiskWarning;
import com.songchao.supplier.modules.risk.service.RiskWarningService;
import com.songchao.supplier.security.permission.RequirePermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/risks")
public class RiskWarningController {
    private final RiskWarningService service;

    public RiskWarningController(RiskWarningService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:risk:view")
    public ApiResponse<List<RiskWarning>> list(@RequestParam(required = false) String status) {
        return ApiResponse.ok(service.list(status));
    }

    @PostMapping("/{id}/handle")
    @RequirePermission("supplier:risk:handle")
    @OperationLogRecord(module = "风险预警", operation = "HANDLE", bizType = "RISK", description = "处理风险预警")
    public ApiResponse<RiskWarning> handle(@PathVariable Long id, @RequestBody RiskHandleRequest request) {
        return ApiResponse.ok(service.handle(id, request, "HANDLED"));
    }

    @PostMapping("/{id}/ignore")
    @RequirePermission("supplier:risk:handle")
    @OperationLogRecord(module = "风险预警", operation = "IGNORE", bizType = "RISK", description = "忽略风险预警")
    public ApiResponse<RiskWarning> ignore(@PathVariable Long id, @RequestBody RiskHandleRequest request) {
        return ApiResponse.ok(service.handle(id, request, "IGNORED"));
    }
}
