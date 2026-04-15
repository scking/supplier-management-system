package com.songchao.supplier.modules.inquiry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sup_inquiry")
public class Inquiry {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String inquiryNo;
    private Long projectId;
    private Long reqId;
    private String inquiryTitle;
    private String status;
    private String deadlineTime;
    private Long recommendedSupplierId;
    private String recommendedReason;
    private Long createdBy;
    private Long createdDeptId;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInquiryNo() { return inquiryNo; }
    public void setInquiryNo(String inquiryNo) { this.inquiryNo = inquiryNo; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getReqId() { return reqId; }
    public void setReqId(Long reqId) { this.reqId = reqId; }
    public String getInquiryTitle() { return inquiryTitle; }
    public void setInquiryTitle(String inquiryTitle) { this.inquiryTitle = inquiryTitle; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadlineTime() { return deadlineTime; }
    public void setDeadlineTime(String deadlineTime) { this.deadlineTime = deadlineTime; }
    public Long getRecommendedSupplierId() { return recommendedSupplierId; }
    public void setRecommendedSupplierId(Long recommendedSupplierId) { this.recommendedSupplierId = recommendedSupplierId; }
    public String getRecommendedReason() { return recommendedReason; }
    public void setRecommendedReason(String recommendedReason) { this.recommendedReason = recommendedReason; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getCreatedDeptId() { return createdDeptId; }
    public void setCreatedDeptId(Long createdDeptId) { this.createdDeptId = createdDeptId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
