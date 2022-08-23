package com.example.demo.common.reposiotry.support;

public class QueryBetween<T extends Comparable<?>> {

    public T before;
    public T after;

    public QueryBetween() {
    }

    public QueryBetween(T before, T after) {
        this.before = before;
        this.after = after;
    }

    public T getBefore() {
        return before;
    }

    public void setBefore(T before) {
        this.before = before;
    }

    public T getAfter() {
        return after;
    }

    public void setAfter(T after) {
        this.after = after;
    }
}
