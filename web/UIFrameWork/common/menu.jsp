<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="/UIFrameWork/common/menu.css" rel="stylesheet"/>
<div class="main-menu a">
    <ul class="nav nav-pills nav-stacked">

        <c:forEach var="item" items="${requestScope.menus}">
            <c:if test="${item.level==1}">
                <li class="${item.orderId==0?'active':''}">
                    <a href="${item.url}" class="${item.url==null?'dropdown-toggle':''}"
                       data-toggle="${item.url==null?'dropdown':''}" target="mainBox">${item.menuName}${item.url==null?"<b class='caret'></b>":""}</a>

                    <ul class="dropdown-menu">
                        <c:forEach var="item2" items="${requestScope.menus}">
                            <c:if test="${item2.level==2 && item2.parentId==item.menuId}">
                                <li>
                                    <a href="${item2.url}" class="${item2.url==null?'dropdown-toggle':''}"
                                       data-toggle="${item2.url==null?'dropdown':''}" target="mainBox">${item2.menuName}
                                            ${item2.url==null?"<b class='caret'></b>":""}
                                    </a>
                                    <c:if test="${item2.url==null}">
                                        <c:forEach var="item3" items="${requestScope.menus}">
                                            <ul class="dropdown-menu">
                                                <c:if test="${item3.level==3 && item3.parentId==item2.menuId}">
                                                    <li>
                                                        <a href="${item3.url}" target="mainBox">${item2.menuName}</a>
                                                    </li>
                                                </c:if>
                                            </ul>
                                        </c:forEach>
                                    </c:if>

                                </li>

                            </c:if>
                        </c:forEach>
                    </ul>
                </li>

            </c:if>
        </c:forEach>
    </ul>
</div>