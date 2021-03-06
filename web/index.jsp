<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<!-- Head -->
<head>

    <title>数字证书认证中心</title>

    <!-- Meta-Tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <script type="application/x-javascript">
        var msg = "<%=request.getAttribute("register_success_msg")%>";
        if (msg != "null") {
            window.alert(msg);
        }
        addEventListener("load", function () {
            setTimeout(hideURLbar, 0);
        }, false);

        function hideURLbar() {
            window.scrollTo(0, 1);
        }


    </script>

    <!-- //Meta-Tags -->

    <!-- Style -->
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all">

    <script src="js/jsencrypt-2.1.0/bin/jsencrypt.min.js"></script>
    <script src="js/sha256-min.js"></script>
    <script src="js/crypto-js/crypto-js.js"></script>
    <script src="js/crypto-js/core.js"></script>
    <script src="js/crypto-js/cipher-core.js"></script>
    <script src="js/crypto-js/tripledes.js"></script>
    <script src="js/crypto-js/mode-ecb.js"></script>


</head>
<!-- //Head -->

<!-- Body -->
<body>

<h1>数字证书认证中心</h1>

<div class="container w3layouts agileits">

    <div class="login w3layouts agileits">
        <h2>登 录</h2>
        <form action="/cryptography_lab_war_exploded/loginServlet" id="login_form" method="post">
            <input type="text" name="username" id="login_username" placeholder="用户名" required="">
            <input type="text" name="sign_username" id="login_sign_username" style="display: none">
            <input type="password" name="password" id="login_password" placeholder="密码" required="">
            <input type="text" name="sign_password" id="login_sign_password" style="display: none">
            <span><%=request.getAttribute("login_error") == null ? "" : request.getAttribute("login_error")%></span>
            <span><%=request.getAttribute("login_msg") == null ? "" : request.getAttribute("login_msg")%></span>
            <div class="send-button w3layouts agileits">
                <input type="button" id="login_btn" value="登 录">
            </div>
        </form>
        <div class="clear"></div>
    </div>

    <div class="register w3layouts agileits">
        <h2>注 册</h2>
        <form action="/cryptography_lab_war_exploded/registerServlet" id="register_form" method="post">
            <input type="text" name="username" id="register_username" placeholder="用户名" required="">
            <input type="text" name="sign_username" id="reg_sign_username" required="" style="display: none">
            <input type="password" name="password" id="register_password" placeholder="密码" required="" onkeyup="CheckIntensity(this.value)">
            <input type="text" name="sign_password" id="reg_sign_password" style="display: none">
            <span class="warn"><%=request.getAttribute("register_fail_msg") == null ? "" : request.getAttribute("register_fail_msg")%></span>
            <table border="0" cellpadding="0" cellspacing="0" style="margin-bottom: 10px">
                <tr align="center">
                    <td id="pwd_Weak" class="pwd pwd_c"></td>
                    <td id="pwd_Medium" class="pwd pwd_c pwd_f">无</td>
                    <td id="pwd_Strong" class="pwd pwd_c pwd_c_r"></td>
                </tr>
            </table>
            <div class="send-button w3layouts agileits">
                <input type="button" id="register_btn" value="注 册">
            </div>
        </form>
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
</div>


</body>
<!-- //Body -->
<script>
    var register_btn = document.getElementById("register_btn");
    var register_username = document.getElementById("register_username");
    var register_password = document.getElementById("register_password");
    var reg_sign_username = document.getElementById("reg_sign_username");
    var reg_sign_password = document.getElementById("reg_sign_password");
    register_btn.addEventListener("click", function () {
        var uPattern = /^[a-zA-Z0-9_-]{6,16}$/;
        if (!uPattern.test(register_username.value)) {
            alert("用户名必须为字母、数字、下划线、减号的组合，长度为6-16！");
            return;
        }
        var pPattern = /^(?=.*[a-zA-Z])(?=.*\d)[^]{6,20}$/;
        if (!pPattern.test(register_password.value)) {
            alert("密码长度至少为6位，至多20位！并且至少包括数字和字母！");
            return;
        }
        var cP = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        reg_encrypt();
        document.getElementById("register_form").submit();
        register_username.value = "";
        register_password.value = "";
    });

    function reg_encrypt() {
        var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_username = encrypt.encrypt(register_username.value);
        var enc_password = encrypt.encrypt(register_password.value);
        var sign_username = hex_sha256(register_username.value);
        var sign_password = hex_sha256(register_password.value);
        register_username.value = enc_username;
        register_password.value = enc_password;
        reg_sign_username.value = sign_username;
        reg_sign_password.value = sign_password;
    }

    var login_btn = document.getElementById("login_btn");
    var login_username = document.getElementById("login_username");
    var login_password = document.getElementById("login_password");
    var login_sign_username = document.getElementById("login_sign_username");
    var login_sign_password = document.getElementById("login_sign_password");
    login_btn.addEventListener("click", function () {
        login_encrypt();
        document.getElementById("login_form").submit();
        login_password.value = "";
        login_username.value = "";
    });

    function login_encrypt() {
        var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_username = encrypt.encrypt(login_username.value);
        var enc_password = encrypt.encrypt(login_password.value);
        var sign_username = hex_sha256(login_username.value);
        var sign_password = hex_sha256(login_password.value);
        login_username.value = enc_username;
        login_password.value = enc_password;
        login_sign_username.value = sign_username;
        login_sign_password.value = sign_password;
    }

    function CheckIntensity(pwd) {
        var Mcolor, Wcolor, Scolor, Color_Html;
        var m = 0;
        //匹配数字
        if (/\d+/.test(pwd)) {
            debugger;
            m++;
        }
        ;
        //匹配字母
        if (/[A-Za-z]+/.test(pwd)) {
            m++;
        }
        ;
        //匹配除数字字母外的特殊符号
        if (/[^0-9a-zA-Z]+/.test(pwd)) {
            m++;
        }
        ;

        if (pwd.length <= 6) {
            m = 1;
        }
        if (pwd.length <= 0) {
            m = 0;
        }
        switch (m) {
            case 1:
                Wcolor = "pwd pwd_Weak_c";
                Mcolor = "pwd pwd_c";
                Scolor = "pwd pwd_c pwd_c_r";
                Color_Html = "弱";
                break;
            case 2:
                Wcolor = "pwd pwd_Medium_c";
                Mcolor = "pwd pwd_Medium_c";
                Scolor = "pwd pwd_c pwd_c_r";
                Color_Html = "中";
                break;
            case 3:
                Wcolor = "pwd pwd_Strong_c";
                Mcolor = "pwd pwd_Strong_c";
                Scolor = "pwd pwd_Strong_c pwd_Strong_c_r";
                Color_Html = "强";
                break;
            default:
                Wcolor = "pwd pwd_c";
                Mcolor = "pwd pwd_c pwd_f";
                Scolor = "pwd pwd_c pwd_c_r";
                Color_Html = "无";
                break;
        }
        document.getElementById('pwd_Weak').className = Wcolor;
        document.getElementById('pwd_Medium').className = Mcolor;
        document.getElementById('pwd_Strong').className = Scolor;
        document.getElementById('pwd_Medium').innerHTML = Color_Html;
    }
</script>
</html>
