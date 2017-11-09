package com.jh.utils;

import com.jh.Interceptor.*;
import com.jh.base.PageInfo;
import com.jh.common.*;
import com.jh.common.exception.DataNoMatchException;
import enums.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

public class Util {
    public static boolean isStrNull(String str) {
        if (str == null || "".equals(str))
            return false;
        else
            return true;

    }

    public static Annotation getAnnotation(Object type) {
        if (type.equals(Add.class))
            return Annotation.ADD;
        else if (type.equals(Find.class))
            return Annotation.FIND;
        else if (type.equals(Delete.class))
            return Annotation.DELETE;
        else if (type.equals(SaveOrUpdate.class))
            return Annotation.UPDATE;
        return null;
    }

    public static Field[] getParment(Class tigerClass) {
        if (tigerClass == null)
            return null;
        return tigerClass.getDeclaredFields();
    }

    // 该方法用于字段和查询
    public static InitMsg initValue(HttpServletRequest request, Field[] Fileds) {
        if (Fileds == null || Fileds.length == 0)
            return null;
        Object[] parmen = new Object[Fileds.length];
        boolean haskeys = false;
        boolean hasID = false;
        int size = 0;
        List<String> filds = new ArrayList<String>();
        List<Object> vals = new ArrayList<Object>();
        // 如果主键的值不为空
        for (Field field : Fileds) {
            if (request.getAttribute(field.getName()) != null
                    || request.getParameter(field.getName()) != null) {
                for (java.lang.annotation.Annotation other : field
                        .getAnnotations()) {
                    if (other instanceof javax.persistence.Id) {
                        hasID = true;
                        size++;
                        filds.add(field.getName());
                        vals.add(request.getAttribute(field.getName()) == null ? request
                                .getParameter(field.getName()) : null);
                    }

                    if (other instanceof FindKey)
                        haskeys = true;
                }
            }

        }
        if (hasID) {
            InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                    vals.toArray(new Object[size]));
            msg.setIskeys(haskeys);
            msg.setId(hasID);
            return msg;
        }
        for (Field field : Fileds) {
            if (request.getAttribute(field.getName()) != null
                    || request.getParameter(field.getName()) != null) {
                if (haskeys) {// 如果有查询主键
                    size++;
                    for (java.lang.annotation.Annotation other : field
                            .getAnnotations()) {
                        if (other instanceof FindKey) {
                            filds.add(field.getName());
                            vals.add(request.getAttribute(field.getName()) == null ? request
                                    .getParameter(field.getName()) : null);
                        }
                    }
                } else {// 没有查询主键的情况
                    size++;
                    filds.add(field.getName());
                    vals.add(request.getAttribute(field.getName()) == null ? request
                            .getParameter(field.getName()) : null);
                }

            }

        }
        InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                vals.toArray(new Object[size]));
        msg.setIskeys(haskeys);
        msg.setId(hasID);
        return msg;

    }

    public static InitMsg initValue(HttpServletRequest request, Field[] Fileds, MethodParameter method) {
        if (Fileds == null || Fileds.length == 0)
            return null;
        Object[] parmen = new Object[Fileds.length];
        boolean haskeys = false;
        boolean hasID = false;
        int size = 0;
        List<String> filds = new ArrayList<String>();
        List<Object> vals = new ArrayList<Object>();
        // 如果主键的值不为空
        for (Field field : Fileds) {
            if (getParameterValue(request, method, field) != null) {
                for (java.lang.annotation.Annotation other : field.getDeclaredAnnotations()) {
                    java.lang.annotation.Annotation annotation =
                            Util.getAnnotation(field, NotSerachField.class);
                    NotSerachField serachField =
                            (annotation instanceof NotSerachField) ? (NotSerachField) annotation : null;
                    if (other instanceof javax.persistence.Id && serachField == null) {
                        hasID = true;
                        size++;
                        filds.add(field.getName());
                        vals.add(getParameterValue(request, method, field));
                    }

                    if (other instanceof FindKey)
                        haskeys = true;
                }
            }

        }
        if (hasID) {
            InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                    vals.toArray(new Object[size]));
            msg.setIskeys(haskeys);
            msg.setId(hasID);
            return msg;
        }
        size = 0;
        first:
        for (Field field : Fileds) {
            if (getParameterValue(request, method, field) != null) {
                if (haskeys) {// 如果有查询主键

                    for (java.lang.annotation.Annotation other : field.getDeclaredAnnotations()) {
                        if (other instanceof FindKey) {
                            size++;
                            filds.add(field.getName());
                            vals.add(getParameterValue(request, method, field));
                            continue first;
                        }
                    }
                    Find find = method.getParameterAnnotation(Find.class);
                    java.lang.annotation.Annotation annotation =
                            Util.getAnnotation(field, NotSerachField.class);
                    NotSerachField serachField =
                            (annotation instanceof NotSerachField) ? (NotSerachField) annotation : null;
                    //如果需要把没有注解的参数也加入查询条件，而且该字段本身没有不需要查询
                    if (!find.isSerachKey() && serachField == null) {
                        //把没有注解的字段属性同时加入集合中
                        size++;
                        filds.add(field.getName());
                        vals.add(getParameterValue(request, method, field));
                    }

                } else {// 没有查询z注解的情况
                    java.lang.annotation.Annotation annotation =
                            Util.getAnnotation(field, NotSerachField.class);
                    NotSerachField serachField =
                            (annotation instanceof NotSerachField) ? (NotSerachField) annotation : null;
                    if (serachField != null) continue;
                    size++;
                    filds.add(field.getName());
                    vals.add(getParameterValue(request, method, field));
                }

            }

        }
        InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                vals.toArray(new Object[size]));
        msg.setIskeys(haskeys);
        msg.setId(hasID);
        return msg;

    }


    public static InitMsg initValue(HttpServletRequest request, Field[] Fileds,
                                    boolean iskey, boolean isID) {
        if (Fileds == null || Fileds.length == 0)
            return null;
        Object[] parmen = new Object[Fileds.length];
        boolean haskeys = iskey;
        // 如果主键的值不为空
        for (Field field : Fileds) {
            if (request.getAttribute(field.getName()) != null
                    || request.getParameter(field.getName()) != null) {
                for (java.lang.annotation.Annotation other : field
                        .getAnnotations()) {
                    if (other instanceof javax.persistence.Id && isID) {
                        InitMsg msg = new InitMsg();
                        msg.setId(true);
                        msg.setIskeys(false);
                        msg.setSize(1);
                        String[] fild = new String[]{field.getName()};
                        Object[] objs = new Object[]{request
                                .getAttribute(field.getName())};
                        msg.setFilds(fild);
                        msg.setValues(objs);
                        return msg;
                    }

                    if (other instanceof FindKey)
                        haskeys = true;
                }
            }

        }
        List<String> filds = new ArrayList<String>();
        List<Object> vals = new ArrayList<Object>();
        int size = 0;
        for (Field field : Fileds) {
            if (request.getAttribute(field.getName()) != null) {
                if (haskeys) {// 如果有查询主键
                    size++;
                    for (java.lang.annotation.Annotation other : field
                            .getAnnotations()) {
                        if (other instanceof FindKey) {
                            filds.add(field.getName());
                            vals.add(request.getAttribute(field.getName()) == null ? request
                                    .getParameter(field.getName()) : null);
                        }
                    }
                } else {// 没有查询主键的情况
                    size++;
                    filds.add(field.getName());
                    vals.add(request.getAttribute(field.getName()) == null ? request
                            .getParameter(field.getName()) : null);
                }

            }

        }
        InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                vals.toArray(new Object[size]));
        msg.setIskeys(haskeys);
        msg.setId(false);
        return msg;

    }

    public static String getParameterValue(HttpServletRequest request, MethodParameter method, Field field) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String name = parameters.nextElement();
            if (name.indexOf(".") != -1 && (method.getParameterName() + "." + field.getName()).equals(name)) {
                return request.getParameter(name);
            } else if (field.getName().equals(name)) {
                return request.getParameter(name);
            }
        }
        return null;
    }

    public static String getParameterValue(HttpServletRequest request, String fieldName) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String name = parameters.nextElement();
            if (fieldName.equals(name)) {
                return request.getParameter(name);
            }
        }
        return null;
    }


    public static String getParameterValue(HttpServletRequest request, Field field) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String name = parameters.nextElement();
            if (name.indexOf(".") != -1 && (Cost.PAGE_TGP + "." + field.getName()).equals(name)) {
                return request.getParameter(name);
            } else if (field.getName().equals(name)) {
                return request.getParameter(name);
            }
        }
        return null;
    }


    public static Object parsueData(Field filed, String data) throws ParseException {
        if (filed.getType().equals(String.class)) {
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(filed, FindKey.class);
            FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;
            if (findKey != null) {
                return findKey.left() + data + findKey.right();
            }
            return data;
        } else if (filed.getType().equals(Integer.class)
                || filed.getType().toString().trim().equals("int")) {
            return Integer.parseInt(data);
        } else if (filed.getType().equals(Long.class)
                || filed.getType().toString().trim().equals("long"))
            return Long.parseLong(data);
        else if (filed.getType().equals(Double.class)
                || filed.getType().toString().trim().equals("double"))
            return Double.parseDouble(data);
        else if (filed.getType().equals(Float.class)
                || filed.getType().toString().trim().equals("float"))
            return Float.parseFloat(data);
        if (filed.getType().equals(Short.class)
                || filed.getType().toString().trim().equals("short"))
            return Short.parseShort(data);
        if (filed.getType().equals(Byte.class)
                || filed.getType().toString().trim().equals("byte"))
            return Byte.parseByte(data);
        if (filed.getType().equals(Character.class)
                || filed.getType().toString().trim().equals("char"))
            return Integer.parseInt(data);
        if (filed.getType().equals(Boolean.class)
                || filed.getType().toString().trim().equals("boolean"))
            return Integer.parseInt(data);
        else if (filed.getType().equals(Date.class)) {
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMddhhmmss");
            Date parse = formatSecond.parse(getRealValue(data, 14));
            return parse;
        } else if (filed.getType().equals(java.sql.Date.class)) {
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMdd");
            Date parse = formatSecond.parse(getRealValue(data, 8));
            return parse;
        }
        return data;
    }

    public static Object puserStringToObject(Field filed, String data) throws ParseException {
        if (filed.getType().equals(String.class)) {
            return data;
        } else if (filed.getType().equals(Integer.class)
                || filed.getType().toString().trim().equals("int")) {
            return Integer.parseInt(data);
        } else if (filed.getType().equals(Long.class)
                || filed.getType().toString().trim().equals("long"))
            return Long.parseLong(data);
        else if (filed.getType().equals(Double.class)
                || filed.getType().toString().trim().equals("double"))
            return Double.parseDouble(data);
        else if (filed.getType().equals(Float.class)
                || filed.getType().toString().trim().equals("float"))
            return Float.parseFloat(data);
        if (filed.getType().equals(Short.class)
                || filed.getType().toString().trim().equals("short"))
            return Short.parseShort(data);
        if (filed.getType().equals(Byte.class)
                || filed.getType().toString().trim().equals("byte"))
            return Byte.parseByte(data);
        if (filed.getType().equals(Character.class)
                || filed.getType().toString().trim().equals("char"))
            return Integer.parseInt(data);
        if (filed.getType().equals(Boolean.class)
                || filed.getType().toString().trim().equals("boolean"))
            return Integer.parseInt(data);
        else if (filed.getType().equals(Date.class)) {
            String realValue = getRealValue(data, 14);
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMddhhmmss".substring(0, realValue.length()));
            Date parse = formatSecond.parse(realValue);
            return parse;
        } else if (filed.getType().equals(java.sql.Date.class)) {
            String realValue = getRealValue(data, 8);
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMdd".substring(0, realValue.length()));
            Date parse = formatSecond.parse(realValue);
            return parse;
        }
        return data;
    }

    public static String convernData(Field filed, Object data) throws ParseException {
        if (filed.getType().equals(String.class)) {
            return data.toString();
        } else if (filed.getType().equals(Integer.class)
                || filed.getType().toString().trim().equals("int")) {
            return String.valueOf(data);
        } else if (filed.getType().equals(Long.class)
                || filed.getType().toString().trim().equals("long"))
            return String.valueOf(data);
        else if (filed.getType().equals(Double.class)
                || filed.getType().toString().trim().equals("double"))
            return String.valueOf(data);
        else if (filed.getType().equals(Float.class)
                || filed.getType().toString().trim().equals("float"))
            return String.valueOf(data);
        if (filed.getType().equals(Short.class)
                || filed.getType().toString().trim().equals("short"))
            return String.valueOf(data);
        if (filed.getType().equals(Byte.class)
                || filed.getType().toString().trim().equals("byte"))
            return String.valueOf(data);
        if (filed.getType().equals(Character.class)
                || filed.getType().toString().trim().equals("char"))
            return String.valueOf(data);
        if (filed.getType().equals(Boolean.class)
                || filed.getType().toString().trim().equals("boolean"))
            return String.valueOf(data);
        else if (filed.getType().equals(Date.class)) {
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMddhhmmss");
            return formatSecond.format(data);
        } else if (filed.getType().equals(java.sql.Date.class)) {
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyyMMdd");
            return formatSecond.format(data);
        }
        return data.toString();
    }

    public static void setData(Field filed, Object data, Object obj) throws Exception {
        filed.setAccessible(true);
        if (filed.getType().equals(Integer.class)
                || filed.getType().getSimpleName().trim().equals("int")) {
            filed.set(obj, Integer.parseInt(String.valueOf(data)));
            ;
        } else if (filed.getType().equals(Long.class)
                || filed.getType().getSimpleName().trim().equals("long"))
            filed.set(obj, Long.parseLong(String.valueOf(data)));
        else if (filed.getType().equals(Double.class)
                || filed.getType().getSimpleName().trim().equals("double"))
            filed.set(obj, Double.parseDouble(String.valueOf(data)));
        else if (filed.getType().equals(Float.class)
                || filed.getType().getSimpleName().trim().equals("float"))
            filed.set(obj, Float.parseFloat(String.valueOf(data)));
        else if (filed.getType().equals(Short.class)
                || filed.getType().getSimpleName().trim().equals("short"))
            filed.set(obj, Short.parseShort(String.valueOf(data)));
        else if (filed.getType().equals(Byte.class)
                || filed.getType().getSimpleName().trim().equals("byte"))
            filed.set(obj, Byte.parseByte(String.valueOf(data)));
        else if (filed.getType().equals(Character.class)
                || filed.getType().getSimpleName().trim().equals("char"))
            filed.set(obj, Integer.parseInt(String.valueOf(data)));
        else if (filed.getType().equals(Boolean.class)
                || filed.getType().getSimpleName().trim().equals("boolean"))
            filed.set(obj, Integer.parseInt(String.valueOf(data)));
        else if (filed.getType().equals(Date.class))
            filed.set(obj, (Date) data);
        else if (filed.getType().equals(String.class))
            filed.set(obj, String.valueOf(data));
    }

    public static String replaceUrl(String oldUrl, Class className, Object object, String paName) {

        return null;
    }

    private static String getRealValue(String time, int len) {
        if (time == null) return null;
        StringBuilder result = new StringBuilder();
        char[] chars = time.toCharArray();
        int arrLen = chars.length;
        for (int i = 0; i < arrLen; i++) {
            if (48 > chars[i] || chars[i] > 57) {
                if (i < arrLen - 2) {
                    if (48 > chars[i + 2] || chars[i + 2] > 57) {
                        result.append("0");
                    }
                }
            } else if (48 <= chars[i] && chars[i] <= 57) {
                result.append(chars[i]);
            }
        }
        if (result.length() <= len)
            return result.toString();
        else return result.substring(0, len - 1);
    }

    /**
     * 找到实体类的class。
     *
     * @param parameter
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getClassByMethodParameter(MethodParameter parameter) throws ClassNotFoundException {
        Type type = parameter.getGenericParameterType();
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            for (Type result : ((ParameterizedType) type).getActualTypeArguments()) {
                return Class.forName(result.toString().substring(result.toString().indexOf(" ") + 1));
            }
        }
        return parameter.getParameterType();
    }

    public static java.lang.annotation.Annotation getAnnotation(Class tigerClass, String fieldName, Class an) {
        if (tigerClass == null || fieldName == null || "".equals(fieldName)) return null;
        for (Field field : tigerClass.getDeclaredFields()) {
            if (fieldName.equals(field.getName())) {
                return getAnnotation(field, an);
            }
        }
        return null;
    }

    public static java.lang.annotation.Annotation getClassAnnotation(Class tigerClass, Class an) {
        if (tigerClass == null) return null;
        for (java.lang.annotation.Annotation a : tigerClass.getAnnotations()) {
            if (a.toString().indexOf(an.getSimpleName()) > -1) {
                return a;
            }

        }
        return null;
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
     * 得到排序字段
     *
     * @param asName
     * @param entityClass
     * @return
     * @throws Exception
     */
    public static String getOrderByStr(String asName, Class entityClass) throws Exception {
        String tempAs = asName == null ? "" : asName;
        if (entityClass == null) throw new DataNoMatchException();
        Field[] declaredFields = entityClass.getDeclaredFields();
        List<Field> orderList = new ArrayList<Field>(declaredFields.length);
        for (Field field : declaredFields) {
            if (getAnnotation(field, OrderByField.class) != null) orderList.add(field);
        }
        orderList = sortOrderFiled(orderList);
        StringBuilder orderStr = new StringBuilder();
        String orderBy = (orderList != null && orderList.size() > 0) ? "  order by " : "";
        orderStr.append(orderBy);
        for (Field filed : orderList) {
            OrderByField annotation = (OrderByField) getAnnotation(filed, OrderByField.class);
            orderStr.append(filed.getName() + " ").append(annotation.orderType()).append(",");

        }
        if (orderStr.length() > 0) {
            return orderStr.substring(0, orderStr.length() - 1);
        }
        return orderStr.toString();
    }

    private static List<Field> sortOrderFiled(List<Field> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i; j++) {
                Field filed1 = list.get(j);
                Field filed2 = list.get(j + 1);
                OrderByField order1 = (OrderByField) getAnnotation(filed1, OrderByField.class);
                OrderByField order2 = (OrderByField) getAnnotation(filed2, OrderByField.class);
                if (order1.orderId() > order2.orderId()) {
                    Field temp = filed2;
                    list.set(j + 1, filed1);
                    list.set(j, temp);
                }
            }
        }
        return list;
    }

    public static InitMsg initValueForSave(HttpServletRequest request, Field[] Fileds, MethodParameter method) throws ParseException {
        if (Fileds == null || Fileds.length == 0)
            return null;
        boolean haskeys = false;
        boolean hasID = false;
        int size = 0;
        List<String> filds = new ArrayList<String>();
        List<Object> vals = new ArrayList<Object>();
        // 如果主键的值不为空
        for (Field field : Fileds) {
            if (getParameterValue(request, method, field) != null) {
                filds.add(field.getName());
                vals.add(puserStringToObject(field, getParameterValue(request, method, field)));
                size++;
            }
        }
        InitMsg msg = new InitMsg(size, filds.toArray(new String[size]),
                vals.toArray(new Object[size]));
        msg.setIskeys(haskeys);
        msg.setId(hasID);
        return msg;

    }

    public static boolean isPageList(MethodParameter method) {
        java.lang.annotation.Annotation[] methodAnnotations = method.getMethodAnnotations();
        for (java.lang.annotation.Annotation annotation : methodAnnotations) {
            if (annotation instanceof Page) return true;
        }
        return false;
    }

    /**
     * 得到MVC 类级别的RequestMapping  注解
     *
     * @param className
     * @return
     */
    public static RequestMapping getRequestMapping(Class className) {
        if (className == null) return null;
        for (java.lang.annotation.Annotation an : className.getAnnotations()) {
            if (an instanceof RequestMapping) {
                return (RequestMapping) an;
            }
        }
        return null;
    }

    /**
     * 得到springMVC 方法级别的RequestMapping注解 从而可以得到请求地址
     *
     * @param method
     * @return
     */
    public static RequestMapping getRequestMapping(Method method) {
        if (method == null) return null;
        for (java.lang.annotation.Annotation an : method.getAnnotations()) {
            if (an instanceof RequestMapping) {
                return (RequestMapping) an;
            }
        }
        return null;
    }

    /**
     * 获取方法上面指定注解对象
     *
     * @param method  方法
     * @param anClass 注解class
     * @return 返回注解
     */
    public static java.lang.annotation.Annotation getAnnotation(Method method, Class anClass) {
        if (method == null || anClass == null) return null;
        for (java.lang.annotation.Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().equals(anClass)) return annotation;
        }
        return null;
    }

    public static PageInfo initPageInfo(HttpServletRequest request, PageInfo pageInfo) throws Exception {
        if (request == null || pageInfo == null) return pageInfo;
        pageInfo.setUrl(request.getRequestURI());
        for (Field field : PageInfo.class.getDeclaredFields()) {
            String parameterValue = getParameterValue(request, field);
            if (!StringUtils.isNull(parameterValue)) {
                field.setAccessible(true);
                field.set(pageInfo, Util.puserStringToObject(field, parameterValue));
            }

        }
        return pageInfo;
    }
}
