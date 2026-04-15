package com.songchao.supplier.modules.inquiry.dto;

import jakarta.validation.constraints.NotBlank;

public class InquirySaveRequest {
    @NotBlank
    private String inquiryNo;
    private Long projectId;
    private Long reqId;
    @NotBlank
    private String inquiryTitle;
    private String deadlineTime;
    private String remark;

    public String getInquiryNo() { return inquiryNo; }
    public void setInquiryNo(String inquiryNo) { this.inquiryNo = inquiryNo; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getReqId() { return reqId; }
    public void setReqId(Long reqId) { this.reqId = reqId; }
    public String getInquiryTitle() { return inquiryTitle; }
    public void setInquiryTitle(String inquiryTitle) { this.inquiryTitle = inquiryTitle; }
    public String getDeadlineTime() { return deadlineTime; }
    public void setDeadlineTime(String deadlineTime) { this.deadlineTime = deadlineTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

