package com.jh.common.config;

import com.jh.Interceptor.Json;
import org.springframework.http.*;
import org.springframework.http.converter.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author liyabin
 * @date 2017-08-22下午 3:43
 */
public  class DataMessageConvert extends AbstractGenericHttpMessageConverter<Object> {
     @Override
     public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
         return ((Class) type).isAssignableFrom(Json.class);
     }
     @Override
     public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
         return ((Class) type).isAssignableFrom(Json.class);
     }
     public List<MediaType> getSupportedMediaTypes() {
         return Collections.singletonList(MediaType.ALL);
     }
     protected boolean supports(Class<?> clazz) {
         return clazz.isAssignableFrom(Map.class);
     }
     public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
         return readMap(inputMessage);
     }
     private Object readMap(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
         Charset cs = Charset.forName("UTF-8");
         StringBuilder stringBuilder = new StringBuilder();
         InputStream inputStream = inputMessage.getBody();
         byte[] b = new byte[1024];
         int length;
         while ((length = inputStream.read(b)) != -1) {
             ByteBuffer byteBuffer = ByteBuffer.allocate(length);
             byteBuffer.put(b, 0, length);
             byteBuffer.flip();
             stringBuilder.append(cs.decode(byteBuffer).array());
         }
         String[] list = stringBuilder.toString().split(";");
         Map<String, String> map = new HashMap<String, String>(list.length);
         for (String entry : list) {
             String[] keyValue = entry.split(",");
             map.put(keyValue[0], keyValue[1]);
         }

         return "";
     }
     public void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
         StringBuilder stringBuilder = new StringBuilder();
        // Map<String, String> map = ((Json) o).getData();

         stringBuilder.deleteCharAt(stringBuilder.length() - 1);
         outputMessage.getBody().write(stringBuilder.toString().getBytes());
     }
     public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
         return readMap(inputMessage);
     }
 }
