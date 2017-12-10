package com.jh.entity;

import com.jh.Interceptor.*;

import javax.persistence.*;

/**
 * @author liyabin
 * @date 2017/11/16
 */
@Entity(name = "menu")
@Table(name = "MENU")
@Verify(classPath = "com.jh.vilidata.Vilidata", MethodName = "checkMenuInfo")
public class Menu extends EBase {
    @Column(name = "menuId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ListField(showName = "菜单主键")
    private Integer menuId;

    @Column(name = "parentId")
    private Integer parentId;

    @Column(name = "menuName")
    @ListField(showName = "菜单名称")
    private String menuName;

    @Column(name = "url")
    @ListField(showName = "请求地址")
    private String url;

    @Column(name = "level")
    @NotNull
    @OrderByField(orderId = 0)
    private Integer level;

    @Column(name = "orderId")
    @OrderByField(orderId = 1)
    @ListField(showName = "序号")
    private Integer orderId;

    public Menu() {
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
