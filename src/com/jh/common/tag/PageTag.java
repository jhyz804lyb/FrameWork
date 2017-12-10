package com.jh.common.tag;

import com.jh.base.PageInfo;
import com.jh.common.Cost;
import com.jh.common.enmu.RequestType;
import com.jh.utils.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PageTag extends SimpleTagSupport {

    private Object data;

    private RequestType requestType;

    private String formId;

    public void setData(Object data) {
        this.data = data;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void init() {
        if (data == null) {
            data = this.getJspContext().findAttribute(Cost.PAGE_INFO);
        }
        if (requestType == null) {
            requestType = RequestType.GET;
        }
    }

    public void doTag() throws JspException, IOException {
        init();
        if (data == null || !(data instanceof PageInfo)) return;//如果没有分页信息
        if (RequestType.POST.equals(requestType) && StringUtils.isEmpty(formId)) return;//如果post请求里面没有表单id
        if (RequestType.GET.equals(requestType)) {
            StringBuilder html = new StringBuilder();
            html.append("<nav class='text-center' aria-label=\"...\">\n" +
                    "         <ul class=\"pagination\">");
            html.append(createHtml((PageInfo) data));
            html.append("</ul>\n </nav>");
            getJspContext().getOut().println(html.toString());
        }
    }

    public String createHtml(PageInfo pageInfo) {
        StringBuilder html = new StringBuilder();
        int pageNo = pageInfo.getPageNo();
        int maxCount = pageInfo.getMaxPage();
        if (pageNo == 1 || pageNo == 0)
            html.append("<li class=\"disabled\"><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
        else
            html.append("<li><a href=\"" + createUrl(pageNo - 1, pageInfo.getUrl()) + "\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
        int count = 1;
        for (int i = 1; i <= maxCount; i++) {
            if (i + 5 < pageNo) continue;
            if (count > 10) break;
            count++;
            if (i == pageNo)
                html.append("<li class=\"active\"><a class='disabled' href=\"#\">" + i + " <span class=\"sr-only\">(current)</span></a></li>");
            else {
                html.append("<li><a  href=\"" + createUrl(i, pageInfo.getUrl()) + "\">" + i + " <span class=\"sr-only\">(current)</span></a></li>");
            }
        }
        if (pageNo == maxCount)
            html.append("<li class='disabled'><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">»</span></a></li>");
        else
            html.append("<li><a href=\""+createUrl(pageNo+1,pageInfo.getUrl())+"\" aria-label=\"Next\"><span aria-hidden=\"true\">»</span></a></li>");
        return html.toString();
    }

    public String createUrl(int pageNo, String url) {
        if (url.contains("pageNo")) {
            int index = url.indexOf("pageNo");
            String temp = url.substring(index);
            int fastLen = temp.indexOf("&");
            if (fastLen == -1) {
                return url.substring(index) + "pageNo=" + pageNo;
            } else {
                return url.substring(index) + "pageNo=" + pageNo + temp.substring(fastLen);
            }
        } else {
            if (url.contains("?")) {
                return url.replace("?", "?pageNo=" + pageNo + "&");
            } else {
                return url + "?pageNo=" + pageNo;
            }
        }
    }

}
