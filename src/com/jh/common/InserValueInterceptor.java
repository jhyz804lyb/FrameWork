package com.jh.common;

import com.jh.Interceptor.*;
import com.jh.utils.*;
import enums.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

public class InserValueInterceptor implements HandlerInterceptor
{

    @Autowired
    DataBindUnit dataBindUnit;

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception
    {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception
    {

    }

    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                             Object arg2) throws Exception
    {
        //如果不是方法级别的请求，结束当前拦截器
        if (!(arg2 instanceof HandlerMethod)) return true;
        //得到当前请求的方法
        HandlerMethod method = (HandlerMethod) arg2;
        //得到请求参数
        MethodParameter[] temp = method.getMethodParameters();
        boolean hasAnnotation = false;
        for (MethodParameter parameter : temp)
        {
            for (java.lang.annotation.Annotation an : parameter.getParameterAnnotations())
            {
                //由于可能出现添加成功后返回实体对象所以这里要考虑到 add 和 find 以及其他注解同时标注在一个参数上的情况
                if (an instanceof Find)
                {
                    //得到具体的实体class
                    Class classType = Util.getClassByMethodParameter(parameter);
                    Object obj = dataBindUnit.initData(arg0, Annotation.FIND, classType, parameter);
                    arg0.setAttribute(Cost.BEAN_TYPE, classType);
                    if (obj != null)
                    {
                        arg0.setAttribute(Cost.FRAME_PARAM, obj);
                    }
                }
                if (an instanceof Add)
                {
                    //如果是 Add 注解的参数。则 去数据库匹配对象
                    //得到具体的实体class
                    Class classType = Util.getClassByMethodParameter(parameter);
                    Object obj = dataBindUnit.initData(arg0, Annotation.ADD, classType, parameter);
                    if (obj != null)
                    {
                        arg0.setAttribute(Add.class.getName(), obj);
                    }

                }
                if (an instanceof SaveOrUpdate)
                {
                    //如果是 Add 注解的参数。则 去数据库匹配对象
                    //得到具体的实体class
                    Class classType = Util.getClassByMethodParameter(parameter);
                    Object obj = dataBindUnit.initData(arg0, Annotation.FIND, classType, parameter);
                    if (obj != null)
                    {
                        arg0.setAttribute(Add.class.getName(), obj);
                    }
                }
                if (an instanceof Delete)
                {
                    //如果是 Delete 注解的参数。则 去数据库删除对象
                    //得到具体的实体class
                    Class classType = Util.getClassByMethodParameter(parameter);
                    Object obj = dataBindUnit.initData(arg0, Annotation.ADD, classType, parameter);
                    if (obj != null)
                    {
                        arg0.setAttribute(Add.class.getName(), obj);
                    }

                }
            }
        }
        return true;
    }

}
