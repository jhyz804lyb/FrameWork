package com.jh.common.config;

import com.jh.Interceptor.Json;
import org.springframework.core.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import java.util.List;

/**
 * @author liyabin
 * @date 2017-08-22下午 3:22
 */
public class JsonResolver extends AbstractMessageConverterMethodProcessor
{

    public JsonResolver(List<HttpMessageConverter<?>> converters)
    {
        super(converters);
        System.out.println("-----------------"+converters.size());
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter)
    {
        System.out.print("A");
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception
    {
        System.out.print("B");
        Object arg =
                readWithMessageConverters(nativeWebRequest, methodParameter, methodParameter.getGenericParameterType());
        String name = Conventions.getVariableNameForParameter(methodParameter);
        WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, arg, name);
        if (arg != null)
        {
            validateIfApplicable(binder, methodParameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, methodParameter))
            {
                throw new MethodArgumentNotValidException(methodParameter, binder.getBindingResult());
            }
        }
        modelAndViewContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        return arg;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter)
    {
        System.out.print("C");
        return methodParameter.getMethodAnnotation(Json.class) != null;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest)
            throws Exception
    {
        System.out.print("D");
        modelAndViewContainer.setRequestHandled(true);
        writeWithMessageConverters(o, methodParameter, nativeWebRequest);
    }
}
