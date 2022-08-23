package com.example.demo.common.controller.auth;

import com.example.demo.common.context.Context;
import com.example.demo.common.controller.BaseInterceptor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CommonInterceptor extends BaseInterceptor {

    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response, HandlerMethod
            handlerMethod, Context context, AdminType[] adminTypes) {

        return true;
    }
}
