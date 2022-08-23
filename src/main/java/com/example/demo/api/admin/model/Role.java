package com.example.demo.api.admin.model;


import com.example.demo.common.converter.StringArrayConverter;
import com.example.demo.common.model.Permission;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;


    @Convert(converter = StringArrayConverter.class)
    @Column
    private List<String> permissions;

    @Transient
    private List<Permission> pstr;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return this.permissions;
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
