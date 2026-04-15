package com.songchao.supplier.modules.supplierproduct.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SupplierProductPriceHistorySaveRequest {
    @NotNull
    private Long supplierProductId;
    private BigDecimal quotePrice;
    private BigDecimal taxRate;
    private Integer deliveryCycleDays;
    private Integer warrantyMonths;
    private String effectiveDate;
    private String expireDate;
    private String sourceType;
    private Long sourceId;
    private String remark;

    public Long getSupplierProductId() { return supplierProductId; }
    public void setSupplierProductId(Long supplierProductId) { this.supplierProductId = supplierProductId; }
    public BigDecimal getQuotePrice() { return quotePrice; }
    public void setQuotePrice(BigDecimal quotePrice) { this.quotePrice = quotePrice; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public Integer getDeliveryCycleDays() { return deliveryCycleDays; }
    public void setDeliveryCycleDays(Integer deliveryCycleDays) { this.deliveryCycleDays = deliveryCycleDays; }
    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getExpireDate() { return expireDate; }
    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
