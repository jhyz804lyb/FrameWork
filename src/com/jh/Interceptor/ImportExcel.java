package com.jh.Interceptor;

import java.lang.annotation.*;

/**文件导入注解
 * @author liyabin
 * @date 2017/11/6
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportExcel
{
    String classPath();
    int [] cellArray() default {};
    String [] filedList();
}