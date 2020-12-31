package com.servlet;

import com.bean.*;
import com.dao.*;
import com.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Objects;

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
                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0Y4V5HSA3ZiDynKX0bfVdr9HKD6zEqlWvgMzqG/bXBJr5jMep3Z8Rkc+xeX6TwGgQyg7G5maIYDhwtJXm1axVoXxY5gAAv8vQJeC9UofgElzN+TweldC43dUcwKWCnHzimY2gkdSo3aQ78cB1y/vGfVjuSgBrG5AWIKq/eg1SlAgMBAAECgYEAk/1W04wBziUCxEUm6SyMadCpHL41YOMs0aJDNXjUMlyZz8KeHBua8E6VktVBETp/ge1ut7bDj+I3mMGUZK4gwOXfyYjRuZa01f3Z89X5aLDRkjOwIm0PmTgEiAEuAIQaYfj/c6Iru+TwCjq5QZyqjLY7ASJ0muzai/0tAOo30QECQQDf6ETVqHZ7WClnoE3JT2ZmQ8CUK/yDzPVnL1Q+HSn8StaUKuZavHGbU5KgrC9dnGQudNEOGPHQGT2tEcSxM2+FAkEA2DNW6YpmxlTUvbHRytMemXkjNVpdnEma5y8osUWDq+IeWnJXmBJu4D6T5K1gwyign5HjmNksNgYK1Fquv0MKoQJAMy0ARqE5a1msJP4zqTZXnjoQEw22ql03Hb1okMXTqdFlF/pyKfz2Ll08nzKbpNaw4xlaCtHSuxB500vDXAj4jQJAclErKo+o6kPevXLxyDo7mtEHweVHTCVLR+SSsrFb/x2wCQkeseVFRUMxdiAK4wZvcBB29NIYY3Rsc36DmdQ8IQJAPSpFpwru315UT4/QCCrq8b/wVA6BbKeptG+ySmhC6cpZVvk2kqtcQekJliGRqUOP0M9EeTofCALJ/Fvm7oD9Mg=="
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
        String checkCode = req.getParameter("check_code");
        HttpSession session = req.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        session.removeAttribute("checkCode_session");
        UserData userDao = new UserData();
        if (checkCode_session != null && checkCode_session.equalsIgnoreCase(checkCode)) { // 判断验证码
            boolean verified = userDao.verify(loginUser); // 验证用户身份
            if (verified) {
                session.setAttribute("username", username);
                resp.sendRedirect(req.getContextPath() + "/home.jsp");
            } else {
                req.setAttribute("login_error", "用户名或密码错误");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("checkCode_error", "验证码错误");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
