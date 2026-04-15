package com.songchao.supplier.modules.supplier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupplierSaveRequest {
    @NotBlank
    @Size(max = 64)
    private String supplierCode;
    @NotBlank
    @Size(max = 255)
    private String supplierName;
    @NotBlank
    private String supplierType;
    @NotBlank
    private String creditCode;
    private String legalPerson;
    @NotBlank
    private String contactPerson;
    @NotBlank
    private String contactPhone;
    private String contactEmail;
    private String province;
    private String city;
    private String address;
    private String bankName;
    private String bankAccount;
    private String taxpayerType;
    private String cooperationLevel;
    private String remark;

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSupplierType() { return supplierType; }
    public void setSupplierType(String supplierType) { this.supplierType = supplierType; }
    public String getCreditCode() { return creditCode; }
    public void setCreditCode(String creditCode) { this.creditCode = creditCode; }
    public String getLegalPerson() { return legalPerson; }
    public void setLegalPerson(String legalPerson) { this.legalPerson = legalPerson; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
    public String getTaxpayerType() { return taxpayerType; }
    public void setTaxpayerType(String taxpayerType) { this.taxpayerType = taxpayerType; }
    public String getCooperationLevel() { return cooperationLevel; }
    public void setCooperationLevel(String cooperationLevel) { this.cooperationLevel = cooperationLevel; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
