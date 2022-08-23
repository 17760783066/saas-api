package com.example.demo.api.user.authority;

import com.example.demo.common.context.Contexts;
import com.example.demo.common.context.SessionWrapper;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.exception.SessionServiceException;

public class UserContexts extends Contexts {

    public static UserSessionWrapper getUserSessionWrapper() {
        SessionWrapper session = getWrapper();
        if (session == null) {
            return null;
        }
        if (!(session instanceof UserSessionWrapper)) {
            return null;
        }
        return (UserSessionWrapper) session;
    }

    public static Integer requestUserId() throws ServiceException {

        Integer userId = sessionUserId();
        if (userId == null) {
            throw new SessionServiceException();
        }
        return userId;
    }

    public static Integer sessionUserId() throws ServiceException {
        UserSessionWrapper wrapper = getUserSessionWrapper();
        if (wrapper == null) {
            return null;
        }
        return wrapper.getUser().getId();
    }

}
