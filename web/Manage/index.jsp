<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>后台管理</title>
    <jsp:include page="/UIFrameWork/common/common.jsp"/>
</head>

<body>
    <jsp:include page="/UIFrameWork/common/top.jsp"/>
    <div class="main">
        <div class="left-menu">
            <jsp:include page="/system/menuList"/>
        </div>
        <div class="body-main">
        </div>
    </div>
    <jsp:include page="/UIFrameWork/common/bottom.jsp"/>
</body>
</html>
