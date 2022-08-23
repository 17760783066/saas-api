package com.example.demo.common.context;


import com.example.demo.common.util.StringUtils;

public class Context {
    private SessionWrapper session;
    private String apiVersionString;
    private volatile ApiVersion apiVersion;

    public SessionWrapper getSession() {
        return session;
    }

    public void setSession(SessionWrapper session) {
        this.session = session;
    }

    public void setApiVersionString(String apiVersionString) {

        this.apiVersionString = apiVersionString;
    }

    public ApiVersion getApiVersion() {
        if (apiVersion != null) {
            return apiVersion;
        }
        if (StringUtils.isNotEmpty(apiVersionString)) {
            apiVersion = ApiVersion.parse(apiVersionString);
        } else {
            apiVersion = ApiVersion.LATEST;
        }
        return apiVersion;
    }
}
