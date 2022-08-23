package com.example.demo.common.model;

public class Permission {

    private String key;
    private String label;
    private String level;

    public Permission() {
        super();
    }

    public Permission(String key, String label, String level) {
        super();
        this.key = key;
        this.label = label;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
