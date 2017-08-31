package com.jh.utils;

import com.jh.Interceptor.*;
import com.jh.common.InitMsg;
import enums.Annotation;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class DataBindUnit
{
    @Autowired
    protected SessionFactory sessionFactory;




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

                }
                //如果这个查询使用OQL语句来执行
                else if (!StringUtils.isNull(parameterAnnotation.OQL()))
                {

                }
                //如果这次查询使用SQL语句来执行
                else if(!StringUtils.isNull(parameterAnnotation.SQL()))
                {

                }
                break;
            case DELETE:
                break;

            case UPDATE:
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
        if (par.isId())
        {//如果是id查询
            Session session = sessionFactory.openSession();
            String[] filds = par.getFilds();
            Object[] values = par.getValues();
            StringBuilder oql = new StringBuilder();
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
            }
            oql.append(Util.getOrderByStr("", tigerClass));
            Query query = session.createQuery(oql.toString());
            for (int i = 0; i < par.getSize(); i++)
            {
                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }
            List<Object> list = query.list();
            session.close();
            //如果是集合类型目前只拓展 List其他的Map之类后续拓展
            if (java.util.Collection.class.isAssignableFrom(method.getParameterType()))
            {
                return list;
            }
            else
            {
                return (list == null || list.size() == 0 ? null : list.get(0));
            }
        }
        else if (par.isIskeys())
        {
            Session session = sessionFactory.openSession();
            String[] filds = par.getFilds();
            Object[] values = par.getValues();
            StringBuilder oql = new StringBuilder();
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {
                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;
                if (findKey != null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                }
            }
            oql.append(Util.getOrderByStr("",tigerClass));
            Query query = session.createQuery(oql.toString());
            for (int i = 0; i < par.getSize(); i++)
            {

                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }
            List<Object> list = query.list();
            session.close();
            //如果是集合类型目前只拓展 List其他的Map之类后续拓展
            if (java.util.Collection.class.isAssignableFrom(method.getParameterType()))
            {
                return list;
            }
            else
            {
                return (list == null || list.size() == 0 ? null : list.get(0));
            }
        }
        else
        {
            Session session = sessionFactory.openSession();
            String[] filds = par.getFilds();
            Object[] values = par.getValues();
            StringBuilder oql = new StringBuilder();
            oql.append("FROM ").append(tigerClass.getName()).append(" e where 1 = 1");
            for (int i = 0; i < par.getSize(); i++)
            {

                java.lang.annotation.Annotation annotation =
                        Util.getAnnotation(tigerClass, filds[i], FindKey.class);
                FindKey findKey = (annotation instanceof FindKey) ? (FindKey) annotation : null;
                if (findKey != null)
                {
                    oql.append(" AND ").append(filds[i]).append(" " + findKey.selectType() + " :").append(
                            filds[i]);
                }
                else
                {
                    oql.append(" AND ").append(filds[i]).append("=:").append(filds[i]);
                }
            }
            oql.append(Util.getOrderByStr("",tigerClass));
            Query query = session.createQuery(oql.toString());
            for (int i = 0; i < par.getSize(); i++)
            {
                query.setParameter(filds[i],
                        Util.parsueData(tigerClass.getDeclaredField(filds[i]), values[i].toString()));
            }
            List<Object> list = query.list();
            session.close();
            //如果是集合类型目前只拓展 List其他的Map之类后续拓展
            if (java.util.Collection.class.isAssignableFrom(method.getParameterType()))
            {
                return list;
            }
            else
            {
                return (list == null || list.size() == 0 ? null : list.get(0));
            }
        }
    }



    private Object FindDTOEntity(HttpServletRequest request, Annotation type,
                                  Class tigerClass, MethodParameter method) throws Exception
     {
         return null;
     }

    private Object FindOQLEntity(HttpServletRequest request, Annotation type,
                                 Class tigerClass, MethodParameter method) throws Exception
    {
        return null;
    }
    private Object FindSQLEntity(HttpServletRequest request, Annotation type,
                                 Class tigerClass, MethodParameter method) throws Exception
    {
        return null;
    }

    /**
     * 持久层实体
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
        boolean issave=false;
        Object et= tigerClass.newInstance();
        java.lang.annotation.Annotation annotation =
                Util.getClassAnnotation(tigerClass, Verify.class);
        Verify verify = (annotation instanceof Verify) ? (Verify) annotation : null;
            for (int i = 0; i <par.getSize(); i++)
            {

                if (verify != null)
                {
                    for (Field field : tigerClass.getDeclaredFields()){
                        if (field.getName().equals(filds[i])){
                            Util.setData(field,values[i],et);
                        }

                    }
                }
            }
        Method m1=tigerClass.getDeclaredMethod(verify.MethodName());
        issave= (boolean) m1.invoke(et);
        if (issave){//检查通过存储
            Session s = sessionFactory.openSession();
            s.save(et);
            session.close();
            return  issave;
        }
        return issave;
    }
}
