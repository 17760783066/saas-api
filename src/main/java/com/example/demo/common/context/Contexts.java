package com.example.demo.common.context;

import com.example.demo.common.model.ErrorCode;
public class Contexts implements ErrorCode {

    public static void set(Context context) {
        SessionThreadLocal.getInstance().set(context);
    }

    public static Context get() {
        return SessionThreadLocal.getInstance().get();
    }

    protected static SessionWrapper getWrapper() {
        return get().getSession();
    }

}
