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
<span style="text-align: right; font-family: 微软雅黑; font-weight: bold; font-size: 20px; margin-top:
20px; margin-right: 20px; color: #000000"><%=request.getSession().getAttribute("username")%>，欢迎您</span>
<br>
<a href="/cryptography_lab_war_exploded/home.jsp"
   style="text-align: right; font-family: 微软雅黑; font-weight: normal; font-size:
   20px; margin-top:
   20px; margin-right: 20px; color: #000000">返回主页</a>
<h1>数字证书认证中心</h1>
<div class="container w3layouts agileits" style="width: 40%;">
    <form action="/cryptography_lab_war_exploded/applyServlet" method="post" id="apply_form">
        <input type="text" name="organization" id="organization" placeholder="网站名" required="">
        <input type="text" name="sign_organization" id="sign_organization" style="display: none">
        <input type="text" name="charge_person" id="charge_person" placeholder="申请人姓名" required="">
        <input type="text" name="sign_charge_person" id="sign_charge_person" style="display: none">
        <input type="text" name="charge_phone" id="charge_phone" placeholder="申请人电话" required="">
        <input type="text" name="sign_charge_phone" id="sign_charge_phone" style="display: none">
        <input type="text" name="ttl" id="ttl" placeholder="有效时间（年）" required="">
        <input type="text" name="sign_ttl" id="sign_ttl" style="display: none">
        <input type="text" name="pk" id="pk" placeholder="公钥" required="">
        <input type="text" name="sign_pk" id="sign_pk" style="display: none">
        <div class="send-button">
            <input type="button" id="submit_btn" value="提 交">
        </div>
    </form>
    <div class="clear"></div>
</div>
</body>

<script>
    var submit_btn = document.getElementById("submit_btn");
    var organization = document.getElementById("organization");
    var charge_person = document.getElementById("charge_person");
    var charge_phone = document.getElementById("charge_phone");
    var ttl = document.getElementById("ttl");
    var pk = document.getElementById("pk");
    var sign_organization = document.getElementById("sign_organization");
    var sign_charge_person = document.getElementById("sign_charge_person");
    var sign_charge_phone = document.getElementById("sign_charge_phone");
    var sign_ttl = document.getElementById("sign_ttl");
    var sign_pk = document.getElementById("sign_pk");
    submit_btn.addEventListener("click", function () {
        apply_encrypt();
        document.getElementById("apply_form").submit();
        organization.value = "";
        charge_person.value = "";
        charge_phone.value = "";
        ttl.value = "";
        pk.value = "";
    });

    function apply_encrypt() {
        var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmfmqCDHEsdjFQJ/14wxR/6fFqY9dDae39ZZyLB6l+5loeCgcEaxJBmepGcW+EBJlfKFAOpSZpijkpdKHhDvtmbyMvpzYeqUGp5Nuyq2tibKrzTSPFfvmDxu0BRBwWPGf3ADUBy60ztbUP20xj51VXi4Vxjk94e4JBqzYN3CRhvQIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_organization = encrypt.encrypt(organization.value);
        var enc_charge_person = encrypt.encrypt(charge_person.value);
        var enc_charge_phone = encrypt.encrypt(charge_phone.value);
        var enc_ttl = encrypt.encrypt(ttl.value);
        var sign_organization = hex_sha256(organization.value);
        var sign_charge_person = hex_sha256(charge_person.value);
        var sign_charge_phone = hex_sha256(charge_phone.value);
        var sign_ttl = hex_sha256(ttl.value);
        var sign_pk = hex_sha256(pk.value);
        organization.value = enc_organization;
        charge_person.value = enc_charge_person;
        charge_phone.value = enc_charge_phone;
        ttl.value = enc_ttl;
        this.sign_organization.value = sign_organization;
        this.sign_charge_person.value = sign_charge_person;
        this.sign_charge_phone.value = sign_charge_phone;
        this.sign_ttl.value = sign_ttl;
        this.sign_pk.value = sign_pk;
    }

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
