package com.jh.utils;

/**
 * @author liyabin
 * @date 2017-08-24下午 5:03
 */
public class StringUtils
{
    /**
     * 判断字符是否为空
     * @param string 要判断的字符
     * @return true 为空或者“” false 不为空
     */
    public static boolean isNull(String string)
    {
      return string==null||string.length()==0;
    }
}
