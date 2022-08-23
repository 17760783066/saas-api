package com.example.demo.common.controller.auth;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Touchable {

    boolean value() default false;

}
