package com.jh.Interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindKey {
    //是否是模糊查询
    String selectType() default "=";
    String left() default "";
    String right() default "";
}
