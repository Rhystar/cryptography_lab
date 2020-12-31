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

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sign_username = request.getParameter("sign_username");
        String sign_password = request.getParameter("sign_password");
        RSAPrivateKey privateKey = null;
        try {
            privateKey =
                    EncodingDecoding.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0Y4V5HSA3ZiDynKX0bfVdr9HKD6zEqlWvgMzqG/bXBJr5jMep3Z8Rkc+xeX6TwGgQyg7G5maIYDhwtJXm1axVoXxY5gAAv8vQJeC9UofgElzN+TweldC43dUcwKWCnHzimY2gkdSo3aQ78cB1y/vGfVjuSgBrG5AWIKq/eg1SlAgMBAAECgYEAk/1W04wBziUCxEUm6SyMadCpHL41YOMs0aJDNXjUMlyZz8KeHBua8E6VktVBETp/ge1ut7bDj+I3mMGUZK4gwOXfyYjRuZa01f3Z89X5aLDRkjOwIm0PmTgEiAEuAIQaYfj/c6Iru+TwCjq5QZyqjLY7ASJ0muzai/0tAOo30QECQQDf6ETVqHZ7WClnoE3JT2ZmQ8CUK/yDzPVnL1Q+HSn8StaUKuZavHGbU5KgrC9dnGQudNEOGPHQGT2tEcSxM2+FAkEA2DNW6YpmxlTUvbHRytMemXkjNVpdnEma5y8osUWDq+IeWnJXmBJu4D6T5K1gwyign5HjmNksNgYK1Fquv0MKoQJAMy0ARqE5a1msJP4zqTZXnjoQEw22ql03Hb1okMXTqdFlF/pyKfz2Ll08nzKbpNaw4xlaCtHSuxB500vDXAj4jQJAclErKo+o6kPevXLxyDo7mtEHweVHTCVLR+SSsrFb/x2wCQkeseVFRUMxdiAK4wZvcBB29NIYY3Rsc36DmdQ8IQJAPSpFpwru315UT4/QCCrq8b/wVA6BbKeptG+ySmhC6cpZVvk2kqtcQekJliGRqUOP0M9EeTofCALJ/Fvm7oD9Mg==");
            username =
                    new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(username))), StandardCharsets.UTF_8);
            password =
                    new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(password))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(username)).equalsIgnoreCase(sign_username) || !Objects.requireNonNull(SHADigest.getDigest(password)).equalsIgnoreCase(sign_password)) {
            request.setAttribute("register_fail_msg", "注册失败！报文可能被损坏！");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        User registerUser = new User(username, password);
        String checkCode = request.getParameter("check_code");
        HttpSession session = request.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        session.removeAttribute("checkCode_session");
        UserData userDao = new UserData();
        if (checkCode_session != null && checkCode_session.equalsIgnoreCase(checkCode)) {
            boolean success_register = userDao.register(registerUser);
            if (!success_register) {
                request.setAttribute("register_fail_msg", "用户名已存在！");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                request.setAttribute("register_success_msg", "注册成功");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("register_fail_msg", "验证码错误！");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
