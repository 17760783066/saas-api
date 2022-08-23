package com.example.demo.api.common.model;

public class ValCode {
    private Long key;
    private String code;
    private String account;
    
    public ValCode() {
    }
    
    public ValCode(Long key, String code, String account) {
        this.key = key;
        this.code = code;
        this.account = account;
    }

    public Long getKey() {
        return key;
    }
    public void setKey(Long key) {
        this.key = key;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    
}
