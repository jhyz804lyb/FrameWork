package com.jh.common.tag;

import com.jh.Interceptor.ShowField;
import com.jh.utils.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Collection;

/**
 * +采用bootstrap 布局循环遍历出所有list集合并使用栅格系统进行布局
 */
public class ListTag extends SimpleTagSupport {
    /**
     * 加载的要加载的List
     */
    private Object data;

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (data != null) {
            try {
                StringBuilder html = new StringBuilder();
                if (Collection.class.isAssignableFrom(data.getClass())) {
                    //得到具体的list 泛型中的Class
                    Collection list = ((Collection) data);

                    Class aClass = list.iterator().hasNext() ? list.iterator().next().getClass() : null;
                    int index = 0;
                    for (Object obj : (Collection) data) {
                        html.append("<div class='row "+(index%2==0?"even":"Odd")+" row" + (++index) + "'>");
                        html.append(createNode(aClass, obj));
                        html.append("</div>");
                    }

                }
                getJspContext().getOut().println(html.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        // JspWriter out = getJspContext().getOut();
        // out.println("Hello Custom Tag!");
    }

    private String createNode(Class type, Object bean) throws ParseException, IllegalAccessException {
        if (bean == null) return "";
        StringBuilder html = new StringBuilder();
        //统计将会展示的列的数量
        int count = 1;
        for (Field field : type.getDeclaredFields()) {
            Annotation annotation = Util.getAnnotation(field, ShowField.class);
            if (annotation != null) {
                count++;
            }
        }
        int baseWidth = 1;
        if (count == 1) baseWidth = 12;
        if (count == 2) baseWidth = 6;
        if (count == 3) baseWidth = 4;
        if (count == 4) baseWidth = 3;
        if (count == 5) baseWidth = 2;
        if (count == 6) baseWidth = 2;
        if (count == 7) baseWidth = 2;
        if (count == 8) baseWidth = 2;
        if (count == 9) baseWidth = 1;
        if (count == 10) baseWidth = 1;
        if (count == 11) baseWidth = 1;
        if (count == 12) baseWidth = 1;

        for (Field field : type.getDeclaredFields()) {
            Annotation annotation = Util.getAnnotation(field, ShowField.class);
            if (annotation != null)
            {
                field.setAccessible(true);
                html.append("<div class='col-md-"+baseWidth+" "+field.getName()+"'>");
                html.append(Util.convernData(field, field.get(bean)));
                html.append("</div>");
            }
        }
        return html.toString();
    }
}
