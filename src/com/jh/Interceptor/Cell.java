package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 注解表明对应数据库的那个字段
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cell {
   String columnName();
}
