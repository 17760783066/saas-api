package com.example.demo.api.admin.entity;

import com.example.demo.common.model.Constants;
import com.example.demo.common.util.StringUtils;

public enum AdminPermission {

    // none
    NONE("", ""),

    /* 功能模块 */

    // info
    USER_EDIT("用户管理", Constants.LEVEL_PRIMARY), TAG_EDIT("TAG管理", Constants.LEVEL_PRIMARY),
    ARTICLE_EDIT("文章管理", Constants.LEVEL_PRIMARY), BANNER_EDIT("Banner管理", Constants.LEVEL_PRIMARY),

    // admin&role
    ROLE_LIST("管理组列表", Constants.LEVEL_IMPORTANT), ROLE_EDIT("管理组管理", Constants.LEVEL_IMPORTANT),

    ADMIN_LIST("管理员列表", Constants.LEVEL_WARNING), ADMIN_EDIT("编辑管理员", Constants.LEVEL_IMPORTANT),

    //merchant
    MERCHANT_LIST("商户列表",Constants.LEVEL_WARNING),RENEW_LIST("开户/续费账单",Constants.LEVEL_WARNING),
    RENEW_EDIT("续费审核",Constants.LEVEL_IMPORTANT),PRESENT_EDIT("赠送审核",Constants.LEVEL_IMPORTANT),
    //product
    PRODUCT_EDIT("产品管理",Constants.LEVEL_IMPORTANT),PRODUCT_LIST("产品列表",Constants.LEVEL_WARNING),
    SETTING("首页管理",Constants.LEVEL_IMPORTANT);
    private String val;
    private String level;

    AdminPermission(String val, String level) {
        this.val = val;
        this.level = level;
    }

    public static AdminPermission nameOf(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (AdminPermission p : AdminPermission.values()) {
            if (name.equals(p.name())) {
                return p;
            }
        }
        return null;
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
