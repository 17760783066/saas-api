package com.example.demo.common.controller;

import com.example.demo.common.context.Context;
import com.example.demo.common.context.Contexts;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public abstract class BaseInterceptor implements org.springframework.web.servlet.HandlerInterceptor, WebappKeys {

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (CrossDomainHandler.handle(request, response)) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 获取客户端信息(api版本号)
        Context context = new Context();
        context.setApiVersionString(WebUtils.getHeader(request, KEY_API_VERSION));
        Contexts.set(context);

        RequiredAdminType adminType = handlerMethod.getMethodAnnotation(RequiredAdminType.class);

        if (adminType == null) {
            adminType = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredAdminType.class);
            if (adminType == null) {
                return true;
            }
        }
        AdminType[] adminTypes = adminType.value();

        if (Arrays.asList(adminTypes).contains(AdminType.NONE)) {
            return true;
        }
        return authenticate(request, response, handlerMethod, context, adminTypes);
    }

    protected abstract boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, AdminType[] adminTypes) throws Exception;

 
    protected RequiredPermission getRequestPermission(HandlerMethod handlerMethod) {
        RequiredPermission requiredPermission = handlerMethod.getMethodAnnotation(RequiredPermission.class);
        if (requiredPermission == null) {
            requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
        }
        return requiredPermission;
    }
}
