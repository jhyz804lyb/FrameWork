package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 注解表示这个字段不需要加入到查询条件中
 * @author liyabin
 * @date 2017-08-23下午 4:58
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSerachField
{
}