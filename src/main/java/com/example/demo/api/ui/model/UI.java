package com.example.demo.api.ui.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.demo.api.ui.converter.UIComponentArrayConverter;
import com.example.demo.api.ui.entity.UIComponent;

@Entity
@Table
public class UI {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String title;


    @Column
    @Convert(converter = UIComponentArrayConverter.class)
    private List<UIComponent> components;

    @Column(updatable = false)
    private Long createdAt;

    @Column
    private Byte isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UIComponent> getComponents() {
        return components;
    }

    public void setComponents(List<UIComponent> components) {
        this.components = components;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    
}
