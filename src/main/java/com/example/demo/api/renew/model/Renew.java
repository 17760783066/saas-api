package com.example.demo.api.renew.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.merchant.model.Merchant;

@Entity
@Table
public class Renew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer merchantId;

    private Byte renewType;

    private String duration;

    private Integer adminId;

    private Byte payType;

    private Integer amount;

    private String payImg;

    private String payNumber;

    private String remark;

    private Long createdAt;

    private Byte status;

    private Long auditAt;

    private Integer auditorId;

    private String reason;
    @Transient
    private Merchant merchant;

    @Transient
    private Admin admin;

    @Transient
    private Admin auditor;


    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Admin getAuditor() {
        return auditor;
    }

    public void setAuditor(Admin auditor) {
        this.auditor = auditor;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId == null ? 0 : merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Byte getRenewType() {
        return renewType;
    }

    public void setRenewType(Byte renewType) {
        this.renewType = renewType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getAdminId() {
        return adminId == null ? 0 : adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPayImg() {
        return payImg;
    }

    public void setPayImg(String payImg) {
        this.payImg = payImg;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getAuditAt() {
        return auditAt;
    }

    public void setAuditAt(Long auditAt) {
        this.auditAt = auditAt;
    }

    public Integer getAuditorId() {
        return auditorId == null ? 0 : auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

}
