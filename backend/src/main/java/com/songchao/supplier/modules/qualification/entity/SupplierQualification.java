package com.songchao.supplier.modules.qualification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sup_supplier_qualification")
public class SupplierQualification {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long supplierId;
    private String qualificationType;
    private String qualificationName;
    private String qualificationNo;
    private String issuedBy;
    private String issueDate;
    private String expireDate;
    private String status;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getQualificationType() { return qualificationType; }
    public void setQualificationType(String qualificationType) { this.qualificationType = qualificationType; }
    public String getQualificationName() { return qualificationName; }
    public void setQualificationName(String qualificationName) { this.qualificationName = qualificationName; }
    public String getQualificationNo() { return qualificationNo; }
    public void setQualificationNo(String qualificationNo) { this.qualificationNo = qualificationNo; }
    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }
    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public String getExpireDate() { return expireDate; }
    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

