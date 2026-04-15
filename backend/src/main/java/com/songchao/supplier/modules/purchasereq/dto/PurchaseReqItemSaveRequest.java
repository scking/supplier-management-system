package com.songchao.supplier.modules.purchasereq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PurchaseReqItemSaveRequest {
    @NotNull
    private Long reqId;
    private Long productId;
    @NotBlank
    private String productName;
    private String specification;
    private String unit;
    private BigDecimal qty;
    private BigDecimal budgetPrice;
    private BigDecimal budgetAmount;
    private String technicalRequirements;
    private String remark;

    public Long getReqId() { return reqId; }
    public void setReqId(Long reqId) { this.reqId = reqId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getQty() { return qty; }
    public void setQty(BigDecimal qty) { this.qty = qty; }
    public BigDecimal getBudgetPrice() { return budgetPrice; }
    public void setBudgetPrice(BigDecimal budgetPrice) { this.budgetPrice = budgetPrice; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
    public String getTechnicalRequirements() { return technicalRequirements; }
    public void setTechnicalRequirements(String technicalRequirements) { this.technicalRequirements = technicalRequirements; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
