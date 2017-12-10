<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" uri="mytaglib" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/UIFrameWork/common/common.jsp"/>
    <title>Title</title>
</head>
<body>
<div class="container">
    <div class="panel panel-primary">
        <!-- Default panel contents -->
        <div class="panel-heading ">

            <div class="row">
                <div class="col-md-6">输入关键字查询</div>
                <div class="col-md-6 text-right">
                    <button type="button" class="btn btn-success">搜索</button>
                    <button type="button" class="btn btn-info">新增</button>
                    <button type="button" class="btn btn-warning">修改</button>
                    <button type="button" class="btn btn btn-danger">删除</button>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group form-group-sm">
                        <label class="col-sm-2 control-label" for="formGroupInputSmall">用户名：</label>
                        <div class="col-sm-10">
                            <input class="form-control" type="text" id="formGroupInputSmall" placeholder="Small input">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group form-group-sm">
                        <label class="col-sm-2 control-label" for="formGroupInputSmall">密码：</label>
                        <div class="col-sm-10">
                            <input class="form-control" type="text" id="formGroupInputSmall2" placeholder="Small input">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group form-group-sm">
                        <label class="col-sm-2 control-label" for="formGroupInputSmall">用户名：</label>
                        <div class="col-sm-10">
                            <input class="form-control" type="text" id="formGroupInputSmal5l" placeholder="Small input">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group form-group-sm">
                        <label class="col-sm-2 control-label" for="formGroupInputSmall">密码：</label>
                        <div class="col-sm-10">
                            <input class="form-control" type="text" id="formGroupInputSmall26" placeholder="Small input">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <my:LoadList btnType="checkbox"></my:LoadList>
        <div class="panel-footer"><my:PageInfo></my:PageInfo></div>
    </div>


</div>
</body>
</html>
