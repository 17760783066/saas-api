package com.example.demo.api.product.model;

import java.util.List;

public class Spec {

    private List<String> imgs;

    private List<Param> params;
    private Integer repertory;
    private Integer linePrice;
    private Integer price;
    private String sno;
    
    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Integer getLinePrice() {
        return linePrice;
    }

    public void setLinePrice(Integer linePrice) {
        this.linePrice = linePrice;
    }

    

    
}
