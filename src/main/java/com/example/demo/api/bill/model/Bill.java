package com.example.demo.api.bill.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer amount;

    private Byte payType;

    private String payNumber;

    private Byte renferType;

    private Integer renderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public Byte getRenferType() {
        return renferType;
    }

    public void setRenferType(Byte renferType) {
        this.renferType = renferType;
    }

    public Integer getRenderId() {
        return renderId;
    }

    public void setRenderId(Integer renderId) {
        this.renderId = renderId;
    }

    
}
