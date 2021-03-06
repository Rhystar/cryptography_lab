<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<!-- Head -->
<head>

    <title>数字证书认证中心</title>

    <!-- Meta-Tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <script type="application/x-javascript">
        addEventListener("load", function () {
            setTimeout(hideURLbar, 0);
        }, false);

        function hideURLbar() {
            window.scrollTo(0, 1);
        }

        function applyJump() {
            window.location.href = "/cryptography_lab_war_exploded/apply.jsp";
        }

        function downloadJump() {
            window.location.href = "download_cer.jsp";
        }

        function revokeJump() {
            window.location.href = "revoke_cer.jsp";
        }

        function downloadCRLJump() {
            window.location.href = "download_crl.jsp";
        }
    </script>

    <!-- //Meta-Tags -->

    <!-- Style -->
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all">

</head>
<!-- //Head -->

<!-- Body -->
<body>
<span style="text-align: right; font-family: 微软雅黑; font-weight: bold; font-size: 20px; margin-top:
20px; margin-right: 20px; color: #000000"><%=request.getSession().getAttribute("username")%>，欢迎您</span>
<br>
<h1>数字证书认证中心</h1>

<div class="item-container">
    <div class="item-button">
        <ul>
            <li>
                <input type="submit" value="申 请 证 书" onclick="applyJump()">
            </li>
            <li>
                <form action="/cryptography_lab_war_exploded/downloadGenServlet?filename=genkey.exe" method="post">
                    <input type="submit" value="密 钥 生 成 器">
                </form>
            </li>
            <li>
                <input type="submit" value="下 载 证 书" onclick="downloadJump()">
            </li>
            <li>
                <input type="submit" value="撤 销 证 书" onclick="revokeJump()">
            </li>
            <li>
                <form action="/cryptography_lab_war_exploded/downloadCRLServlet?filename=crl.xml" method="post">
                    <input type="submit" value="下 载 CRL">
                </form>
            </li>
            <li>
                <form action="/cryptography_lab_war_exploded/logoutServlet" method="post">
                    <input type="submit" value="退 出 登 录">
                </form>
            </li>
        </ul>


    </div>
</div>


</body>
<!-- //Body -->

</html>
