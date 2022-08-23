package com.example.demo.api.admin.qo;


import com.example.demo.common.reposiotry.support.DataQueryObjectSort;
import com.example.demo.common.reposiotry.support.QueryField;
import com.example.demo.common.reposiotry.support.QueryType;

public class AdminQo extends DataQueryObjectSort {

    @QueryField(type = QueryType.FULL_LIKE, name = {"name", "mobile"})
    private String nameOrMobile;

    public AdminQo() {
    }

    public String getNameOrMobile() {
        return nameOrMobile;
    }

    public void setNameOrMobile(String nameOrMobile) {
        this.nameOrMobile = nameOrMobile;
    }
}
