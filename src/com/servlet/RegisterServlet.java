package com.servlet;

import com.bean.*;
import com.util.*;
import com.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Objects;

@WebServlet(name = "registerServlet", value = "/registerServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sign_username = request.getParameter("sign_username");
        String sign_password = request.getParameter("sign_password");
        RSAPrivateKey privateKey;
        try {
            privateKey = EncodingDecoding.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==");
            username = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(username))), StandardCharsets.UTF_8);
            password = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(password))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(username)).equalsIgnoreCase(sign_username)
                || !Objects.requireNonNull(SHADigest.getDigest(password)).equalsIgnoreCase(sign_password)) {
            request.setAttribute("register_fail_msg", "注册失败！报文可能被损坏！");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        User registerUser = new User(username, password);
        UserData userDao = new UserData();
        boolean success_register = userDao.register(registerUser);
        if (!success_register) {
            request.setAttribute("register_fail_msg", "用户名已存在！");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("register_success_msg", "注册成功");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
