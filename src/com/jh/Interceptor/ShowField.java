package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 被这个注解标志的字段将会展示到列表中
 * @author liyabin
 * @date 2017/11/28
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShowField
{

}