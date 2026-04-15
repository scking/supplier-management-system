package com.songchao.supplier.modules.contract.dto;

public class ContractReminderResponse {
    private Long contractId;
    private String contractNo;
    private String contractTitle;
    private String supplierName;
    private String expireDate;
    private String paymentStatus;
    private String reminderStatus;
    private Long daysToExpire;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractTitle() {
        return contractTitle;
    }

    public void setContractTitle(String contractTitle) {
        this.contractTitle = contractTitle;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(String reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public Long getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(Long daysToExpire) {
        this.daysToExpire = daysToExpire;
    }
}
