package com.example.demo.api.user.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectSort;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class ShoppingQo extends DataQueryObjectSort {
    protected String sortPropertyName = "id";
    protected boolean sortAscending = false;

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Integer userId;
    @QueryField(type = QueryType.EQUAL, name = "merchantId")
    private Integer merchantId;


    @Override
    public String getSortPropertyName() {
        return sortPropertyName;
    }

    @Override
    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    @Override
    public boolean isSortAscending() {
        return sortAscending;
    }

    @Override
    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
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

}
