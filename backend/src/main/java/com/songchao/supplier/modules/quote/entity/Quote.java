package com.songchao.supplier.modules.quote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("sup_quote")
public class Quote {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long inquiryId;
    private Long supplierId;
    private String supplierName;
    private String quoteStatus;
    private BigDecimal totalAmount;
    private Integer deliveryCycleDays;
    private Integer warrantyMonths;
    private String quoteTime;
    private BigDecimal priceScore;
    private BigDecimal deliveryScore;
    private BigDecimal warrantyScore;
    private BigDecimal serviceScore;
    private BigDecimal totalScore;
    private Integer compareRank;
    private Long fileId;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInquiryId() { return inquiryId; }
    public void setInquiryId(Long inquiryId) { this.inquiryId = inquiryId; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getQuoteStatus() { return quoteStatus; }
    public void setQuoteStatus(String quoteStatus) { this.quoteStatus = quoteStatus; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getDeliveryCycleDays() { return deliveryCycleDays; }
    public void setDeliveryCycleDays(Integer deliveryCycleDays) { this.deliveryCycleDays = deliveryCycleDays; }
    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }
    public String getQuoteTime() { return quoteTime; }
    public void setQuoteTime(String quoteTime) { this.quoteTime = quoteTime; }
    public BigDecimal getPriceScore() { return priceScore; }
    public void setPriceScore(BigDecimal priceScore) { this.priceScore = priceScore; }
    public BigDecimal getDeliveryScore() { return deliveryScore; }
    public void setDeliveryScore(BigDecimal deliveryScore) { this.deliveryScore = deliveryScore; }
    public BigDecimal getWarrantyScore() { return warrantyScore; }
    public void setWarrantyScore(BigDecimal warrantyScore) { this.warrantyScore = warrantyScore; }
    public BigDecimal getServiceScore() { return serviceScore; }
    public void setServiceScore(BigDecimal serviceScore) { this.serviceScore = serviceScore; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public Integer getCompareRank() { return compareRank; }
    public void setCompareRank(Integer compareRank) { this.compareRank = compareRank; }
    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
