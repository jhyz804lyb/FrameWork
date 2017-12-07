package com.jh.common.tag;

import com.jh.Interceptor.ShowField;
import com.jh.utils.Util;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Collection;

/**
 * 自定义标签测试
 *
 * @author liyabin
 * @date 2017/11/28
 */
public class demoTag extends SimpleTagSupport
{
    /**
     * 加载的要加载的List
     */
    private Object data;
    /**
     * 生成数据类型，
     * 1表格 ，2 行
     */
    private Integer type;

    private String baseCell;

    public demoTag()
    {
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public void setBaseCell(String baseCell)
    {
        this.baseCell = baseCell;
    }

    @Override
    public void doTag() throws JspException, IOException
    {
        if (data != null)
        {
            try
            {
               StringBuilder html = new StringBuilder();
                html.append("<table>");
                if (Collection.class.isAssignableFrom(data.getClass()))
                {
                    //得到具体的list 泛型中的Class
                    Collection list = ((Collection) data);

                    Class aClass =  list.iterator().hasNext()?list.iterator().next().getClass():null;
                    for (Object obj : (Collection) data)
                    {
                        html.append(createNode(aClass,obj));
                    }

                }
                html.append("</table>");
                getJspContext().getOut().println(html.toString());
            }
            catch (Exception e)
            {
                   System.out.println(e);
            }
        }

       // JspWriter out = getJspContext().getOut();
       // out.println("Hello Custom Tag!");
    }

    private String createNode(Class type, Object bean) throws ParseException, IllegalAccessException
    {
        if (bean == null) return "";
        StringBuilder html = new StringBuilder();
        if ("td".toUpperCase().equals(baseCell.toUpperCase()))
        {
            html.append("<tr>");
        }
        else
        {
            html.append("<ul>");
        }
        for (Field field : type.getDeclaredFields())
        {
            Annotation annotation = Util.getAnnotation(field, ShowField.class);
            if (annotation != null)
            {
                field.setAccessible(true);
                html.append("<").append(baseCell).append(">");
                html.append(Util.convernData(field, field.get(bean)));
                html.append("</").append(baseCell).append(">");
            }
        }
        if ("td".toUpperCase().equals(baseCell.toUpperCase()))
        {
            html.append("</tr>");
        }
        else
        {
            html.append("</ul>");
        }
        return html.toString();
    }
}
