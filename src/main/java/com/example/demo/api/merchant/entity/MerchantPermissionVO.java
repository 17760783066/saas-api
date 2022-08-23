package com.example.demo.api.merchant.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.common.model.Permission;
import com.example.demo.common.util.CollectionUtils;

public class MerchantPermissionVO {
    private static List<Permission> list = null;

    public static List<Permission> initAdmPermissions() {
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
            for (MerchantPermission p : MerchantPermission.values()) {
                list.add(new Permission(p.name(), p.getVal(), p.getLevel()));
            }
        }
        return list;
    }

    public static List<Permission> initAdmPermissionsByPs(List<String> ps) {
        List<Permission> list = initAdmPermissions();
        List<Permission> result = new ArrayList<>();
        if (ps.size() > 0) {
            for (String s : ps) {
                for (Permission p : list) {
                    if (s.equals(p.getKey()))
                        result.add(p);
                }
            }
        }
        return result;
    }
}
