package com.example.demo.api.merchant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table
public class MerchantAdmin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private String mobile;

    @Column
    @JSONField(serialize = false)
    private String password;

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }


    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column
    private Integer merchantRoleId; 

    private Integer merchantId;

    @Column
    private String img;

    @Column
    private Byte status;

    @Column(updatable = false)
    private Long createdAt;

    @Column
    private Long signinAt;

    @Transient
    private MerchantRole merchantRole;

    @Transient
    private Merchant merchant;

    @Transient
    private String newpassword;

    public static Merchant getSimpleInstance(Merchant admin) {
        Merchant a = new Merchant();

        a.setName(admin.getName());
        return a;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMerchantRoleId() {
        return merchantRoleId;
    }

    public void setMerchantRoleId(Integer merchantRoleId) {
        this.merchantRoleId = merchantRoleId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Long getSigninAt() {
        return signinAt;
    }

    public void setSigninAt(Long signinAt) {
        this.signinAt = signinAt;
    }

    public MerchantRole getMerchantRole() {
        return merchantRole;
    }

    public void setMerchantRole(MerchantRole merchantRole) {
        this.merchantRole = merchantRole;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
