package com.songchao.supplier.modules.supplierproduct.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SupplierProductSaveRequest {
    @NotNull
    private Long supplierId;
    @NotNull
    private Long productId;
    private String supplyStatus;
    private BigDecimal quotePrice;
    private String currency;
    private String priceEffectiveDate;
    private Integer deliveryCycleDays;
    private Integer warrantyMonths;
    private BigDecimal minOrderQty;
    private BigDecimal taxRate;
    private String priceRemark;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSupplyStatus() { return supplyStatus; }
    public void setSupplyStatus(String supplyStatus) { this.supplyStatus = supplyStatus; }
    public BigDecimal getQuotePrice() { return quotePrice; }
    public void setQuotePrice(BigDecimal quotePrice) { this.quotePrice = quotePrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPriceEffectiveDate() { return priceEffectiveDate; }
    public void setPriceEffectiveDate(String priceEffectiveDate) { this.priceEffectiveDate = priceEffectiveDate; }
    public Integer getDeliveryCycleDays() { return deliveryCycleDays; }
    public void setDeliveryCycleDays(Integer deliveryCycleDays) { this.deliveryCycleDays = deliveryCycleDays; }
    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }
    public BigDecimal getMinOrderQty() { return minOrderQty; }
    public void setMinOrderQty(BigDecimal minOrderQty) { this.minOrderQty = minOrderQty; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public String getPriceRemark() { return priceRemark; }
    public void setPriceRemark(String priceRemark) { this.priceRemark = priceRemark; }
}

