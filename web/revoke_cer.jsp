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
    <script src="js/crypto-js/crypto-js.js"></script>
    <script src="js/crypto-js/core.js"></script>
    <script src="js/crypto-js/cipher-core.js"></script>
    <script src="js/crypto-js/tripledes.js"></script>
    <script src="js/crypto-js/mode-ecb.js"></script>
    <script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
    <script src="js/sha256-min.js"></script>
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
    <form action="/tw/revokeCerServlet" method="post" id="revoke_form">
        <input type="text" name="serial_number" id="serial_number" placeholder="证书序列号" required="">
        <input type="text" name="sign_serial_number" id="sign_serial_number" style="display: none">
        <input type="password" name="revoke_pwd" id="revoke_pwd" placeholder="密码" required="">
        <input type="text" name="sign_revoke_pwd" id="sign_revoke_pwd" style="display: none">
        <span><%=request.getAttribute("msg") == null ? "" : request.getAttribute("msg")%></span>
        <div class="send-button">
            <input type="button" id="submit_btn" value="提 交">
        </div>
    </form>
    <div class="clear"></div>

</div>
</body>
<!-- //Body -->
<script>
    var submit_btn = document.getElementById("submit_btn");
    var serial_number = document.getElementById("serial_number");
    var revoke_pwd = document.getElementById("revoke_pwd");
    var sign_serial_number = document.getElementById("sign_serial_number");
    var sign_revoke_pwd = document.getElementById("sign_revoke_pwd");
    submit_btn.addEventListener("click", function () {
        var message = "您确定要撤销该证书吗？\n" +
            document.getElementById("serial_number").getAttribute("placeholder") + ": " +
            document.getElementById("serial_number").value + "\n";
        var r = confirm(message);
        if (r == true) {
            revoke_encrypt();
            document.getElementById("revoke_form").submit();
            serial_number.value = "";
            revoke_pwd.value = "";
        } else {

        }
    });

    function revoke_encrypt() {
        var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiEctQMqqeQjnS1vU6BXOaVThhanUcOT67jziw25NuVUKEHjZgs194OOz5IZ0w9UGtzn59opBwy3295UKb9r2QLMXTFWh88zeZ7KF4LLNesnYhwy0MIXknb6lxnJ7Dfnz5K+vgGJd0O0LmTUJDQ+xnlgpkh7x2jVrDJglcU5M0aQIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_serial_number = encrypt.encrypt(serial_number.value);
        var enc_revoke_pwd = encrypt.encrypt(revoke_pwd.value);
        var sign_serial_number = hex_sha256(serial_number.value);
        var sign_revoke_pwd = hex_sha256(revoke_pwd.value);
        serial_number.value = enc_serial_number;
        revoke_pwd.value = enc_revoke_pwd;
        this.sign_serial_number.value = sign_serial_number;
        this.sign_revoke_pwd.value = sign_revoke_pwd;
    }

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
