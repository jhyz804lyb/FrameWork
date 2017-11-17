<%--
  Created by IntelliJ IDEA.
  User: liyabin
  Date: 2017/11/16
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="/UIFrameWork/common/menu.css" rel="stylesheet"/>
<div class="main-menu">
  <ul class="nav nav-pills nav-stacked">
<c:forEach var="item" items="${menus}">
<c:if test="item.level==1">
  <li class="${item.orderId==0?'active':''}"><a href="#">${item.menuName}</a></li>
</c:if>
</c:forEach>
  </ul>
</div>