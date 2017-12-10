package com.jh.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

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
    public static Object getValue(Object instance, Class tigerClass, String fieldName)
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

    public static Field getField(Class tigerClass, String fieldName) throws NoSuchFieldException {
        Field field = tigerClass.getDeclaredField(fieldName);
        return field;
    }

    /**
     * 初始化1 个bean 从另一个bean中复制值 这里要求字段名称和类型一致
     *
     * @param bean
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T initBeanValue(Class<T> bean, Object obj) throws Exception {
        T result = bean.newInstance();
        Field[] resultFields = bean.getDeclaredFields();
        Field[] beanField = obj.getClass().getDeclaredFields();
        for (Field r : resultFields) {
            for (Field b : beanField) {
                if (r.getType().equals(b.getType()) && r.getName().equals(b.getName())) {
                    r.setAccessible(true);
                    b.setAccessible(true);
                    r.set(result, b.get(obj));
                }
            }
        }
        return result;

    }

    public static <T> T initBeanValue(Class<T> bean, Object obj, Class objClass) throws Exception {
        T result = bean.newInstance();
        Field[] resultFields = bean.getDeclaredFields();
        Field[] beanField = objClass.getDeclaredFields();
        for (Field r : resultFields) {
            for (Field b : beanField) {
                if (r.getType().equals(b.getType()) && r.getName().equals(b.getName())) {
                    r.setAccessible(true);
                    b.setAccessible(true);
                    r.set(result, b.get(obj));
                }
            }
        }
        return result;

    }

    /**
     * 获取字段
     *
     * @param tigerClass
     * @return
     */
    public static Field[] getParment(Class tigerClass) {
        if (tigerClass == null)
            return null;
        return tigerClass.getDeclaredFields();
    }

    /**
     * 获取字段中的注解
     *
     * @param filed
     * @param claName
     * @return
     */
    public static java.lang.annotation.Annotation getAnnotation(Field filed, Class claName) {
        if (filed == null || claName == null) return null;
        for (java.lang.annotation.Annotation an : filed.getDeclaredAnnotations()) {
            if (an.annotationType().equals(claName)) return an;
        }
        return null;
    }

    /**
     * @param type
     * @return
     */
    public static int getAnnotationCount(Class type, Class an) {
        int count = 0;
        Field[] parment = getParment(type);
        for (Field field : parment) {
            Annotation annotation = getAnnotation(field, an);
            if (annotation != null) count++;
        }
        return count;
    }

    /**
     * @param type
     * @return
     */
    public static java.lang.annotation.Annotation[] getAnnotationArray(Class type, Class an) {
        int count = 0;
        Annotation[] temp = new Annotation[getAnnotationCount(type, an)];
        Field[] parment = getParment(type);
        for (Field field : parment) {
            Annotation annotation = getAnnotation(field, an);
            if (annotation != null) temp[count++] = annotation;
        }
        return temp;
    }

    /**
     * @param type
     * @return
     */
    public static Field[] getFieldArray(Class type, Class an) {
        int count = 0;
        Field[] temp = new Field[getAnnotationCount(type, an)];
        Field[] parment = getParment(type);
        for (Field field : parment) {
            Annotation annotation = getAnnotation(field, an);
            if (annotation != null) temp[count++] = field;
        }
        return temp;
    }

    /**
     * 得到实际的字段
     *
     * @param filedName
     * @param otherClass
     * @return
     */
    public static Field getActualField(String filedName, Class otherClass) {
        if (filedName.indexOf(".") != -1) {
            String fileds = filedName.substring(filedName.indexOf(".") + 1, filedName.length());
            String temp = filedName.substring(0, filedName.indexOf("."));
            for (Field field : otherClass.getDeclaredFields()) {
                if (field.getName().equals(temp)) {
                    return getActualField(fileds, field.getType());
                }
            }
        } else {
            for (Field field : otherClass.getDeclaredFields()) {
                if (field.getName().equals(filedName)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static Object getFiledVal(Object commerceInfo, String filedName, Class className)
            throws IllegalAccessException {
        if (filedName.indexOf(".") != -1) {
            String fileds = filedName.substring(filedName.indexOf(".") + 1, filedName.length());
            String str = filedName.substring(0, filedName.indexOf("."));
            for (Field field : className.getDeclaredFields()) {
                if (field.getName().equals(str)) {
                    field.setAccessible(true);
                    return getFiledVal(field.get(commerceInfo), fileds, field.getType());
                }
            }
        } else {
            for (Field field : className.getDeclaredFields()) {
                if (field.getName().equals(filedName)) {
                    if (field.getType().getName().equals("java.util.Date")) {
                        field.setAccessible(true);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                        return sdf.format(field.get(commerceInfo));
                    } else if (field.getType().getName().equals("java.sql.Date")) {
                        field.setAccessible(true);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        return sdf.format(field.get(commerceInfo));
                    } else if (field.getType().getName().equals(Double.class.getName()) ||
                            field.getType().getSimpleName().equals("double")) {
                        field.setAccessible(true);
                        return field.get(commerceInfo);
                    } else {
                        field.setAccessible(true);
                        return field.get(commerceInfo);
                    }
                }
            }
        }
        return "";
    }

}