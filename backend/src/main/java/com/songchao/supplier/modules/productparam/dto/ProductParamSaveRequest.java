package com.songchao.supplier.modules.productparam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductParamSaveRequest {
    @NotNull
    private Long productId;
    @NotBlank
    private String paramName;
    private String paramValue;
    private String paramUnit;
    private Integer sortNo;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getParamName() { return paramName; }
    public void setParamName(String paramName) { this.paramName = paramName; }
    public String getParamValue() { return paramValue; }
    public void setParamValue(String paramValue) { this.paramValue = paramValue; }
    public String getParamUnit() { return paramUnit; }
    public void setParamUnit(String paramUnit) { this.paramUnit = paramUnit; }
    public Integer getSortNo() { return sortNo; }
    public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }
}

