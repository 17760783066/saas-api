package com.example.demo.api.admin.authority;

import java.util.List;

import com.example.demo.api.admin.model.Admin;
import com.example.demo.common.context.Contexts;
import com.example.demo.common.context.SessionWrapper;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;

public class AdminContexts extends Contexts {

    public static final byte COLLECT_TYPE_ARTICLE = 0;

    public static AdminSessionWrapper getAdminSessionWrapper() {
        SessionWrapper session = getWrapper();
        if (session == null) {
            return null;
        }
        if (!(session instanceof AdminSessionWrapper)) {
            return null;
        }
        return (AdminSessionWrapper) session;
    }

    public static AdminSessionWrapper requireAdminSessionWrapper() throws ServiceException {
        AdminSessionWrapper session = getAdminSessionWrapper();
        if (session == null) {
            throw new ServiceException(ERR_SESSION_EXPIRES);
        }
        return session;
    }


    public static Integer sessionAdminId() throws ServiceException {
        AdminSessionWrapper wrapper = getAdminSessionWrapper();
        if (wrapper == null) {
            return null;
        }
        return wrapper.getAdmin().getId();
    }

    public static Integer requestAdminId() throws ServiceException {

        Integer adminId = sessionAdminId();
        if (adminId == null) {
            throw new SessionServiceException();
        }
        return adminId;
    }

    private static List<String> requestAdminPermissions() throws ServiceException {
        Admin admin = requireAdminSessionWrapper().getAdmin();
        return admin.getRole().getPermissions();
    }

    public static boolean checkAdminPermission(String permission) {
        return requestAdminPermissions().contains(permission);
    }

}
