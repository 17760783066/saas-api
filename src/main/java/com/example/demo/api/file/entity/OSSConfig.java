package com.example.demo.api.file.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "oss", ignoreUnknownFields = false)
@Component
@Validated
public class OSSConfig {
    @NotNull
//    @Value("${oss.key}")
    private String key;
    @NotNull
    private String secret;
    @NotNull
    private String bucket;
    @NotNull
    private String internalEndpoint;
    @NotNull
    private String canonicalDomain;
    @NotNull
    private String cdnDomain;
    @NotNull
    private String namespace;
    @NotNull
    private String putArn;
    @NotNull
    private String region;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getInternalEndpoint() {
        return internalEndpoint;
    }

    public void setInternalEndpoint(String internalEndpoint) {
        this.internalEndpoint = internalEndpoint;
    }

    public String getCanonicalDomain() {
        return canonicalDomain;
    }

    public void setCanonicalDomain(String canonicalDomain) {
        this.canonicalDomain = canonicalDomain;
    }

    public String getCdnDomain() {
        return cdnDomain;
    }

    public void setCdnDomain(String cdnDomain) {
        this.cdnDomain = cdnDomain;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPutArn() {
        return putArn;
    }

    public void setPutArn(String putArn) {
        this.putArn = putArn;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
