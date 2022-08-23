package com.example.demo.common.controller.auth;

import com.example.demo.api.admin.authority.AdminSessionWrapper;
import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.admin.service.AdminService;
import com.example.demo.common.context.Context;
import com.example.demo.common.controller.BaseInterceptor;
import com.example.demo.common.controller.WebUtils;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;
import com.example.demo.common.model.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class AdmInterceptor extends BaseInterceptor implements ErrorCode {

    @Autowired
    private AdminService adminService;

    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response, HandlerMethod
            handlerMethod, Context context, AdminType[] adminTypes) throws Exception {

        for (AdminType type : adminTypes) {
            if (type != AdminType.ADMIN) {
                throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
            }
        }

        String token = WebUtils.getHeader(request, KEY_ADMIN_TOKEN);
        if (token == null) {
            throw new SessionServiceException();
        }

        //先通过缓存验证session是否过期
        AdminSessionWrapper wrapper = adminService.findByToken(token);
        if (wrapper == null) {
            throw new SessionServiceException();
        }
        boolean authorized = false;

        RequiredPermission requiresAuth = getRequestPermission(handlerMethod);

        authorized = requiresAuth == null;

        if (requiresAuth != null) {
            //权限
            AdminPermission[] permissions = requiresAuth.value();
            if (permissions.length == 0) {
                authorized = true;
            }
            if (Arrays.asList(permissions).contains(AdminPermission.NONE)) {
                authorized = true;
            }

            //多个权限满足其一即可
            Admin admin = wrapper.getAdmin();
            List<String> ps = admin.getRole().getPermissions();
            for (AdminPermission permission : permissions) {
                if (ps.contains(permission.name())) {
                    authorized = true;
                    break;
                }
            }
        }

        if (!authorized) {
            throw new ServiceException(ERR_PERMISSION_DENIED);
        }

        context.setSession(wrapper);

        return true;
    }
}
