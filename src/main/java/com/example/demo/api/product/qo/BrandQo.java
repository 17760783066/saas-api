package com.example.demo.api.product.qo;

import com.example.demo.common.reposiotry.support.DataQueryObjectPage;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class BrandQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.FULL_LIKE, name = "name", zero2Null = true)
    private String name;
    @QueryField(type = QueryType.EQUAL, name = "status", zero2Null = true)
    private Byte status;


    public BrandQo() {
    }

    public BrandQo(String name, Byte status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
