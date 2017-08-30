package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 方法级别的注解，在控制层加上此注解 返回对象将会转换成json对象
 * @author liyabin
 * @date 2017-08-22下午 3:36
 */
@Target ( {ElementType.TYPE, ElementType.METHOD})
@Retention (RetentionPolicy.RUNTIME)
@Documented
public @interface Json
{
   /**
    * 如果对象为空时返回的值
    * @return
    */
   String value() default "";
}