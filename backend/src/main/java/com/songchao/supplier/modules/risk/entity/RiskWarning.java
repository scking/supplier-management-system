package com.songchao.supplier.modules.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sup_risk_warning")
public class RiskWarning {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String warningType;
    private Long supplierId;
    private Long projectId;
    private Long bizId;
    private String warningLevel;
    private String warningTitle;
    private String warningContent;
    private String status;
    private String triggerTime;
    private String resolvedTime;
    private Long handlerId;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWarningType() { return warningType; }
    public void setWarningType(String warningType) { this.warningType = warningType; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public String getWarningLevel() { return warningLevel; }
    public void setWarningLevel(String warningLevel) { this.warningLevel = warningLevel; }
    public String getWarningTitle() { return warningTitle; }
    public void setWarningTitle(String warningTitle) { this.warningTitle = warningTitle; }
    public String getWarningContent() { return warningContent; }
    public void setWarningContent(String warningContent) { this.warningContent = warningContent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTriggerTime() { return triggerTime; }
    public void setTriggerTime(String triggerTime) { this.triggerTime = triggerTime; }
    public String getResolvedTime() { return resolvedTime; }
    public void setResolvedTime(String resolvedTime) { this.resolvedTime = resolvedTime; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

