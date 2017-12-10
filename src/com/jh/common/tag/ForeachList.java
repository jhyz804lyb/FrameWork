package com.jh.common.tag;

import com.jh.Interceptor.ListField;
import com.jh.common.Cost;
import com.jh.utils.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Collection;

public class ForeachList extends SimpleTagSupport {
    /**
     * 加载的要加载的List
     */
    private Object data;
    /**
     * 前端入参代表要显示的列
     */
    private String fields;
    /**
     * 列名
     */
    private String titleNames;

    private String msg;
    /**
     * 文字方向左对齐还是右对齐
     */
    private String textAlign;

    private String[] titleList;

    private String[] fieldList;

    private String[] textAligns;
    /**
     * 如果设置这个属性将会覆盖默认样式
     */
    private String tableStyle;

    private String addStyle;

    private int maxCell;

    private Class beanType;

    private String btnType;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setBtnType(String btnType) {
        this.btnType = btnType;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setTitleNames(String titleNames) {
        this.titleNames = titleNames;
    }

    public void setBeanType(Class beanType) {
        this.beanType = beanType;
    }

    public void setTextAligns(String[] textAligns) {
        this.textAligns = textAligns;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public void setTableStyle(String tableStyle) {
        this.tableStyle = tableStyle;
    }

    public void setAddStyle(String addStyle) {
        this.addStyle = addStyle;
    }

    public void doTag() throws JspException, IOException {
        if (data == null) {  //这里获取数据源
            data = this.getJspContext().findAttribute(Cost.FRAME_PARAM);
        }
        if (beanType == null) {
            beanType = (Class) this.getJspContext().findAttribute(Cost.BEAN_TYPE);
        }
        if (data == null) return;
        else {
            //如果是集合类型的数据
            if (Collection.class.isAssignableFrom(data.getClass())) {
                Collection list = ((Collection) data);
                Class type = list.iterator().hasNext() ? list.iterator().next().getClass() : null;
                if (type == null) type = beanType;
                if (type == null) return;
                init(type);//初始化要展示的列和表头信息
                int index = 0;
                StringBuilder html = new StringBuilder();
                maxCell = fieldList.length > titleList.length ? fieldList.length : titleList.length;
                html.append("<table class='" + tableStyle + "'>");
                html.append(createTile());
                if (list.size() == 0) html.append(noDateMsg());
                for (Object obj : list) {
                    html.append("<tr class='" + (index % 2 == 0 ? "even" : "Odd") + " tr" + (++index) + "'>");
                    try {
                        html.append(getBtn(index));
                        html.append(createNode(type, obj));
                    } catch (Exception e) {
                    }
                    html.append("</tr>");
                }
                html.append("</table>");
                getJspContext().getOut().println(html.toString());
            } else {//如果不是集合对象，可以考虑派发给另一个自定义标签解析生成HTML代码

            }
        }
    }

    /**
     * 原则上优先以页面上定义的优先级较高，实体上面注解优先级较低。
     * 排版都是使用bootstrap上面的样式 class 类
     *
     * @param type 实体类的Class
     */
    public void init(Class type) {
        if (StringUtils.isEmpty(addStyle)) {
            addStyle = "";
        }
        if (StringUtils.isEmpty(tableStyle)) {
            tableStyle = "table table-striped table-bordered table-hover";
        }
        tableStyle = tableStyle + " " + addStyle;
        if (!StringUtils.isEmpty(textAlign)) {
            textAligns = textAlign.split(",");
        }
        //页面上定义了要展示的列
        if (!StringUtils.isEmpty(fields)) {
            fieldList = fields.split(",");
        }
        //页面上定义了表头
        if (!StringUtils.isEmpty(titleNames)) {
            titleList = titleNames.split(",");
        }
        //如果列集合还是为空那么去实体里面映射
        if (fieldList == null || titleList == null) {
            Field[] parment = Util.getParment(type);
            Object[] temp = ReflectionUtil.getFieldArray(type, ListField.class);
            //对结果进行排序
            temp = Util.sortArray(temp, new SortInteface() {
                public boolean compare(Object obj1, Object obj2) {
                    if (obj1 instanceof Field && obj2 instanceof Field) {
                        Field s1 = (Field) obj1;
                        Field s2 = (Field) obj2;
                        Annotation annotation1 = ReflectionUtil.getAnnotation(s1, ListField.class);
                        Annotation annotatio2 = ReflectionUtil.getAnnotation(s2, ListField.class);
                        if (annotation1 != null && annotatio2 != null) {
                            return ((ListField) annotation1).orderId() <= ((ListField) annotatio2).orderId();
                        }
                    }
                    return false;
                }
            });
            boolean option = false;
            boolean title = false;
            if (fieldList == null) {
                fieldList = new String[temp.length];
                option = true;
            }
            if (!option || titleList == null) {
                title = true;
                titleList = new String[temp.length];
            }
            for (int i = 0; i < temp.length; i++) {
                if (option) {
                    Field field = (Field) temp[i];
                    ListField showField = (ListField) ReflectionUtil.getAnnotation(field, ListField.class);
                    if (!StringUtils.isEmpty(showField.reflectField())) {
                        fieldList[i] = field.getName() + "." + showField.reflectField();
                    } else
                        fieldList[i] = field.getName();
                }
                if (title) {
                    ListField showField = (ListField) ReflectionUtil.getAnnotation((Field) temp[i], ListField.class);
                    titleList[i] = showField.showName();
                }

            }
        }
        if (textAligns == null) {//默认设置为居中对齐
            int max = titleList.length > fieldList.length ? titleList.length : fieldList.length;
            textAligns = new String[max];
            for (int i = 0; i < max; i++) {
                textAligns[i] = "text-center";
            }
        } else {//如果有那么对其进行转意 L l 1 为左对齐 按照 bootStrap  框架依次序类推
            for (int i = 0; i < textAligns.length; i++) {
                StringBuilder style = new StringBuilder();//这里给表格上面加上样式
                for (int j = 0; j < textAligns[i].length(); j++) {//这么设计可以同时添加多个设计属性
                    if ("lL1".contains("" + textAligns[i].charAt(j))) {
                        style.append(" text-left");
                    } else if ("cC2".contains("" + textAligns[i].charAt(j))) {
                        style.append(" text-center");
                    } else if ("rR3".contains("" + textAligns[i].charAt(j))) {
                        style.append(" text-right");
                    } else if ("rR3".contains("" + textAligns[i].charAt(j))) {
                        style.append(" text-right");
                    } else if ("mM4".contains("" + textAligns[i].charAt(j))) {//内容是减弱的 --其他的都是有字体颜色的
                        style.append(" muted");
                    } else if ("pP5".contains("" + textAligns[i].charAt(j))) {
                        style.append(" primary");
                    } else if ("sS6".contains("" + textAligns[i].charAt(j))) {
                        style.append(" success");
                    } else if ("iI7".contains("" + textAligns[i].charAt(j))) {
                        style.append(" info");
                    } else if ("wW8".contains("" + textAligns[i].charAt(j))) {
                        style.append(" warning");
                    } else if ("dD9".contains("" + textAligns[i].charAt(j))) {
                        style.append(" danger");
                    } else {
                        style.append(UITools.getStyleClassByCode(textAligns[i].charAt(j)));
                    }
                }
                textAligns[i] = style.toString();
            }
        }
    }

    private String createNode(Class type, Object bean) throws ParseException, IllegalAccessException {
        StringBuilder html = new StringBuilder();
        int Flen = fieldList.length;
        int Clen = textAligns.length;
        for (int i = 0; i < maxCell; i++) {
            html.append("<td ").append("class='").append(i < Flen ? fieldList[i] : "").append(" ").append(i < Clen ? textAligns[i] : "").append("'").append(">");

            if (i < Flen) {
                Object filedVal = ReflectionUtil.getFiledVal(bean, fieldList[i], type);
                String result = Util.DataFormat(ReflectionUtil.getActualField(fieldList[i], type), filedVal);
                html.append(result);
            }
            html.append("</td>");
        }
        return html.toString();
    }

    /**
     * 构建表头
     *
     * @return
     */
    private String createTile() {
        StringBuilder html = new StringBuilder();
        html.append("<tr class='b-title'>");
        html.append(getBtn());
        for (int i = 0; i < maxCell; i++) {
            html.append("<th class='text-center'>").append(i < titleList.length ? titleList[i] : "").append("</th>");
        }
        html.append("</tr>");
        return html.toString();
    }

    private String noDateMsg() {
        return "<tr class='noData warning'><td class='text-center' colspan='" + maxCell + "'><h4>" + (StringUtils.isEmpty(msg) ? "没有更多数据" : msg) + "！</h4></td></tr>";
    }

    private String getBtn(int index) {
        StringBuilder html = new StringBuilder();
        if (StringUtils.isEmpty(btnType) || "none".toUpperCase().equals(btnType.toUpperCase())) return html.toString();
        if ("view".equals(btnType)) {
            html.append("<td class='text-center'>").append("<h6>").append(index).append("</h6>")
                    .append("</td>");
        }
        if ("checkbox".toUpperCase().equals(btnType.toUpperCase())) {
            html.append("<td class='text-center'>").append(" " +
                    "      <input type=\"checkbox\"  name='select" + index + "' value='" + index + "'>\n")
                    .append("</td>");
        }
        if ("radioBox".toUpperCase().equals(btnType.toUpperCase())) {
            html.append("<td class='text-center'>").append("" +
                    "    <input type=\"radio\" name=\"select\"  value=\"" + index + "\" >\n" ).append("</td>");
        }
        return html.toString();
    }

    public String getBtn() {
        StringBuilder html = new StringBuilder();
        if (StringUtils.isEmpty(btnType) || "none".equals(btnType)) return html.toString();
        if ("view".equals(btnType)) {
            html.append("<th class='text-center'>").append("<h6>").append("序号").append("</h6>")
                    .append("</th>");
        }
        if ("checkbox".toUpperCase().equals(btnType.toUpperCase())) {
            html.append("<th class='text-center'>").append(" " +
                    "      <input type=\"checkbox\" name='select''>\n")
                    .append("</th>");
        }
        if ("radioBox".toUpperCase().equals(btnType.toUpperCase())) {
            html.append("<th>").append("</th>");
        }
        return html.toString();
    }
}
