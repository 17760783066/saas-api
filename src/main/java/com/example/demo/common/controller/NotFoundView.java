package com.example.demo.common.controller;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotFoundView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(java.util.Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setStatus(404);
    }

}
