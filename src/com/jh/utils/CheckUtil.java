package com.jh.utils;

import com.jh.Interceptor.*;
import com.jh.entity.EBase;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.regex.*;

/**
 * @author liyabin
 * @date 2017-08-31下午 10:07
 */
public class CheckUtil
{
    public static boolean checkFiled(Object entity, Class className) throws Exception
    {
        if (entity == null || className == null) return false;
        for (Field field : className.getDeclaredFields())
        {
            if (!checkNotNull(field, entity) || !checkValue(field, entity))
            {
                if (entity instanceof EBase)
                {
                    EBase base = (EBase) entity;
                    String msg =!checkNotNull(field, entity)?"添加失败，字段:"+field.getName()+"不能为空！":"添加失败，字段:"+field.getName()+"格式不正确！";
                    base.setMsg(msg);
                    base.setSuccess(false);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 如果字段拥有NotNull注解那么校验他是否为空
     *
     * @param field
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    private static boolean checkNotNull(Field field, Object entity) throws IllegalAccessException
    {
        if (field == null || entity == null) return false;
        field.setAccessible(true);
        if (Util.getAnnotation(field, NotNull.class) != null)
        {
            return field.get(entity) != null;
        }
        return true;
    }

    /**
     * 校验值是否符合正则表达式
     *
     * @param field
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    private static boolean checkValue(Field field, Object entity) throws IllegalAccessException, ParseException
    {
        if (field == null || entity == null) return false;
        field.setAccessible(true);
        if (Util.getAnnotation(field, Regular.class) != null && field.get(entity) != null)
        {
            Regular regular = (Regular) Util.getAnnotation(field, Regular.class);
            Pattern pattern = Pattern.compile(regular.value());
            String data = Util.convernData(field, field.get(entity));
            Matcher match = pattern.matcher(data);
            return match.matches();
        }
        return true;
    }
}
