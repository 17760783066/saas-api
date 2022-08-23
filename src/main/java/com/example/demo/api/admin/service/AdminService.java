package com.example.demo.api.admin.service;

import com.example.demo.api.admin.authority.AdminSessionWrapper;
import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.model.AdminSession;
import com.example.demo.api.admin.model.Role;
import com.example.demo.api.admin.qo.AdminQo;
import com.example.demo.api.admin.qo.AdminSessionQo;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ValCode;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdminService {

    // role

    void saveRole(Role role);

    void removeRole(int id);

    Role role(int id);

    List<Role> roles(boolean init);

    // admin

    List<Admin> admins(AdminQo qo);

    void saveAdmin(Admin admin) throws ServiceException;

    void adminStatus(int id, byte status) throws ServiceException;

    Admin admin(int id, boolean init);

    void removeAdmin(int id) throws ServiceException;

    void updatePassword(String password, String repeatPassword, String oldPassword) throws ServiceException;

    Admin findByAccount(ValCode valCode);

    void resetPassword(ValCode valCode, String password);

    Map<Integer, Admin> findByIdIn(Collection<Integer> ids);

    //session
    Page<AdminSession> adminSessions(AdminSessionQo qo);

    AdminSessionWrapper findByToken(String token);

    // signin

    Map<String, Object> signin(Admin admin, ValCode valCode);

    Map<String, Object> profile();

    //tool
    void getPassword(String password);
}
