package com.jh.utils;

import com.jh.Interceptor.*;
import com.jh.base.*;
import com.jh.common.*;
import com.jh.entity.EBase;
import enums.Annotation;
import org.hibernate.*;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class DataBindUnit
{

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    CreatePageInfoInteface pageIntefac;

    public Object initData(HttpServletRequest request, Annotation type,
                           Class tigerClass, MethodParameter method) throws Exception
    {
        if (tigerClass == null)
            return null;
        switch (type)
        {
            case ADD:
                return SaveEntity(request, tigerClass, method);
            case FIND:
                Find parameterAnnotation = method.getParameterAnnotation(Find.class);
                //只注解 Find 没有加入其它参数
                if (StringUtils.isNull(parameterAnnotation.entityClass()) &&
                        StringUtils.isNull(parameterAnnotation.OQL()) &&
                        StringUtils.isNull(parameterAnnotation.SQL()))
                {
                    return FindCommonEntity(request, tigerClass, method);
                }
                //如果标明具体的实体类的class。 这种情况 大多是在DTO中应用到
                else if (!StringUtils.isNull(parameterAnnotation.entityClass()))
                {
                    return FindDTOEntity(request, tigerClass, parameterAnnotation, method);
                }
                //如果这次查询使用OQL语句来执行
                else if (!StringUtils.isNull(parameterAnnotation.OQL()))
                {
                    return FindOQLEntity(request, parameterAnnotation, tigerClass, method);
                }
                //如果这次查询使用SQL语句来执行
                else if (!StringUtils.isNull(parameterAnnotation.SQL()))
                {
                    return FindSQLEntity(request, parameterAnnotation, tigerClass, method);
                }
                break;
            case DELETE:
                deleteEntity(request, tigerClass, method);
                break;

            case UPDATE:
                updateEntity(request, tigerClass, method);
                break;
        }

        return null;
    }


    /**
     * 持久层实体查询
     *
     * @param request
     * @param tigerClass
     * @param method
     * @return
     * @throws Exception
     */
    private Object FindCommonEntity(HttpServletRequest request,
                                    Class tigerClass, MethodParameter method) throws Exception
    {
        InitMsg par = Util.initValue(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        StringBuilder oql = new StringBuilder();
        Query query = null;
        if (par.isId()) //如果是id查询
        {
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation instanceof Between) ? (Between) annotation : null;
                if (between != null)
                {
                    String start = Util.getParameterValue(request, between.startField());
                    String end = Util.getParameterValue(request, between.endField());
                    oql.append(StringUtils.isNull(start) ? "" :
                            (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                            .append(StringUtils.isNull(end) ? "" :
                                    (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                }
                else
                {
                    oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                }

            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);
        }
        else if (par.isIskeys())
        {
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);

        }
        else
        {
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {

                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            oql.append(Util.getOrderByStr("", tigerClass));

        }
        boolean isPageList = (Collection.class.isAssignableFrom(method.getParameterType()) && Util.isPageList(method));
        if (isPageList)
        {
            String pageCountSQL = pageIntefac.getPageCountSQL(oql.toString());

            Query countQuery = session.createQuery(pageCountSQL);
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation instanceof Between) ? (Between) annotation : null;
                if (between != null)
                {
                    String start = Util.getParameterValue(request, between.startField());
                    String end = Util.getParameterValue(request, between.endField());
                    if (!StringUtils.isNull(start))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.startField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                    }
                    if (!StringUtils.isNull(end))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.endField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                    }
                }
                else
                {
                    countQuery.setParameter(filds[i],
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
                }
            }
            List list = countQuery.list();
            Integer max = null;
            if (list == null || list.size() == 0) max = 0;
            else
            {
                max = Integer.parseInt(String.valueOf(list.get(0)));
            }
            java.lang.annotation.Annotation annotation = Util.getAnnotation(method.getMethod(), Page.class);
            Page temp = annotation instanceof Page ? (Page) annotation : null;
            PageInfo page = new PageInfo();
            page.setPageCount(temp.defaultCount());
            PageInfo pageInfo = Util.initPageInfo(request, page);
            if (pageInfo.getPageNo() == null)
            {
                pageInfo.setPageNo(1);
            }
            //设置最大数据量信息
            pageInfo.setMaxCount(max);
            //设置最大页信息，如果max 为0 对应为0
            pageInfo.setMaxPage(max != 0 ? (max % pageInfo.getPageCount() == 0 ? max / pageInfo.getPageCount() :
                    1 + (max / pageInfo.getPageCount())) : 0);
            //得到转换之后的分页查询语句，由于可能会出现以后不适用hibernate 所以这里不使用 hibernate的分页
            //query = session.createQuery(pageIntefac.getPageListSQL(oql.toString(), pageInfo));
            //hibernate 不是识别LIMIT 语句 这里分页做出调整
            query = session.createQuery(oql.toString())
                    .setFirstResult((pageInfo.getPageNo() - 1) * pageInfo.getPageCount())
                    .setMaxResults(pageInfo.getPageCount());
            //处理分页的情况
            pageInfo.setUrl(request.getRequestURI());;
            request.setAttribute(Cost.PAGE_INFO,pageInfo);
        }
        else
        {
            query = session.createQuery(oql.toString());
        }

        for (int i = 0; i < par.getSize(); i++)
        {
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(tigerClass, filds[i], Between.class);
            Between between = (annotation instanceof Between) ? (Between) annotation : null;
            if (between != null)
            {
                String start = Util.getParameterValue(request, between.startField());
                String end = Util.getParameterValue(request, between.endField());
                if (!StringUtils.isNull(start))
                {
                    query.setParameter(filds[i] + "_" + between.startField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                }
                if (!StringUtils.isNull(end))
                {
                    query.setParameter(filds[i] + "_" + between.endField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                }
            }
            else
            {
                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }

        }
        List<Object> list = query.list();
        session.close();
        //如果是集合类型目前只拓展 List其他的Map之类后续拓展
        if (Collection.class.isAssignableFrom(method.getParameterType()))
        {
            return list;
        }
        else
        {
            return (list == null || list.size() == 0 ? null : list.get(0));
        }
    }


    private Object FindDTOEntity(HttpServletRequest request,
                                 Class tigerClass, Find find, MethodParameter method) throws Exception
    {
        String entityClass = find.entityClass();
        Class<?> forName = Class.forName(entityClass);
        InitMsg par = Util.initValue(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        StringBuilder oql = new StringBuilder();
        Query query = null;
        if (par.isId()) //如果是id查询
        {
            oql.append("FROM ").append(forName.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);
        }
        else if (par.isIskeys())
        {
            oql.append("FROM ").append(forName.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);

        }
        else
        {
            oql.append("FROM ").append(forName.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {

                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            oql.append(Util.getOrderByStr("", tigerClass));

        }
        //必须是List 而且还拥有Page注解才走分页操作
        boolean isPageList = (Collection.class.isAssignableFrom(method.getParameterType()) && Util.isPageList(method));
        if (isPageList)
        {
            String pageCountSQL = pageIntefac.getPageCountSQL(oql.toString());
            Query countQuery = session.createQuery(pageCountSQL);
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation instanceof Between) ? (Between) annotation : null;
                if (between != null)
                {
                    String start = Util.getParameterValue(request, between.startField());
                    String end = Util.getParameterValue(request, between.endField());
                    if (!StringUtils.isNull(start))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.startField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                    }
                    if (!StringUtils.isNull(end))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.endField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                    }
                }
                else
                {
                    countQuery.setParameter(filds[i],
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
                }

            }
            List list = countQuery.list();
            Integer max = null;
            if (list == null || list.size() == 0) max = 0;
            else
            {
                max = (Integer) list.get(0);
            }
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(method.getMethod(), method.getMethod().getDeclaringClass());
            Page temp = annotation instanceof Page ? (Page) annotation : null;
            PageInfo page = new PageInfo();
            page.setPageCount(temp.defaultCount());
            PageInfo pageInfo = Util.initPageInfo(request, page);
            //设置最大数据量信息
            pageInfo.setMaxCount(max);
            //设置最大页信息，如果max 为0 对应为0
            pageInfo.setMaxPage(max != 0 ? (max % pageInfo.getPageCount() == 0 ? max / pageInfo.getPageCount() :
                    1 + (max / pageInfo.getPageCount())) : 0);
            request.setAttribute(Cost.PAGE_ATTR_NAME, pageInfo);
            //得到转换之后的分页查询语句，由于可能会出现以后不适用hibernate 所以这里不使用 hibernate的分页
            query = session.createQuery(oql.toString())
                    .setFirstResult((pageInfo.getPageNo() - 1) * pageInfo.getPageCount())
                    .setMaxResults(pageInfo.getPageCount());
            pageInfo.setUrl(request.getRequestURI());;
            request.setAttribute(Cost.PAGE_INFO,pageInfo);
        }
        else
        {
            query = session.createQuery(oql.toString());
        }

        for (int i = 0; i < par.getSize(); i++)
        {
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(tigerClass, filds[i], Between.class);
            Between between = (annotation instanceof Between) ? (Between) annotation : null;
            if (between != null)
            {
                String start = Util.getParameterValue(request, between.startField());
                String end = Util.getParameterValue(request, between.endField());
                if (!StringUtils.isNull(start))
                {
                    query.setParameter(filds[i] + "_" + between.startField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                }
                if (!StringUtils.isNull(end))
                {
                    query.setParameter(filds[i] + "_" + between.endField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                }
            }
            else
            {
                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }

        }
        List<Object> list = query.list();
        session.close();
        //如果是集合类型目前只拓展 List其他的Map之类后续拓展
        if (Collection.class.isAssignableFrom(method.getParameterType()))
        {
            return list;
        }
        else
        {
            return (list == null || list.size() == 0 ? null : list.get(0));
        }
    }

    private Object FindOQLEntity(HttpServletRequest request, Find find,
                                 Class tigerClass, MethodParameter method) throws Exception
    {
        InitMsg par = Util.initValue(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        StringBuilder oql = new StringBuilder();
        Query query = null;
        if (par.isId()) //如果是id查询
        {
            oql.append(find.OQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);
        }
        else if (par.isIskeys())
        {
            oql.append(find.OQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);

        }
        else
        {
            oql.append(find.OQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation2 instanceof Between) ? (Between) annotation2 : null;
                if (findKey != null && between == null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    if (between != null)
                    {
                        String start = Util.getParameterValue(request, between.startField());
                        String end = Util.getParameterValue(request, between.endField());
                        oql.append(StringUtils.isNull(start) ? "" :
                                (" AND " + filds[i] + " >= :" + filds[i] + "_" + between.startField()))
                                .append(StringUtils.isNull(end) ? "" :
                                        (" AND " + filds[i] + " <= :" + filds[i] + "_" + between.endField()));
                    }
                    else
                    {
                        oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                    }
                }
            }
            oql.append(Util.getOrderByStr("", tigerClass));

        }
        //必须是List 而且还拥有Page注解才走分页操作
        boolean isPageList = (Collection.class.isAssignableFrom(method.getParameterType()) && Util.isPageList(method));
        if (isPageList)
        {
            String pageCountSQL = pageIntefac.getPageCountSQL(oql.toString());
            Query countQuery = session.createQuery(pageCountSQL);
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], Between.class);
                Between between = (annotation instanceof Between) ? (Between) annotation : null;
                if (between != null)
                {
                    String start = Util.getParameterValue(request, between.startField());
                    String end = Util.getParameterValue(request, between.endField());
                    if (!StringUtils.isNull(start))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.startField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                    }
                    if (!StringUtils.isNull(end))
                    {
                        countQuery.setParameter(filds[i] + "_" + between.endField(),
                                Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                    }
                }
                else
                {
                    countQuery.setParameter(filds[i],
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
                }
            }
            List list = countQuery.list();
            Integer max = null;
            if (list == null || list.size() == 0) max = 0;
            else
            {
                max = (Integer) list.get(0);
            }
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(method.getMethod(), method.getMethod().getDeclaringClass());
            Page temp = annotation instanceof Page ? (Page) annotation : null;
            PageInfo page = new PageInfo();
            page.setPageCount(temp.defaultCount());
            PageInfo pageInfo = Util.initPageInfo(request, page);
            //设置最大数据量信息
            pageInfo.setMaxCount(max);
            //设置最大页信息，如果max 为0 对应为0
            pageInfo.setMaxPage(max != 0 ? (max % pageInfo.getPageCount() == 0 ? max / pageInfo.getPageCount() :
                    1 + (max / pageInfo.getPageCount())) : 0);
            request.setAttribute(Cost.PAGE_ATTR_NAME, pageInfo);
            //得到转换之后的分页查询语句，由于可能会出现以后不适用hibernate 所以这里不使用 hibernate的分页
            query = session.createQuery(oql.toString())
                    .setFirstResult((pageInfo.getPageNo() - 1) * pageInfo.getPageCount())
                    .setMaxResults(pageInfo.getPageCount());
            pageInfo.setUrl(request.getRequestURI());;
            request.setAttribute(Cost.PAGE_INFO,pageInfo);
        }
        else
        {
            query = session.createQuery(oql.toString());
        }

        for (int i = 0; i < par.getSize(); i++)
        {
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(tigerClass, filds[i], Between.class);
            Between between = (annotation instanceof Between) ? (Between) annotation : null;
            if (between != null)
            {
                String start = Util.getParameterValue(request, between.startField());
                String end = Util.getParameterValue(request, between.endField());
                if (!StringUtils.isNull(start))
                {
                    query.setParameter(filds[i] + "_" + between.startField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), start));
                }
                if (!StringUtils.isNull(end))
                {
                    query.setParameter(filds[i] + "_" + between.endField(),
                            Util.parsueData(tigerClass.getDeclaredField(filds[i]), end));
                }
            }
            else
            {
                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }

        }
        List<Object> list = query.list();
        session.close();
        //如果是集合类型目前只拓展 List其他的Map之类后续拓展
        if (Collection.class.isAssignableFrom(method.getParameterType()))
        {
            return list;
        }
        else
        {
            return (list == null || list.size() == 0 ? null : list.get(0));
        }
    }

    private Object FindSQLEntity(HttpServletRequest request, Find find,
                                 Class tigerClass, MethodParameter method) throws Exception
    {
        BaseDaoImlp baseDaoImlp = new BaseDaoImlp(sessionFactory);
        String entityClass = find.entityClass();
        Class<?> forName = Class.forName(entityClass);
        InitMsg par = Util.initValue(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        StringBuilder oql = new StringBuilder();
        SQLQuery query = null;
        if (par.isId()) //如果是id查询
        {
            oql.append(find.SQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Cell.class);
                Cell cell = (annotation2 instanceof Cell) ? (Cell) annotation2 : null;
                String asName = cell == null ? filds[i] : cell.columnName();
                oql.append(" AND ").append(asName).append("=:").append(asName);
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);
        }
        else if (par.isIskeys())
        {
            oql.append(find.SQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;

                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Cell.class);
                Cell cell = (annotation2 instanceof Cell) ? (Cell) annotation2 : null;
                String asName = cell == null ? filds[i] : cell.columnName();
                if (findKey != null)
                {
                    oql.append(" AND ").append(asName).append(" " + findKey.selectType() + " :").append(
                            asName);
                }
                else
                {
                    oql.append(" AND ").append(asName).append("=:").append(asName);
                }
            }
            String orderByStr = Util.getOrderByStr("", tigerClass);
            oql.append(orderByStr);

        }
        else
        {
            oql.append(find.SQL()).append("  where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;
                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Cell.class);
                Cell cell = (annotation2 instanceof Cell) ? (Cell) annotation2 : null;
                String asName = cell == null ? filds[i] : cell.columnName();
                if (findKey != null)
                {
                    oql.append(" AND ").append(asName).append(" " + findKey.selectType() + " :").append(
                            asName);
                }
                else
                {
                    oql.append(" AND ").append(asName).append("=:").append(asName);
                }
            }
            oql.append(Util.getOrderByStr("", tigerClass));

        }
        //必须是List 而且还拥有Page注解才走分页操作
        boolean isPageList = (Collection.class.isAssignableFrom(method.getParameterType()) && Util.isPageList(method));
        if (isPageList)
        {
            String pageCountSQL = pageIntefac.getPageCountSQL(oql.toString());
            Query countQuery = session.createSQLQuery(pageCountSQL);
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation2 =
                        Util.getAnnotation(tigerClass, filds[i], Cell.class);
                Cell cell = (annotation2 instanceof Cell) ? (Cell) annotation2 : null;
                String asName = cell == null ? filds[i] : cell.columnName();
                countQuery.setParameter(asName,
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }
            List list = countQuery.list();
            Integer max = null;
            if (list == null || list.size() == 0) max = 0;
            else
            {
                max = (Integer) list.get(0);
            }
            java.lang.annotation.Annotation annotation =
                    Util.getAnnotation(method.getMethod(), method.getMethod().getDeclaringClass());
            Page temp = annotation instanceof Page ? (Page) annotation : null;
            PageInfo page = new PageInfo();
            page.setPageCount(temp.defaultCount());
            PageInfo pageInfo = Util.initPageInfo(request, page);
            //设置最大数据量信息
            pageInfo.setMaxCount(max);
            //设置最大页信息，如果max 为0 对应为0
            pageInfo.setMaxPage(max != 0 ? (max % pageInfo.getPageCount() == 0 ? max / pageInfo.getPageCount() :
                    1 + (max / pageInfo.getPageCount())) : 0);
            //得到转换之后的分页查询语句，由于可能会出现以后不适用hibernate 所以这里不使用 hibernate的分页
            query = session.createSQLQuery(pageIntefac.getPageListSQL(oql.toString(), pageInfo));
            pageInfo.setUrl(request.getRequestURI());;
            request.setAttribute(Cost.PAGE_INFO,pageInfo);
        }
        else
        {
            query = session.createSQLQuery(oql.toString());
        }
        Map<String, Object> valueMap = new HashMap<String, Object>();
        for (int i = 0; i < par.getSize(); i++)
        {
            java.lang.annotation.Annotation annotation2 =
                    Util.getAnnotation(tigerClass, filds[i], Cell.class);
            Cell cell = (annotation2 instanceof Cell) ? (Cell) annotation2 : null;
            String asName = cell == null ? filds[i] : cell.columnName();
            valueMap.put(asName, Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            query.setParameter(asName,
                    Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
        }

        List<Object> list = baseDaoImlp.getListBySQL(oql.toString(), tigerClass, valueMap);
        session.close();
        //如果是集合类型目前只拓展 List其他的Map之类后续拓展
        if (Collection.class.isAssignableFrom(method.getParameterType()))
        {
            return list;
        }
        else
        {
            return (list == null || list.size() == 0 ? null : list.get(0));
        }
    }

    /**
     * 持久层实体新增
     *
     * @param request
     * @param tigerClass
     * @param method
     * @return
     * @throws Exception
     */
    private Object SaveEntity(HttpServletRequest request,
                              Class tigerClass, MethodParameter method) throws Exception
    {
        InitMsg par = Util.initValueForSave(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        boolean issave = false;
        Object et = tigerClass.newInstance();
        java.lang.annotation.Annotation annotation =
                Util.getClassAnnotation(tigerClass, Verify.class);
        Verify verify = (annotation instanceof Verify) ? (Verify) annotation : null;
        for (int i = 0; i < par.getSize(); i++)
        {
            for (Field field : tigerClass.getDeclaredFields())
            {
                if (field.getName().equals(filds[i]))
                {
                    Util.setData(field, values[i], et);
                }
            }
        }
        boolean isPass = true;
        //如果有这个注解说明需要 校验外部方法
        if (verify != null)
        {
            String classPath = verify.classPath();
            Class aClass = null;
            try
            {
                //找到校验的类
                aClass = Class.forName(classPath);
            }
            catch (ClassNotFoundException e)
            {//类没有找到不匹配}
            }
            //使用springde 容器代理生成对象。这样可以实现依赖注入
            Object bean = ApplicationContextUtil.getApplicationContext().getBean(aClass);
            for (Method methods : aClass.getDeclaredMethods())
            {
                //找到需要代理的方法
                if (methods.getName().equals(verify.MethodName()))
                {
                    //代理执行验证方法。原则上这个方法会返回一个 boolean值
                    isPass = (Boolean) methods.invoke(bean, et);
                }
            }
            // Method declaredMethod = aClass.getDeclaredMethod(verify.MethodName());
            // isPass = (boolean) declaredMethod.invoke(bean, et);
        }
        //如果外部方法已经通过验证这验证必填的字段

        if (isPass)
        {
            isPass = CheckUtil.checkFiled(et, tigerClass);
        }
        else
        {
            if (et instanceof EBase)
            {
                EBase base = (EBase) et;
                base.setSuccess(false);
                base.setMsg("未通过校验！");
            }
        }
        // Method m1 = tigerClass.getDeclaredMethod(verify.MethodName());
        //issave = (boolean) m1.invoke(et);
        if (isPass)
        {//检查通过存储
            session.save(et);
            session.close();
            if (et instanceof EBase)
            {
                EBase base = (EBase) et;
                base.setSuccess(true);
                base.setMsg("新增成功！");
            }
            return et;
        }
        if (session.isOpen())
        {
            session.close();
        }
        return et;
    }

    /**
     * 持久层实体修改
     *
     * @param request
     * @param tigerClass
     * @param method
     * @return
     * @throws Exception
     */
    private Object updateEntity(HttpServletRequest request,
                                Class tigerClass, MethodParameter method) throws Exception
    {
        InitMsg par = Util.initValueForSave(request, Util.getParment(tigerClass), method);
        Session session = sessionFactory.openSession();
        String[] filds = par.getFilds();
        Object[] values = par.getValues();
        boolean issave = false;
        Object et = initData(request, Annotation.FIND, tigerClass, method);
        java.lang.annotation.Annotation annotation = Util.getClassAnnotation(tigerClass, Verify.class);
        Verify verify = (annotation instanceof Verify) ? (Verify) annotation : null;
        for (int i = 0; i < par.getSize(); i++)
        {
            for (Field field : tigerClass.getDeclaredFields())
            {
                if (field.getName().equals(filds[i]))
                {
                    Util.setData(field, values[i], et);
                }
            }
        }
        boolean isPass = true;
        //如果有这个注解说明需要 校验外部方法
        if (verify != null)
        {
            String classPath = verify.classPath();
            Class aClass = null;
            try
            {
                //找到校验的类
                aClass = Class.forName(classPath);
            }
            catch (ClassNotFoundException e)
            {//类没有找到不匹配}
            }
            //使用springde 容器代理生成对象。这样可以实现依赖注入
            Object bean = ApplicationContextUtil.getApplicationContext().getBean(aClass);
            for (Method methods : aClass.getDeclaredMethods())
            {
                //找到需要代理的方法
                if (methods.getName().equals(verify.MethodName()))
                {
                    //代理执行验证方法。原则上这个方法会返回一个 boolean值
                    isPass = (Boolean) methods.invoke(bean, et);
                }
            }
            // Method declaredMethod = aClass.getDeclaredMethod(verify.MethodName());
            // isPass = (boolean) declaredMethod.invoke(bean, et);
        }
        //如果外部方法已经通过验证这验证必填的字段
        if (isPass)
        {
            isPass = CheckUtil.checkFiled(et, tigerClass);
        }
        // Method m1 = tigerClass.getDeclaredMethod(verify.MethodName());
        //issave = (boolean) m1.invoke(et);
        if (isPass)
        {//检查通过存储
            session.save(et);
            session.close();
            return et;
        }
        if (session.isOpen())
        {
            session.close();
        }
        return et;
    }

    private Object deleteEntity(HttpServletRequest request,
                                Class tigerClass, MethodParameter method) throws Exception
    {
        Session session = sessionFactory.openSession();
        Object relust = initData(request, Annotation.FIND, tigerClass, method);
        EBase base = new EBase();
        if (relust != null)
        {
            if (Collection.class.isAssignableFrom(method.getParameterType()))
            {
                Transaction transaction = session.beginTransaction();
                transaction.begin();
                try
                {
                    List list = (List) relust;
                    for (Object obj : list)
                    {
                        session.delete(obj);
                    }
                    transaction.commit();
                    if (list != null && list.size() > 0)
                    {
                        base.setSuccess(true);
                        base.setMsg("删除" + list.size() + "条记录成功！");
                    }
                    else
                    {
                        base.setSuccess(false);
                        base.setMsg("没有找到需要删除的记录！");
                    }
                }
                catch (Exception e)
                {
                    base.setSuccess(false);
                    base.setMsg("删除记录发生未知错误！");
                }
                finally
                {
                    session.close();
                }
            }
            else
            {
                try
                {
                    session.delete(relust);
                    base.setSuccess(true);
                    base.setMsg("删除1条成功！");
                }
                finally
                {
                    session.close();
                }

            }
        }
        else
        {
            base.setSuccess(false);
            base.setMsg("没有找到需要删除的数据！");
        }

        return base;
    }
}
