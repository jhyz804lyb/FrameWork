package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * @author liyabin
 * @date 2017-08-31下午 10:16
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull
{
}