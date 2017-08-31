package com.jh.Interceptor;

import java.lang.annotation.*;

/**
 * @author liyabin
 * @date 2017-08-31下午 10:21
 */

@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Regular
{
    /**
     * 正则表达式
     * @return
     */
    String value();

    /**
     * 验证错误的提示信息
     * @return
     */
    String msg();
}