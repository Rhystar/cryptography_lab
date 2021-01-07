<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<!-- Head -->
<head>

    <title>哥谭市数字证书认证中心</title>

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
<a href="/tw/home.jsp"
   style="text-align: right; font-family: 楷体; font-weight: normal; font-size:
   20px; margin-top:
   20px; margin-right: 20px; color: #FFFFFF">返回主页</a>
<h1>哥谭市数字证书认证中心</h1>
<div class="container" style="width: 40%;">
    <h3><%=request.getAttribute("msg")%></h3>
    <p>
        被撤销证书序列号为：<%=request.getAttribute("serial_number")%> <br>
        证书撤销已被登记至 CRL 库！
    </p>
    <a href="/tw/downloadCRLServlet">CRL下载</a>
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
