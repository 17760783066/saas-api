package com.example.demo.common.controller.auth;
import com.example.demo.api.user.authority.UserSessionWrapper;
import com.example.demo.api.user.service.UserService;
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



@ControllerAdvice
public class UserInterceptor extends BaseInterceptor implements ErrorCode {

    @Autowired
    private UserService userService;
    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, AdminType[] adminTypes) throws Exception {



        for (AdminType type : adminTypes) {
            if (type != AdminType.USER) {
                throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
            }
        }

        boolean touchable = false;
        Touchable touchableAnn = handlerMethod.getMethodAnnotation(Touchable.class);
        if (touchableAnn != null) {
            touchable = touchableAnn.value();
        }

        String token = WebUtils.getHeader(request, KEY_USER_TOKEN);
        if (!touchable && token == null) {
            throw new SessionServiceException();
        }

        // 先通过缓存验证session是否过期
        UserSessionWrapper wrapper = userService.findByToken(token);
        if (!touchable && wrapper == null) {
            throw new SessionServiceException();
        }
        context.setSession(wrapper);

        return true;
    }
}
