package com.songchao.supplier.modules.dashboard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.modules.contract.entity.Contract;
import com.songchao.supplier.modules.contract.mapper.ContractMapper;
import com.songchao.supplier.modules.qualification.entity.SupplierQualification;
import com.songchao.supplier.modules.qualification.mapper.SupplierQualificationMapper;
import com.songchao.supplier.modules.risk.entity.RiskWarning;
import com.songchao.supplier.modules.risk.mapper.RiskWarningMapper;
import com.songchao.supplier.modules.supplier.entity.Supplier;
import com.songchao.supplier.modules.supplier.mapper.SupplierMapper;
import com.songchao.supplier.security.permission.RequirePermission;
import com.songchao.supplier.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final SupplierMapper supplierMapper;
    private final SupplierQualificationMapper qualificationMapper;
    private final ContractMapper contractMapper;
    private final RiskWarningMapper riskWarningMapper;

    public DashboardController(
            SupplierMapper supplierMapper,
            SupplierQualificationMapper qualificationMapper,
            ContractMapper contractMapper,
            RiskWarningMapper riskWarningMapper
    ) {
        this.supplierMapper = supplierMapper;
        this.qualificationMapper = qualificationMapper;
        this.contractMapper = contractMapper;
        this.riskWarningMapper = riskWarningMapper;
    }

    @GetMapping("/overview")
    @RequirePermission("supplier:dashboard:view")
    public ApiResponse<Map<String, Object>> overview() {
        LocalDate today = LocalDate.now();
        LocalDate inThirtyDays = today.plusDays(30);

        Long supplierCount = supplierMapper.selectCount(new LambdaQueryWrapper<Supplier>().eq(Supplier::getDeleted, 0));
        Long riskCount = riskWarningMapper.selectCount(new LambdaQueryWrapper<RiskWarning>().eq(RiskWarning::getStatus, "OPEN"));

        long qualificationExpiringCount = qualificationMapper.selectList(new LambdaQueryWrapper<SupplierQualification>()).stream()
                .filter(item -> item.getExpireDate() != null && !item.getExpireDate().isBlank())
                .map(SupplierQualification::getExpireDate)
                .map(LocalDate::parse)
                .filter(date -> !date.isBefore(today) && !date.isAfter(inThirtyDays))
                .count();

        long contractExpiringCount = contractMapper.selectList(new LambdaQueryWrapper<Contract>()).stream()
                .filter(item -> item.getExpireDate() != null && !item.getExpireDate().isBlank())
                .map(Contract::getExpireDate)
                .map(LocalDate::parse)
                .filter(date -> !date.isBefore(today) && !date.isAfter(inThirtyDays))
                .count();

        BigDecimal monthPurchaseAmount = contractMapper.selectList(new LambdaQueryWrapper<Contract>()).stream()
                .filter(item -> item.getSignDate() != null && !item.getSignDate().isBlank())
                .filter(item -> LocalDate.parse(item.getSignDate()).getYear() == today.getYear()
                        && LocalDate.parse(item.getSignDate()).getMonthValue() == today.getMonthValue())
                .map(Contract::getContractAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ApiResponse.ok(Map.of(
                "todoCount", 8,
                "supplierCount", supplierCount,
                "qualificationExpiringCount", qualificationExpiringCount,
                "contractExpiringCount", contractExpiringCount,
                "riskCount", riskCount,
                "monthPurchaseAmount", monthPurchaseAmount,
                "warnings", List.of(
                        Map.of("title", qualificationExpiringCount + " 家供应商资质 30 天内到期", "level", "warning"),
                        Map.of("title", contractExpiringCount + " 个合同 30 天内到期", "level", "danger")
                )
        ));
    }
}
