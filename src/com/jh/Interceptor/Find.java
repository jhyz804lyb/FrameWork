package com.jh.Interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数注解 表示这个参数在进入控制前将会自动被框架装载
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Find
{
    /**
     * 表示 是否子查询被FindKey 注解的参数为查询参数
     * @return
     */
    boolean isSerachKey() default false;

    /**
     * 对应实体类的class
     * @return
     */
    String entityClass() default "";

    /**
     * 执行的sql语句
     * @return
     */
    String SQL() default "";

    /**
     * 执行的oql语句
     * @return
     */
    String OQL() default "";
}
