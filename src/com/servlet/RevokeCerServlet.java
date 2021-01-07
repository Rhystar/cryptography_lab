package com.servlet;

import com.bean.Certificate;
import com.bean.User;
import com.dao.CertificateData;
import com.dao.UserData;
import com.util.EncodingDecoding;
import com.util.SHADigest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@WebServlet("/revokeCerServlet")
public class RevokeCerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        CertificateData certificateDao = new CertificateData();
        String serial_number = request.getParameter("serial_number");
        String revoke_pwd = request.getParameter("revoke_pwd");
        String sign_serial_number = request.getParameter("sign_serial_number");
        String sign_revoke_pwd = request.getParameter("sign_revoke_pwd");
        RSAPrivateKey privateKey;
        try {
            privateKey = EncodingDecoding.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==");
            serial_number = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(serial_number))), StandardCharsets.UTF_8);
            revoke_pwd = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(revoke_pwd))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(serial_number)).equalsIgnoreCase(sign_serial_number) || !Objects.requireNonNull(SHADigest.getDigest(revoke_pwd)).equalsIgnoreCase(sign_revoke_pwd)) {
            request.setAttribute("msg", "消息已损坏！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
            return;
        }
        String username = request.getSession().getAttribute("username").toString();
        UserData userDao = new UserData();
        User presentUser = new User(username, revoke_pwd);
        if (!userDao.verify(presentUser)) {
            request.setAttribute("msg", "撤销身份验证密码错误！请您确认登录信息后输入正确的验证密码！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
            return;
        }
        // String[] revoke_msg = certificateDao.revoke(serial_number);
        boolean revoke_msg = certificateDao.revoke(serial_number);
        if (revoke_msg) {
            request.setAttribute("msg", "此证书已被成功撤销！");
            List<Certificate> crlList = certificateDao.getCrl();
            ServletContext servletContext = this.getServletContext();
            String file_path = servletContext.getRealPath("/download");
            File file = new File(file_path);
            if (!file.exists()) {
                boolean flag;
                flag = file.mkdirs();
                if (!flag)
                    throw new IOException("新建文件夹失败");
            }
            String file_name = file_path + "/" + "crl.xml";
            file = new File(file_name);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("<?xml version='1.0' encoding='gbk'?>");
            bufferedWriter.write("<crls>\n");
            var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Certificate myCrl : crlList) {
                bufferedWriter.write("<crl>\n");
                bufferedWriter.write(
                        "<serial_number>" + myCrl.getSerialNumber() + "</serial_number>\n");
                bufferedWriter.write(
                        "<organization>" + myCrl.getUsername() + "</organization" + ">\n");
                bufferedWriter.write("<start_time>" + sdf.format(myCrl.getNotBefore()) + "</start_time>\n");
                bufferedWriter.write("<end_time>" + sdf.format(myCrl.getNotAfter()) + "</end_time>\n");
                bufferedWriter.write("</crl>\n");
                bufferedWriter.flush();
            }
            bufferedWriter.write("</crls>\n");
            bufferedWriter.close();
            fileWriter.close();
            request.setAttribute("serial_number", serial_number);
            request.getRequestDispatcher("revoke_result.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "此证书不存在或已失效！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
        }

    }
}
