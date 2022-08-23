package com.example.demo.common.controller.auth;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.api.admin.entity.AdminPermission;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RequiredPermission {

    AdminPermission[] value() default {AdminPermission.NONE};

}
