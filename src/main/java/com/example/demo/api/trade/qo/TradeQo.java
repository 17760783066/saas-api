package com.example.demo.api.trade.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryBetween;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class TradeQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "type",zero2Null = true)
    private Byte type;
    @QueryField(type = QueryType.EQUAL, name = "merchantId",zero2Null = true)
    private Integer merchantId;
    @QueryField(type = QueryType.EQUAL, name = "userId",zero2Null = true)
    private Integer userId;
    @QueryField(type = QueryType.EQUAL, name = "orderNumber",zero2Null = true)
    private String orderNumber;
    @QueryField(type = QueryType.GREATEROR_THAN, name = "createdAt",zero2Null = true)
    private Long validTime;

    @QueryField(type = QueryType.BEWTEEN, name = "createdAt",zero2Null = true)
    private QueryBetween<Integer> lastModified;

    public TradeQo() {
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public QueryBetween<Integer> getLastModified() {
        return lastModified;
    }

    public void setLastModified(QueryBetween<Integer> lastModified) {
        this.lastModified = lastModified;
    }

}
