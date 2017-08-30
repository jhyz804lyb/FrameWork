package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 注解表示这个参数查询条件里面的参数
 * @author liyabin
 * @date 2017-08-24下午 4:50
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InParameter
{
}