package com.jh.common.config;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.*;

/**
 * @author liyabin
 * @date 2017-08-22下午 3:39
 */
//@Configuration
public class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport
{
    public MyWebMvcConfigurationSupport()
    {
    }

    //@Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter()
    {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new DataMessageConvert());
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
        argumentResolvers.add(new JsonResolver(converters));
        requestMappingHandlerAdapter.setCustomArgumentResolvers(argumentResolvers);
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
        returnValueHandlers.add(new JsonResolver(converters));
        requestMappingHandlerAdapter.setCustomReturnValueHandlers(returnValueHandlers);
        return requestMappingHandlerAdapter;
    }


}
