package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 文件导出注解，在控制器上面加上此注解并标明需要导出的类名标题数组以及字段数组
 * @author liyabin
 * @date 2017/11/6
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportExcel
{
    /**
     * 表头
     * @return
     */
    String [] titleList();

    /**
     * 列中具体的值
     * @return
     */
    String [] fieldList();

    /**
     * 数据类型，默认都是为String
     * @return
     */
    int [] dataType() default {0};

    /**
     * 列宽
     * @return
     */
    int [] cellWidth() default {-1};
}