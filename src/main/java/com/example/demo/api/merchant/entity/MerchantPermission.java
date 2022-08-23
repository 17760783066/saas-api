package com.example.demo.api.merchant.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.common.model.Constants;

public enum MerchantPermission {
    NONE("",""),

    ROLE_LIST("管理组列表", Constants.LEVEL_IMPORTANT), ROLE_EDIT("管理组管理", Constants.LEVEL_IMPORTANT),

    ADMIN_LIST("管理员列表", Constants.LEVEL_WARNING), ADMIN_EDIT("编辑管理员", Constants.LEVEL_IMPORTANT),
    RENEW_LIST("账单管理",Constants.LEVEL_IMPORTANT),PRODUCT_EDIT("产品管理",Constants.LEVEL_IMPORTANT),PRODUCT_LIST("产品列表",Constants.LEVEL_WARNING),
    SETTING("首页管理",Constants.LEVEL_IMPORTANT)
    ;

    private String val;
    private String level;
    private MerchantPermission(String val, String level) {
        this.val = val;
        this.level = level;
    }
    public static List<String> getSuperPermission() {
        List<String> ret = new ArrayList<>(4);
        ret.add(ROLE_EDIT.name());
        ret.add(ROLE_LIST.name());
        ret.add(ADMIN_LIST.name());
        ret.add(ADMIN_EDIT.name());
        return ret;
    }
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    
}
