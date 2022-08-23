package com.example.demo.api.product.model;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.common.converter.ParamArrayConverter;
import com.example.demo.common.converter.SpecArrayConverter;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer brandId;
    private Integer categoryId;
    private String content;
    private Byte status;
    private Long createdAt;
    private Integer merchantId;
    private Integer priority;
    private Long putAt;
    @Transient
    private ProductCategory productCategory;
    @Transient
    private Brand brand;
    @Transient
    private Merchant merchant;

    @Convert(converter = ParamArrayConverter.class)
    private List<Param> params;
    @Convert(converter = SpecArrayConverter.class)
    private List<Spec> specs;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getBrandId() {
        return brandId;
    }
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Long getPutAt() {
        return putAt;
    }
    public void setPutAt(Long putAt) {
        this.putAt = putAt;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Merchant getMerchant() {
        return merchant;
    }
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    public List<Param> getParams() {
        return params;
    }
    public void setParams(List<Param> params) {
        this.params = params;
    }
    public List<Spec> getSpecs() {
        return specs;
    }
    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public ProductCategory getProductCategory() {
        return productCategory;
    }
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
    
}
