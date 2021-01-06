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
    <script>
        window.onload = function () {
            document.getElementById("apply_check_img").onclick = function () {
                this.src = "/tw/checkCodeServlet?time=" + new Date().getTime();
            };
        }
    </script>
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
<a href="/tw/logoutServlet"
   style="text-align: right; font-family: 楷体; font-weight: normal; font-size:
   20px; margin-top:
   20px; margin-right: 20px; color: #FFFFFF">退出登录</a>
<h1>哥谭市数字证书认证中心</h1>
<div class="container w3layouts agileits" style="width: 40%;">
    <form action="/tw/applyServlet" method="post" enctype="multipart/form-data" id="apply_form">
        <input type="text" name="organization" id="organization" placeholder="组织机构" required="">
        <input type="text" name="sign_organization" id="sign_organization" style="display: none">
        <input type="text" name="registration_number" id="registration_number" placeholder="工商注册号"
               required="">
        <input type="text" name="sign_registration_number" id="sign_registration_number"
               style="display: none">
        <input type="text" name="juridical_person" id="juridical_person" placeholder="法人姓名"
               required="">
        <input type="text" name="sign_juridical_person" id="sign_juridical_person"
               style="display: none">
        <input type="text" name="charge_person" id="charge_person" placeholder="经办人姓名"
               required="">
        <input type="text" name="sign_charge_person" id="sign_charge_person" style="display: none">
        <input type="text" name="charge_phone" id="charge_phone" placeholder="经办人电话" required="">
        <input type="text" name="sign_charge_phone" id="sign_charge_phone" style="display: none">
        <div class="valid_time_radio">
            <label><input type="radio" name="valid_time" value="1">1 年</label>
            <label><input type="radio" name="valid_time" value="2">2 年</label>
            <label><input type="radio" name="valid_time" value="3">3 年</label>
        </div>
        <input type="text" style="width: 70%;" disabled="true" id="upload_file_name"
               name="upload_file_name" placeholder="请选择密钥文件">
        <input type="button" id="upload_btn" name="upload_btn" value="选择上传文件" style="font-family:
         微软雅黑; font-size: 18px; float: left">
        <input type="file" id="upload_file" name="upload_file" style="display: none">
        <input type="password" name="check_code" placeholder="验证码" required="" style="width: 50%">
        <img src="/tw/checkCodeServlet" id="apply_check_img" style="float: left;">
        <span><%=request.getAttribute("isApplied") == null ?
                "" :
                request.getAttribute("isApplied")%></span>
        <span><%=request.getAttribute("checkcode_error") == null ?
                "" :
                request.getAttribute("checkcode_error")%></span>
        <div class="send-button">
            <input type="button" id="submit_btn" value="提 交">
        </div>
    </form>
    <div class="clear"></div>

</div>
</body>
<!-- //Body -->
<script>
    var upload_btn = document.getElementById("upload_btn");
    var upload_file = document.getElementById("upload_file");
    var upload_file_name = document.getElementById("upload_file_name");
    upload_btn.addEventListener("click", function () {
        upload_file.click();
    });
    upload_file.addEventListener("change", function () {
        upload_file_name.value = upload_file.value;
    });
</script>

<script>
    var submit_btn = document.getElementById("submit_btn");
    var organization = document.getElementById("organization");
    var registration_number = document.getElementById("registration_number");
    var juridical_person = document.getElementById("juridical_person");
    var charge_person = document.getElementById("charge_person");
    var charge_phone = document.getElementById("charge_phone");
    var sign_organization = document.getElementById("sign_organization");
    var sign_registration_number = document.getElementById("sign_registration_number");
    var sign_juridical_person = document.getElementById("sign_juridical_person");
    var sign_charge_person = document.getElementById("sign_charge_person");
    var sign_charge_phone = document.getElementById("sign_charge_phone");
    submit_btn.addEventListener("click", function () {
        var radios = document.getElementsByName("valid_time");
        var checked;
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) {
                checked = radios[i].value;
            }
        }
        var message = "请确认信息无误：\n" +
            organization.getAttribute("placeholder") + ": " + organization.value + "\n" +
            registration_number.getAttribute("placeholder") + ": " + registration_number.value + "\n" +
            juridical_person.getAttribute("placeholder") + ": " + juridical_person.value + "\n" +
            charge_person.getAttribute("placeholder") + ": " + charge_person.value + "\n" +
            charge_phone.getAttribute("placeholder") + ": " + charge_phone.value + "\n" + "有效期: " + checked + " 年";
        var r = confirm(message);
        if (r == true) {
            apply_encrypt();
            document.getElementById("apply_form").submit();
            organization.value = "";
            registration_number.value = "";
            juridical_person.value = "";
            charge_person.value = "";
            charge_phone.value = "";
        } else {
        }
    });

    function apply_encrypt() {
        var publicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLFxU/IBjwf0UjGsXy/dx6QiRH6pvvZoGvtUtDhGT1Wq0Vga2h7CUKwNazN8/l8YksKgz01JqIEh8NUAGOFY3tVMk/sNlMgYqCWyRw6QvMtspLDe3dzJn/83qAsMq3wr3Ooxgk143AXV6YlXhDvipYqwasP7luwRrbgdTANItl3wIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_organization = encrypt.encrypt(organization.value);
        var enc_registration_number = encrypt.encrypt(registration_number.value);
        var enc_juridical_person = encrypt.encrypt(juridical_person.value);
        var enc_charge_person = encrypt.encrypt(charge_person.value);
        var enc_charge_phone = encrypt.encrypt(charge_phone.value);
        var sign_organization = hex_sha256(organization.value);
        var sign_registration_number = hex_sha256(registration_number.value);
        var sign_juridical_person = hex_sha256(juridical_person.value);
        var sign_charge_person = hex_sha256(charge_person.value);
        var sign_charge_phone = hex_sha256(charge_phone.value);
        // var keyHex = CryptoJS.enc.Utf8.parse("9f6fuTsdFk8YMIwp");
        // var enc_organization = CryptoJS.DES.encrypt(organization.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        // var enc_registration_number = CryptoJS.DES.encrypt(registration_number.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        // var enc_juridical_person = CryptoJS.DES.encrypt(juridical_person.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        // var enc_charge_person = CryptoJS.DES.encrypt(charge_person.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        // var enc_charge_phone = CryptoJS.DES.encrypt(charge_phone.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        organization.value = enc_organization;
        registration_number.value = enc_registration_number;
        juridical_person.value = enc_juridical_person;
        charge_person.value = enc_charge_person;
        charge_phone.value = enc_charge_phone;
        this.sign_organization.value = sign_organization;
        this.sign_registration_number.value = sign_registration_number;
        this.sign_juridical_person.value = sign_juridical_person;
        this.sign_charge_person.value = sign_charge_person;
        this.sign_charge_phone.value = sign_charge_phone;
    }

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
