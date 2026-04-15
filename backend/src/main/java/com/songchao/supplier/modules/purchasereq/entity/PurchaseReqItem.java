package com.songchao.supplier.modules.purchasereq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("sup_purchase_req_item")
public class PurchaseReqItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reqId;
    private Long productId;
    private String productName;
    private String specification;
    private String unit;
    private BigDecimal qty;
    private BigDecimal budgetPrice;
    private BigDecimal budgetAmount;
    private String technicalRequirements;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
