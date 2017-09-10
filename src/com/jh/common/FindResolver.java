package com.jh.common;

import com.jh.Interceptor.*;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class FindResolver implements HandlerMethodArgumentResolver {
    /**
     * 对请求参数注解中含有 Find 注解的参数 将调用自定义的解析器
     * @param parameter
     * @return
     */
	@Override
    public boolean supportsParameter(MethodParameter parameter) {
        return  parameter.getParameterAnnotation(Find.class) != null||parameter.getParameterAnnotation(Add.class) != null;
    }

    /**
     *  从请求中获取数据填充到 方法的参数中。由于拦截器已经做了相应的功能并把数据存放到attribute 中这里直接获取就可以了
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
	@Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
		Object result =webRequest.getAttribute(Find.class.getName(), WebRequest.SCOPE_REQUEST);
        result =result==null?webRequest.getAttribute(Add.class.getName(), WebRequest.SCOPE_REQUEST):result;
        return result; 
    }

}
