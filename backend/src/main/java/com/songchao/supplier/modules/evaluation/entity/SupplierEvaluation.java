package com.songchao.supplier.modules.evaluation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("sup_supplier_evaluation")
public class SupplierEvaluation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long supplierId;
    private Long projectId;
    private Long contractId;
    private BigDecimal qualityScore;
    private BigDecimal deliveryScore;
    private BigDecimal serviceScore;
    private BigDecimal priceScore;
    private BigDecimal complianceScore;
    private BigDecimal totalScore;
    private String rating;
    private Long evaluationUserId;
    private String evaluationTime;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public BigDecimal getQualityScore() { return qualityScore; }
    public void setQualityScore(BigDecimal qualityScore) { this.qualityScore = qualityScore; }
    public BigDecimal getDeliveryScore() { return deliveryScore; }
    public void setDeliveryScore(BigDecimal deliveryScore) { this.deliveryScore = deliveryScore; }
    public BigDecimal getServiceScore() { return serviceScore; }
    public void setServiceScore(BigDecimal serviceScore) { this.serviceScore = serviceScore; }
    public BigDecimal getPriceScore() { return priceScore; }
    public void setPriceScore(BigDecimal priceScore) { this.priceScore = priceScore; }
    public BigDecimal getComplianceScore() { return complianceScore; }
    public void setComplianceScore(BigDecimal complianceScore) { this.complianceScore = complianceScore; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public Long getEvaluationUserId() { return evaluationUserId; }
    public void setEvaluationUserId(Long evaluationUserId) { this.evaluationUserId = evaluationUserId; }
    public String getEvaluationTime() { return evaluationTime; }
    public void setEvaluationTime(String evaluationTime) { this.evaluationTime = evaluationTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

