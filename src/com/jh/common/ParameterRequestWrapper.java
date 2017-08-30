package com.jh.common;
import java.util.Enumeration;  
import java.util.Map;  
import java.util.Vector;  
  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletRequestWrapper;  
  
@SuppressWarnings("unchecked")  
public class ParameterRequestWrapper extends HttpServletRequestWrapper {  
  
    private Map params;  
  
    public ParameterRequestWrapper(HttpServletRequest request, Map newParams) {  
        super(request);  
        this.params = newParams;  
    }  
  
    public Map getParameterMap() {  
        return params;  
    }  
  
    public Enumeration getParameterNames() {  
        Vector l = new Vector(params.keySet());  
        return l.elements();
}
}