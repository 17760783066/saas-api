package com.example.demo.api.img.model;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.demo.common.converter.StringArrayConverter;

@Entity
@Table
public class ArtisanCaseImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Convert(converter = StringArrayConverter.class)
    private List<String> imgs;
    private Byte status;
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
    public List<String> getImgs() {
        return imgs;
    }
    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    
}
