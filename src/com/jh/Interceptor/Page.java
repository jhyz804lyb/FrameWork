package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * @author liyabin
 * @date 2017-08-30下午 8:38
 */

@Target ( {ElementType.TYPE, ElementType.METHOD})
@Retention (RetentionPolicy.RUNTIME)
@Documented
public @interface Page
{
   int defaultCount() default 10;
}