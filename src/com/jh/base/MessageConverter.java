package com.jh.base;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * @author liyabin
 * @date 2017-08-22上午 9:50
 */
public class MessageConverter extends StringHttpMessageConverter
{
    private static final MediaType utf8 = new MediaType("text", "plain", Charset.forName("UTF-8"));

    @Override
    protected MediaType getDefaultContentType(String dumy) {
        return utf8;
    }
    public MessageConverter()
    {
    }
}
