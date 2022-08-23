package com.example.demo.common.controller;

import com.example.demo.common.controller.auth.AdmInterceptor;
import com.example.demo.common.controller.auth.CommonInterceptor;
import com.example.demo.common.controller.auth.MerchantInterceptor;
import com.example.demo.common.controller.auth.UserInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebappConfigs implements WebMvcConfigurer {

    @Autowired
    private AdmInterceptor admInterceptor;

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private MerchantInterceptor merchantInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/common/**");
        registry.addInterceptor(admInterceptor).addPathPatterns("/adm/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/usr/**");
        registry.addInterceptor(merchantInterceptor).addPathPatterns("/merchant/**");

    }

}
