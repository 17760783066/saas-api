package com.example.demo.api.file.entity;

public class UploadOptions {
    private String contentType;
    private AccessPermission permission;
    private String namespace;
    private int randomLength;
    private String fileName;

    public String getContentType() {
        return contentType;
    }

    public UploadOptions setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public AccessPermission getPermission() {
        return permission;
    }

    public UploadOptions setPermission(AccessPermission permission) {
        this.permission = permission;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public UploadOptions setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public int getRandomLength() {
        return randomLength;
    }

    public UploadOptions setRandomLength(int randomLength) {
        this.randomLength = randomLength;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadOptions setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public enum AccessPermission {
        DEFAULT, PRIVATE, PUBLIC_READ
    }

}
