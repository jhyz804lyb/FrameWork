package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * @author liyabin
 * @date 2017-08-24下午 4:50
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InParameter
{
}