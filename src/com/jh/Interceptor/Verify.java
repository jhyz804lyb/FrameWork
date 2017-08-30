package com.jh.Interceptor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Verify {
    /**
     * 类名
     * @return
     */
    String classPath() ;

    /**
     * 方法名
     * @return
     */
    String MethodName() ;
}
