package com.example.demo.api.merchant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MerchantAdminSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private Integer adminId;
    @Column
    private String token;
    @Column
    private Long signinAt;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getAdminId() {
        return adminId;
    }
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getSigninAt() {
        return signinAt;
    }
    public void setSigninAt(Long signinAt) {
        this.signinAt = signinAt;
    }
    public Long getExpireAt() {
        return expireAt;
    }
    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }
    @Column
    private Long expireAt;
}
