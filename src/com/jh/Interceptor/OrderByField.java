package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * 表示这个字段建辉被用作排序
 * @author liyabin
 * @date 2017-08-24下午 5:35
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface OrderByField
{
    /**
     * orderId  决定排序优先级
     * @return
     */
    int orderId() default 0;

    /**
     * 排序方式
     * @return
     */
    String orderType() default "asc";
}