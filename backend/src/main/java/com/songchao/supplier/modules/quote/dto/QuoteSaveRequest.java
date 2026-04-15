package com.songchao.supplier.modules.quote.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class QuoteSaveRequest {
    @NotNull
    private Long inquiryId;
    @NotNull
    private Long supplierId;
    private String supplierName;
    private BigDecimal totalAmount;
    private Integer deliveryCycleDays;
    private Integer warrantyMonths;
    private String remark;
    private List<QuoteItemSaveRequest> items;

    public Long getInquiryId() { return inquiryId; }
    public void setInquiryId(Long inquiryId) { this.inquiryId = inquiryId; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getDeliveryCycleDays() { return deliveryCycleDays; }
    public void setDeliveryCycleDays(Integer deliveryCycleDays) { this.deliveryCycleDays = deliveryCycleDays; }
    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public List<QuoteItemSaveRequest> getItems() { return items; }
    public void setItems(List<QuoteItemSaveRequest> items) { this.items = items; }
}
