package com.example.demo.common.exception;


import com.example.demo.common.model.ErrorCode;

import java.util.HashMap;

public class DataNotFoundServiceException extends ServiceException {
    private static final long serialVersionUID = 7404151147635683478L;

    public DataNotFoundServiceException() {
        super(ErrorCode.ERR_DATA_NOT_FOUND);
    }

    public DataNotFoundServiceException(String message) {
        super(ErrorCode.ERR_DATA_NOT_FOUND);
        java.util.Map<String, Object> errorData = new HashMap<>();
        errorData.put("message", message);
        setErrorData(errorData);
    }

}
