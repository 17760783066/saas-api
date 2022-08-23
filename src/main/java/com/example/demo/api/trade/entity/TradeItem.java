package com.example.demo.api.trade.entity;

import com.example.demo.api.product.model.Product;

public class TradeItem {
    private Product product;
    private Integer number;
    private String productSno;
    private Integer id;
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public String getProductSno() {
        return productSno;
    }
    public void setProductSno(String productSno) {
        this.productSno = productSno;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


}
