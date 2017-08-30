package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * @author liyabin
 * @date 2017-08-24下午 5:35
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface OrderByField
{
    int orderId() default 0;
    String orderType() default "asc";
}