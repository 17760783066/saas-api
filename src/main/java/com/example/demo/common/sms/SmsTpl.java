package com.example.demo.common.sms;

public class SmsTpl {

    public static final String TPL_RESET_PWD = "SMS_163052510";
    public static final String TPL_VAL_CODE = "SMS_182551357";

    private String templateCode;
    private String signName = "迈道科技";
    private String phoneNumbers;
    private String templateParam;

    public SmsTpl() {
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public SmsTpl(String templateCode, String phoneNumbers, String templateParam) {
        this.templateCode = templateCode;
        this.phoneNumbers = phoneNumbers;
        this.templateParam = templateParam;
    }

}
