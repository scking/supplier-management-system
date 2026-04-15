package com.songchao.supplier.modules.purchasereq.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class PurchaseReqSaveRequest {
    @NotBlank
    private String reqNo;
    private Long projectId;
    private String projectName;
    @NotBlank
    private String reqTitle;
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private String requiredDate;
    private BigDecimal totalAmount;
    private String remark;

    public String getReqNo() { return reqNo; }
    public void setReqNo(String reqNo) { this.reqNo = reqNo; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getReqTitle() { return reqTitle; }
    public void setReqTitle(String reqTitle) { this.reqTitle = reqTitle; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getRequiredDate() { return requiredDate; }
    public void setRequiredDate(String requiredDate) { this.requiredDate = requiredDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

