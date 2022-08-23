package com.example.demo.api.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.product.model.Product;
import com.example.demo.api.user.authority.UserContexts;

@Entity
@Table
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer number;
    private Integer merchantId;
    private Integer productId;
    private Long createdAt;
    private Integer userId;
    private Byte status;
    private String productSno;
    @Transient
    private Merchant merchant;
    @Transient
    private Product product;

    public void copy(Shopping shopping, String productSno) {
        this.setUserId(UserContexts.requestUserId());
        this.setNumber(shopping.getNumber());
        this.setMerchantId(shopping.getMerchantId());
        this.setProductId(shopping.getProductId());
        this.setProductSno(productSno);
        this.setCreatedAt(System.currentTimeMillis());
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getProductSno() {
        return productSno;
    }

    public void setProductSno(String productSno) {
        this.productSno = productSno;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
