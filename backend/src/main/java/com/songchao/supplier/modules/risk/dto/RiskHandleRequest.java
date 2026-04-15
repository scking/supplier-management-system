package com.songchao.supplier.modules.risk.dto;

public class RiskHandleRequest {
    private Long handlerId;
    private String remark;

    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

