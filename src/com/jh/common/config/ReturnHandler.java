package com.jh.common.config;

import com.alibaba.fastjson.JSONObject;
import com.jh.Interceptor.Json;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**返回处理器
 * @author liyabin
 * @date 2017-08-22下午 5:31
 */
@Component
public class ReturnHandler implements HandlerMethodReturnValueHandler
{
    public ReturnHandler()
    {
    }

    /**
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter)
    {
        return methodParameter.getMethod().getAnnotation(Json.class)!=null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest)
            throws Exception
    {
        modelAndViewContainer.setRequestHandled(true);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(JSONObject.toJSONString(returnValue));
            out.flush();
        } catch (IOException e) {
            throw e;
        }
    }
}
