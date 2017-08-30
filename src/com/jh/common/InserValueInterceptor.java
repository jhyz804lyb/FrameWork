package com.jh.common;

import com.jh.Interceptor.Find;
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
            //如果是 Find 注解的参数。则 去数据库匹配对象
            hasAnnotation = parameter.hasParameterAnnotation(Find.class);
            if (hasAnnotation)
            {
                //得到具体的实体class
                Class classType = Util.getClassByMethodParameter(parameter);
                Object obj = dataBindUnit.initData(arg0, Annotation.FIND, classType, parameter);
                if (obj != null)
                {
                    arg0.setAttribute(Find.class.getName(), obj);
                }
            }
        }
        return true;
    }

}
