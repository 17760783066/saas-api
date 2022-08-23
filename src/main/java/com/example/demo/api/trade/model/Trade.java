package com.example.demo.api.trade.model;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.trade.converter.TradeItemConterter;
import com.example.demo.api.trade.entity.TradeItem;
import com.example.demo.api.user.converter.UserAddressConverter;
import com.example.demo.api.user.model.Address;
import com.example.demo.api.user.model.User;

@Entity
@Table
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Byte type;
    private Integer userId;
    private Integer merchantId;
    private String orderNumber;
    private Integer totalAmount;
    private Integer totalPrice;
    private Integer couponAmount;
    private Long createdAt;
    @Convert(converter = TradeItemConterter.class)
    private List<TradeItem> tradeItems;
    @Convert(converter = UserAddressConverter.class)
    private Address address;

    @Transient
    private Merchant merchant;
    @Transient
    private User user;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Byte getType() {
        return type;
    }
    public void setType(Byte type) {
        this.type = type;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public Integer getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Integer getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Integer getCouponAmount() {
        return couponAmount;
    }
    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }
    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Merchant getMerchant() {
        return merchant;
    }
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
