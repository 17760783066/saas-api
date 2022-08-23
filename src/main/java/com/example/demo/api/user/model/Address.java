package com.example.demo.api.user.model;
import javax.persistence.*;

import com.example.demo.common.converter.LocationConverter;
import com.example.demo.common.model.Location;


@Entity
@Table

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    private Integer userId;
    private String name;
    private String mobile;
    private Byte isDefault;
    private Long createdAt;
    private Long updateAt;
    @Convert(converter = LocationConverter.class)
    private Location location;

    public void copy(Address address) {
        this.setUpdateAt(System.currentTimeMillis());
        this.setIsDefault(address.getIsDefault());
        this.setMobile(address.getMobile());
        this.setName(address.getName());
        this.setLocation(address.getLocation());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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


    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

}
