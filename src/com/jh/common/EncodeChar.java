package com.jh.common;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author liyabin
 * @date 2017-08-21下午 5:59
 */
public class EncodeChar extends OncePerRequestFilter
{
    private String encoding;
    private boolean forceEncoding = false;

    public EncodeChar() {
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setForceEncoding(boolean forceEncoding) {
        this.forceEncoding = forceEncoding;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(this.encoding);
            if(this.forceEncoding) {
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding(this.encoding);
            }
        }

        filterChain.doFilter(request, response);
    }
}
