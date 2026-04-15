package com.songchao.supplier.modules.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sup_performance")
public class Performance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long contractId;
    private Long projectId;
    private Long supplierId;
    private String performanceType;
    private String nodeName;
    private String plannedDate;
    private String actualDate;
    private String status;
    private Long responsibleUserId;
    private String description;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getPerformanceType() { return performanceType; }
    public void setPerformanceType(String performanceType) { this.performanceType = performanceType; }
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    public String getPlannedDate() { return plannedDate; }
    public void setPlannedDate(String plannedDate) { this.plannedDate = plannedDate; }
    public String getActualDate() { return actualDate; }
    public void setActualDate(String actualDate) { this.actualDate = actualDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getResponsibleUserId() { return responsibleUserId; }
    public void setResponsibleUserId(Long responsibleUserId) { this.responsibleUserId = responsibleUserId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

