package com.example.demo.common.controller.auth;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.merchant.authority.MerchantAdminSessionWrapper;
import com.example.demo.api.merchant.entity.MerchantPermission;
import com.example.demo.api.merchant.model.MerchantAdmin;
import com.example.demo.api.merchant.service.MerchantAdminService;
import com.example.demo.common.context.Context;
import com.example.demo.common.controller.BaseInterceptor;
import com.example.demo.common.controller.WebUtils;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;
import com.example.demo.common.model.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
@ControllerAdvice

public class MerchantInterceptor extends BaseInterceptor implements ErrorCode{

    @Autowired
    private MerchantAdminService merchantAdminService;

    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, AdminType[] adminTypes) throws Exception {
                for (AdminType type : adminTypes) {
                    if (type != AdminType.MERCHANT) {
                        throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
                    }
                }
        
                String token = WebUtils.getHeader(request, KEY_MERCHANTADMINSESSION_TOKEN);
                if (token == null) {
                    throw new SessionServiceException();
                }
        
                //先通过缓存验证session是否过期
                MerchantAdminSessionWrapper wrapper = merchantAdminService.findByToken(token);
                if (wrapper == null) {
                    throw new SessionServiceException();
                }
                boolean authorized = false;
        
                RequiredMerchantPermission requiresAuth   = handlerMethod.getMethodAnnotation(RequiredMerchantPermission.class);
                if (requiresAuth == null) {
                    requiresAuth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredMerchantPermission.class);
                }
        
                authorized = requiresAuth == null;
        
                if (requiresAuth != null) {
                    //权限
                    MerchantPermission[] permissions = requiresAuth.value();
                    if (permissions.length == 0) {
                        authorized = true;
                    }
                    if (Arrays.asList(permissions).contains(MerchantPermission.NONE)) {
                        authorized = true;
                    }
        
                    //多个权限满足其一即可
                    MerchantAdmin merchantAdmin = wrapper.getMerchantAdmin();
                    List<String> ps = merchantAdmin.getMerchantRole().getPermissions();
                    for (MerchantPermission permission : permissions) {
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
