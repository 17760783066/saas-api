package com.example.demo.api.user.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class UserCollectQo extends DataQueryObjectPage {

    private String sortPropertyName = "createdAt";

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Integer userId;

    @QueryField(type = QueryType.EQUAL, name = "collectType")
    private Byte collectType;

    @Override
    public String getSortPropertyName() {
        return sortPropertyName;
    }

    @Override
    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getCollectType() {
        return collectType;
    }

    public void setCollectType(Byte collectType) {
        this.collectType = collectType;
    }
}
