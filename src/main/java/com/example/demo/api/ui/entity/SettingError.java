package com.example.demo.api.ui.entity;

import com.example.demo.common.model.ErrorCode;

public interface SettingError extends ErrorCode{
    
    public static final int ERR_TITLE_VALID_DENIED = 10001;
    public static final int ERR_UI_TYPE_VALID_DENIED = 10002;
    public static final int ERR_UI_COMPONENT_VALID_DENIED = 10003;
}
