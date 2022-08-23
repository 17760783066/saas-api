package com.example.demo.common.controller;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonView extends AbstractView {
    private Object result;

    public JsonView(Object result) {
        super();
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    @Override
    protected void renderMergedOutputModel(java.util.Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(JsonSerializerManager.serialize(result));
    }

}
