package com.example.demo.common.controller.auth;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.api.merchant.entity.MerchantPermission;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RequiredMerchantPermission {

    MerchantPermission[] value() default {MerchantPermission.NONE};

}
