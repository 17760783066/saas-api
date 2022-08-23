package com.example.demo.common.model;

public interface ErrorCode {

    // common
    public static final int ERR_UNKNOWN_ERROR = 1;
    public static final int ERR_ILLEGAL_ARGUMENT = 2;
    public static final int ERR_PERMISSION_DENIED = 3;
    public static final int ERR_DETAILED_MESSAGE = 4;
    public static final int ERR_SESSION_EXPIRES = 5;
    public static final int ERR_OPERATION_TOO_FREQUENT = 6;
    public static final int ERR_DATA_NOT_FOUND = 7;
    public static final int ERR_VERSION_UPDATE = 8;
    public static final int ERR_ID_NULL = 9;

    public static final int ERR_MOBILE_INVALID = 100;
    public static final int ERR_ACCOUNT_NOT_EXIST = 101;
    public static final int ERR_ACCOUNT_EXIST = 1011;
    public static final int ERR_PASSWORD_INVALID = 102;
    public static final int ERR_ACCOUNT_FORBIDDEN = 103;
    public static final int ERR_IDENTITY_INVALID = 104;
    public static final int ERR_MOBILE_EXIST = 105;
    public static final int ERR_IDENTITY_EXIST = 106;
    public static final int ERR_EMAIL_ERROR = 107;
    public static final int ERR_USER_NOT_FOUNT = 108;
    public static final int ERR_MOBILE_NOT_VALCODE = 109;
    public static final int ERR_PASSWORD_VALID_DENIED = 110;
    public static final int ERR_VALCODE = 111;
    public static final int ERR_VALCODE_NONE = 112;
    public static final int ERR_MOBILE_NOT_FOUNT = 113;
    public static final int ERR_PASSWORD_INCONSISTENT = 114;
    public static final int ERR_WORN_PASSWORD_INCONSISTENT = 115;
    public static final int ERR_PAYNUMBER_EXIST = 150;


    //file
    public static final int ERR_FILE_SIZE = 200;
    public static final int ERR_IMG_NEEDED = 201;
    public static final int ERR_FILE_NOT_FOUND = 202;

    //article
    public static final int ERR_ARTICLE_TITLE = 300;
    public static final int ERR_ARTICLE_CONTENT = 301;
}
