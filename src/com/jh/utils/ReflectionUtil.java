package com.jh.utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    /***
     * 获取私有成员变量的值
     * 
     */
    public static Object getValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // 参数值为true，禁止访问控制检查

        return field.get(instance);
    }
    /***
     * 获取私有成员变量的值
     *
     */
    public static Object getValue(Object instance,Class tigerClass, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = tigerClass.getDeclaredField(fieldName);
        field.setAccessible(true); // 参数值为true，禁止访问控制检查

        return field.get(instance);
    }

    /***
     * 设置私有成员变量的值
     * 
     */
    public static void setValue(Object instance, String fileName, Object value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        Field field = instance.getClass().getDeclaredField(fileName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /***
     * 访问私有方法
     * 
     */
    public static Object callMethod(Object instance, String methodName, Class[] classes, Object[] objects)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = instance.getClass().getDeclaredMethod(methodName, classes);
        method.setAccessible(true);
        return method.invoke(instance, objects);
    }

    public static Field getField(Class tigerClass, String fieldName) throws NoSuchFieldException
    {
        Field field = tigerClass.getDeclaredField(fieldName);
        return field;
    }
}