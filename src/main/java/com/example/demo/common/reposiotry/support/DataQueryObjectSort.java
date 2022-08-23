package com.example.demo.common.reposiotry.support;

public class DataQueryObjectSort implements DataQueryObject {

    protected String sortPropertyName = "id";
    protected boolean sortAscending = false;


    public String getSortPropertyName() {
        return sortPropertyName;
    }

    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    protected Byte zero2Null(Byte val) {
        return val == 0 ? null : val;
    }
}
