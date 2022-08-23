package com.example.demo.api.merchant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.common.converter.StringArrayConverter;
import com.example.demo.common.model.Permission;

@Entity
@Table
public class MerchantRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;


    @Convert(converter = StringArrayConverter.class)
    @Column
    private List<String> permissions;

    private Integer merchantId;

    @Transient
    private Merchant merchant;

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Transient
    private List<Permission> pstr;

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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPstr() {
        return pstr;
    }

    public void setPstr(List<Permission> pstr) {
        this.pstr = pstr;
    }
}
