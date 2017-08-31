package com.jh.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApplicationContextUtil implements ApplicationContextAware
{

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext ac)
            throws BeansException
    {
        applicationContext = ac;
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    /**
     * 根据Class类型在IOC容器中获取对象
     *
     * @param clazz Class类型
     * @return 对象
     */
    public static <T> List<T> getBeanByType(Class<T> clazz)
    {
        List<T> list = new ArrayList<T>();
        
        /* 获取接口的所有实例名 */
        String[] beanNames = applicationContext.getBeanNamesForType(clazz);
        System.out.println("getBeanByType beanNames : " + beanNames == null ? "" : Arrays.toString(beanNames));

        if (beanNames == null || beanNames.length == 0)
        {
            return list;
        }
        T t = null;
        for (String beanName : beanNames)
        {
            t = (T) applicationContext.getBean(beanName);
            list.add(t);
        }
        return list;
    }

}