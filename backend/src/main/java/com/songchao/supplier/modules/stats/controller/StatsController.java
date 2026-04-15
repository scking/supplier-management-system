package com.songchao.supplier.modules.stats.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.modules.contract.entity.Contract;
import com.songchao.supplier.modules.contract.mapper.ContractMapper;
import com.songchao.supplier.modules.performance.entity.Performance;
import com.songchao.supplier.modules.performance.mapper.PerformanceMapper;
import com.songchao.supplier.modules.risk.entity.RiskWarning;
import com.songchao.supplier.modules.risk.mapper.RiskWarningMapper;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProductPriceHistory;
import com.songchao.supplier.modules.supplierproduct.mapper.SupplierProductPriceHistoryMapper;
import com.songchao.supplier.modules.supplier.entity.Supplier;
import com.songchao.supplier.modules.supplier.mapper.SupplierMapper;
import com.songchao.supplier.security.permission.RequirePermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final SupplierMapper supplierMapper;
    private final ContractMapper contractMapper;
    private final RiskWarningMapper riskWarningMapper;
    private final PerformanceMapper performanceMapper;
    private final SupplierProductPriceHistoryMapper supplierProductPriceHistoryMapper;

    public StatsController(
            SupplierMapper supplierMapper,
            ContractMapper contractMapper,
            RiskWarningMapper riskWarningMapper,
            PerformanceMapper performanceMapper,
            SupplierProductPriceHistoryMapper supplierProductPriceHistoryMapper
    ) {
        this.supplierMapper = supplierMapper;
        this.contractMapper = contractMapper;
        this.riskWarningMapper = riskWarningMapper;
        this.performanceMapper = performanceMapper;
        this.supplierProductPriceHistoryMapper = supplierProductPriceHistoryMapper;
    }

    @GetMapping("/overview")
    @RequirePermission("supplier:stats:view")
    public ApiResponse<Map<String, Object>> overview() {
        Long supplierCount = supplierMapper.selectCount(new LambdaQueryWrapper<Supplier>().eq(Supplier::getDeleted, 0));
        Long activeSupplierCount = supplierMapper.selectCount(new LambdaQueryWrapper<Supplier>()
                .eq(Supplier::getDeleted, 0)
                .eq(Supplier::getStatus, "NORMAL"));
        Long contractCount = contractMapper.selectCount(new LambdaQueryWrapper<Contract>());
        Long riskCount = riskWarningMapper.selectCount(new LambdaQueryWrapper<RiskWarning>().eq(RiskWarning::getStatus, "OPEN"));
        Long performanceCount = performanceMapper.selectCount(new LambdaQueryWrapper<Performance>());
        BigDecimal contractAmount = contractMapper.selectList(new LambdaQueryWrapper<>()).stream()
                .map(Contract::getContractAmount)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ApiResponse.ok(Map.of(
                "supplierCount", supplierCount,
                "activeSupplierCount", activeSupplierCount,
                "contractCount", contractCount,
                "openRiskCount", riskCount,
                "riskCount", riskCount,
                "performanceCount", performanceCount,
                "contractAmount", contractAmount,
                "monthPurchaseAmount", contractAmount,
                "purchaseAmount", contractAmount
        ));
    }

    @GetMapping("/analysis")
    @RequirePermission("supplier:stats:view")
    public ApiResponse<Map<String, Object>> analysis() {
        List<Supplier> suppliers = supplierMapper.selectList(new LambdaQueryWrapper<Supplier>().eq(Supplier::getDeleted, 0));
        List<Contract> contracts = contractMapper.selectList(new LambdaQueryWrapper<>());
        List<RiskWarning> risks = riskWarningMapper.selectList(new LambdaQueryWrapper<>());
        List<SupplierProductPriceHistory> priceHistories = supplierProductPriceHistoryMapper.selectList(new LambdaQueryWrapper<>());

        List<Map<String, Object>> supplierTypeStats = suppliers.stream()
                .collect(Collectors.groupingBy(Supplier::getSupplierType, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "name", entry.getKey() == null ? "未分类" : entry.getKey(),
                        "value", entry.getValue()
                ))
                .sorted((a, b) -> Long.compare(Long.parseLong(String.valueOf(b.get("value"))), Long.parseLong(String.valueOf(a.get("value")))))
                .toList();

        Map<Long, BigDecimal> contractBySupplier = new HashMap<>();
        for (Contract contract : contracts) {
            if (contract.getSupplierId() == null || contract.getContractAmount() == null) {
                continue;
            }
            contractBySupplier.merge(contract.getSupplierId(), contract.getContractAmount(), BigDecimal::add);
        }
        Map<Long, Supplier> supplierMap = suppliers.stream().collect(Collectors.toMap(Supplier::getId, item -> item, (a, b) -> a));
        List<Map<String, Object>> cooperationTop = contractBySupplier.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> Map.<String, Object>of(
                        "supplierId", entry.getKey(),
                        "supplierName", supplierMap.containsKey(entry.getKey()) ? supplierMap.get(entry.getKey()).getSupplierName() : "未知供应商",
                        "amount", entry.getValue().setScale(2, RoundingMode.HALF_UP)
                ))
                .toList();

        List<Map<String, Object>> riskLevelStats = risks.stream()
                .collect(Collectors.groupingBy(RiskWarning::getWarningLevel, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "name", entry.getKey() == null ? "未分级" : entry.getKey(),
                        "value", entry.getValue()
                ))
                .toList();

        Map<String, BigDecimal> monthlyPriceTrend = new HashMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (SupplierProductPriceHistory history : priceHistories) {
            String rawDate = history.getEffectiveDate();
            if (rawDate == null || rawDate.isBlank()) {
                continue;
            }
            String month = rawDate.length() >= 7 ? rawDate.substring(0, 7) : rawDate;
            monthlyPriceTrend.merge(month, history.getQuotePrice() == null ? BigDecimal.ZERO : history.getQuotePrice(), BigDecimal::add);
        }
        List<Map<String, Object>> priceTrend = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 5; i >= 0; i--) {
            LocalDate month = currentMonth.minusMonths(i);
            String key = month.format(monthFormatter);
            priceTrend.add(Map.of(
                    "month", key,
                    "amount", monthlyPriceTrend.getOrDefault(key, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)
            ));
        }

        List<Map<String, Object>> contractStatusStats = contracts.stream()
                .collect(Collectors.groupingBy(Contract::getContractStatus, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "name", entry.getKey() == null ? "未分类" : entry.getKey(),
                        "value", entry.getValue()
                ))
                .toList();

        return ApiResponse.ok(Map.of(
                "supplierTypeStats", supplierTypeStats,
                "cooperationTop", cooperationTop,
                "riskLevelStats", riskLevelStats,
                "priceTrend", priceTrend,
                "contractStatusStats", contractStatusStats
        ));
    }
}
