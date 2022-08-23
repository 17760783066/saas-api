package com.example.demo.common.controller;

import com.example.demo.common.L;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.resources.LocaleBundles;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionInterceptor {

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleError(HttpServletRequest request, HandlerMethod handlerMethod, Throwable ex) {
        L.error(ex);
        if (!(ex instanceof ServiceException)) {
            ApiLog.log(request, null);
        }

        ServiceException se;
        if (ex instanceof ServiceException) {
            se = (ServiceException) ex;
        } else {
            L.error(ex);
            se = new ServiceException(ErrorCode.ERR_UNKNOWN_ERROR);
        }
        int errorCode = se.getErrorCode();
        String errorMsg = LocaleBundles.getWithArrayParams(null, "err." + errorCode, se.getErrorParams());
        Map<String, Object> error = new HashMap<>();
        error.put("errcode", errorCode);
        error.put("errmsg", errorMsg);
        if (se.getErrorData() != null) {
            error.put("errdata", se.getErrorData());
        }
        return new ModelAndView(new JsonView(error));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Throwable ex) {
        return new ModelAndView(new NotFoundView());
    }
}
