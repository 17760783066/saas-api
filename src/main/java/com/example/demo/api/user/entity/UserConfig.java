package com.example.demo.api.user.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "user", ignoreUnknownFields = false)
@Validated
@Component
public class UserConfig {

    private String salt;
    private int sessionDays;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getSessionDays() {
        return sessionDays;
    }

    public void setSessionDays(int sessionDays) {
        this.sessionDays = sessionDays;
    }
}
