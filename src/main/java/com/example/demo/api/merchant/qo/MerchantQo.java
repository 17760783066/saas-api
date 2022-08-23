package com.example.demo.api.merchant.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryBetween;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class MerchantQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.FULL_LIKE, name = { "name", "mobile" })
    private String nameOrMobile;

    @QueryField(type = QueryType.FULL_LIKE, name = "name")
    private String name;

    @QueryField(type = QueryType.EQUAL, name = "mobile")
    private String mobile;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.LESS_THAN, name = "validThru")
    private QueryBetween<Long> expireBefore;

    public MerchantQo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = zero2Null(status);
    }

    public String getNameOrMobile() {
        return nameOrMobile;
    }

    public void setNameOrMobile(String nameOrMobile) {
        this.nameOrMobile = nameOrMobile;
    }

    public QueryBetween<Long> getExpireBefore() {
        return expireBefore;
    }

    public void setExpireBefore(QueryBetween<Long> expireBefore) {
        this.expireBefore = expireBefore;
    }

}
