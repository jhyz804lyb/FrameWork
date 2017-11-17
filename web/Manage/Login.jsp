<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="/UIFrameWork/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/Manage/css/signin.css" rel="stylesheet">
    <script src="/UIFrameWork/common/jquery-1.11.0.js"></script>
    <script src="/UIFrameWork/bootstrap/js/bootstrap.js"></script>
    <title>系统登录</title>
</head>
<body>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
    		<div class="modal-content">
    			<div class="modal-header">
    				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
    					&times;
    				</button>
    				<h4 class="modal-title" id="myModalLabel">
    					登录失败
    				</h4>
    			</div>
    			<div class="modal-body">
    				${msg}
    			</div>
    			<div class="modal-footer">
    				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
    				</button>
    			</div>
    		</div><!-- /.modal-content -->
    	</div><!-- /.modal -->
    </div>
    <div class="container">

        <form class="form-signin" action="/user/login" method="post">
            <h2 class="form-signin-heading">后台管理系统</h2>
            <label for="username" class="sr-only">用户名</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="用户名" required autofocus>
            <label for="inputPassword" class="sr-only">密码</label>
            <input type="password" id="inputPassword" name="pwd" class="form-control" placeholder="密码" required>

            <div class="checkbox">
                <label>
                    <input type="checkbox" value="remember-me"> 记住我
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>

    </div>
    <script>
        var msg = '${msg}';
        $(function ()
        {
            if (msg)
            {
                $('#myModal').modal('show');
            }
        })
    </script>
</body>

</html>
