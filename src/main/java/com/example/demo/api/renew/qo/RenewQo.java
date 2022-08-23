package com.example.demo.api.renew.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryBetween;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class RenewQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.FULL_LIKE, name = { "payType", "amount" })
    private String payTypeOrAmount;

    @QueryField(type = QueryType.EQUAL, name = "status",zero2Null = true)
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "merchantId",zero2Null = true)
    private Integer merchantId;

    @QueryField(type = QueryType.EQUAL, name = "adminId")
    private Integer adminId;
    @QueryField(type = QueryType.BEWTEEN, name = "createdAt")
    private QueryBetween<Long> createdAt;



    @Override
    public String getSortPropertyName() {
        return sortPropertyName;
    }

    @Override
    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    public String getPayTypeOrAmount() {
        return payTypeOrAmount;
    }

    public void setPayTypeOrAmount(String payTypeOrAmount) {
        this.payTypeOrAmount = payTypeOrAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public QueryBetween<Long> getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(QueryBetween<Long> createdAt) {
        this.createdAt = createdAt;
    }

  
}
