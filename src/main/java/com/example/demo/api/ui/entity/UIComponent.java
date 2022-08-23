package com.example.demo.api.ui.entity;

import java.util.List;

public class UIComponent {
    private String key;
    private String title;
    private List<?> list;
    private Byte listStyle;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<?> getList() {
        return list;
    }
    public void setList(List<?> list) {
        this.list = list;
    }
    public Byte getListStyle() {
        return listStyle;
    }
    public void setListStyle(Byte listStyle) {
        this.listStyle = listStyle;
    }

    
}
