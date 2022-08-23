package com.example.demo.api.admin.authority;

import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.model.AdminSession;
import com.example.demo.common.context.SessionWrapper;

public class AdminSessionWrapper implements SessionWrapper {

    private Admin admin;
    private AdminSession adminSession;

    public AdminSessionWrapper() {
    }

    public AdminSessionWrapper(Admin admin, AdminSession adminSession) {
        this.admin = admin;
        this.adminSession = adminSession;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AdminSession getAdminSession() {
        return adminSession;
    }

    public void setAdminSession(AdminSession adminSession) {
        this.adminSession = adminSession;
    }
}