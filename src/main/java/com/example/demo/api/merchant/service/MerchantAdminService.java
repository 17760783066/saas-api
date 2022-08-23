package com.example.demo.api.merchant.service;

import java.util.List;
import java.util.Map;

import com.example.demo.api.admin.model.Role;
import com.example.demo.api.merchant.authority.MerchantAdminSessionWrapper;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.model.MerchantAdminSession;
import com.example.demo.api.merchant.model.MerchantRole;
import com.example.demo.api.merchant.qo.MerchantAdminQo;
import com.example.demo.api.merchant.qo.MerchantAdminSessionQo;
import com.example.demo.api.merchant.qo.MerchantRoleQo;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.model.ValCode;

import org.springframework.data.domain.Page;

public interface MerchantAdminService {
    void saveRole(MerchantRole role, AdminType type);

    void saveMerchantAdmin(MerchantAdmin merchantAdmin,AdminType adminType);

    Map<String, Object> signin(MerchantAdmin merchantAdmin, ValCode valCode);

    MerchantRole merchantRole(int id);

    MerchantAdminSessionWrapper findByToken(String token);

    Map<String, Object> profile();

    MerchantAdmin merchantAdmin(int id, boolean init);

    List<MerchantAdmin> admins(MerchantAdminQo qo, AdminType adminType);

    List<MerchantRole> roles( MerchantRoleQo qo, AdminType adminType);

    Page<MerchantAdminSession> merchantAdminSessions(MerchantAdminSessionQo qo);



}
