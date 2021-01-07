package com.servlet;

import com.bean.*;
import com.dao.*;
import com.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Objects;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String sign_username = req.getParameter("sign_username");
        String sign_password = req.getParameter("sign_password");
        RSAPrivateKey privateKey;
        try {
            privateKey = EncodingDecoding.loadPrivateKeyByStr(
                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw=="
            );
            username = new String(Objects.requireNonNull(EncodingDecoding.decrypt(
                    privateKey, Base64.getDecoder().decode(username))), StandardCharsets.UTF_8
            );
            password = new String(Objects.requireNonNull(EncodingDecoding.decrypt(
                    privateKey, Base64.getDecoder().decode(password))), StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(username)).equalsIgnoreCase(sign_username)
                || !Objects.requireNonNull(SHADigest.getDigest(password)).equalsIgnoreCase(sign_password)) {
            req.setAttribute("login_error", "登录失败！报文可能被损坏！");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        User loginUser = new User(username, password);
        HttpSession session = req.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        UserData userDao = new UserData();
        boolean verified = userDao.verify(loginUser); // 验证用户身份
        if (verified) {
            session.setAttribute("username", username);
            resp.sendRedirect(req.getContextPath() + "/home.jsp");
        } else {
            req.setAttribute("login_error", "用户名或密码错误");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
