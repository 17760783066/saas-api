package com.example.demo.api.merchant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.demo.api.merchant.converter.OemLogoConverter;
import com.example.demo.common.converter.LocationConverter;
import com.example.demo.common.model.Location;

@Entity
@Table
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String name;

    @Column(updatable = false)
    private Long createdAt;

    private String mobile;

    private String cover;

    @Convert(converter = LocationConverter.class)
    private Location location;

    private Long validThru;

    private Byte status;
    @Column
    @Convert(converter = OemLogoConverter.class)
    private Oem oem;

    private List<String> productCategorySequence;

    
    public List<String> getProductCategorySequence() {
        return productCategorySequence;
    }

    public void setProductCategorySequence(List<String> productCategorySequence) {
        this.productCategorySequence = productCategorySequence;
    }

    public Oem getOem() {
        return oem;
    }

    public void setOem(Oem oem) {
        this.oem = oem;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getValidThru() {
        return validThru;
    }

    public void setValidThru(Long validThru) {
        this.validThru = validThru;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

}
