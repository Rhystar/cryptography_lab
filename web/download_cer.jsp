<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<!-- Head -->
<head>

    <title>数字证书认证中心</title>

    <!-- Meta-Tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <!-- //Meta-Tags -->

    <!-- Style -->
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all">
</head>
<!-- //Head -->

<!-- Body -->
<body>
<span style="text-align: right; font-family: 楷体; font-weight: bold; font-size: 20px; margin-top:
20px; margin-right: 20px; color: #FFFFFF"><%=request.getSession().getAttribute("username")%>，欢迎您</span>
<br>
<a href="/tw/logoutServlet"
   style="text-align: right; font-family: 楷体; font-weight: normal; font-size:
   20px; margin-top:
   20px; margin-right: 20px; color: #FFFFFF">退出登录</a>
<h1>数字证书认证中心</h1>
<div class="container" style="width: 40%;">
    <form action="/tw/downloadCerServlet" method="post">
        <input type="text" name="serial_number" id="serial_number" placeholder="证书序列号" required="">
        <span><%=request.getAttribute("msg") == null ? "" : request.getAttribute("msg")%></span>
        <div class="send-button">
            <input type="submit" id="submit_btn" value="提 交">
        </div>
    </form>
    <div class="clear"></div>

</div>
</body>
<!-- //Body -->
<script>

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
