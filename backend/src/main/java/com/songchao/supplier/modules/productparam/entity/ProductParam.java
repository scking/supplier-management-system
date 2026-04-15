package com.songchao.supplier.modules.productparam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sup_product_param")
public class ProductParam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String paramName;
    private String paramValue;
    private String paramUnit;
    private Integer sortNo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

