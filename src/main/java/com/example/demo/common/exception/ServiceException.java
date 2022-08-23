package com.example.demo.common.exception;

import java.util.Map;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -3121925981104998575L;
    private int errorCode;
    private Object[] errorParams;
    private Map<?, ?> errorData;

    public ServiceException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, Object... errorParams) {
        super();
        this.errorCode = errorCode;
        this.errorParams = errorParams;
    }

    public ServiceException(int errorCode, Object[] errorParams, Map<?, ?> errorData) {
        super();
        this.errorCode = errorCode;
        this.errorParams = errorParams;
        this.errorData = errorData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object[] getErrorParams() {
        return errorParams;
    }

    public Map<?, ?> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<?, ?> errorData) {
        this.errorData = errorData;
    }

}
