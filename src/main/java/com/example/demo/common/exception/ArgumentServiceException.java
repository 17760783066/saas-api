package com.example.demo.common.exception;


import com.example.demo.common.model.ErrorCode;

import java.util.HashMap;

public class ArgumentServiceException extends ServiceException {
    private static final long serialVersionUID = 8659114628660349452L;

    public ArgumentServiceException(String key, java.io.Serializable value) {
        super(ErrorCode.ERR_ILLEGAL_ARGUMENT);
        java.util.Map<String, Object> errorData = new HashMap<String, Object>(2);
        errorData.put("key", key);
        errorData.put("value", value);
        this.setErrorData(errorData);
    }

    public ArgumentServiceException(String key) {
        this(key, null);
    }
}
