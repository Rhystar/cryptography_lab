package com.servlet;

import com.bean.Certificate;
import com.bean.User;
import com.dao.CertificateData;
import com.dao.UserData;
import com.util.EncodingDecoding;
import com.util.SHADigest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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

public class RevokeCerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //        String key = "7c6ffnDSak8YMiVq";
        CertificateData certificateDao = new CertificateData();
        //        String serial_number = DESUtil.decrypt(request.getParameter("serial_number"), key);
        //        String revoke_pwd = DESUtil.decrypt(request.getParameter("revoke_pwd"), key);
        String serial_number = request.getParameter("serial_number");
        String revoke_pwd = request.getParameter("revoke_pwd");
        String sign_serial_number = request.getParameter("sign_serial_number");
        String sign_revoke_pwd = request.getParameter("sign_revoke_pwd");
        RSAPrivateKey privateKey;
        try {
            privateKey =
                    EncodingDecoding.loadPrivateKeyByStr("MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKIRy1Ayqp5COdLW9ToFc5pVOGFqdRw5PruPOLDbk25VQoQeNmCzX3g47PkhnTD1Qa3Ofn2ikHDLfb3lQpv2vZAsxdMVaHzzN5nsoXgss16ydiHDLQwheSdvqXGcnsN+fPkr6+AYl3Q7QuZNQkND7GeWCmSHvHaNWsMmCVxTkzRpAgMBAAECgYEAimdFyEwsdpA5zzsxGoaTTaYfStnd/udIEmZh1G7/fYakEi225GfqTMHYZXz2P1wC5cnlLadJUHoG/McvVf+lq3+6ph2pA6wHI4bvMSPS9SoLkcLNiggcqAKnySu2X9ZNCEza+NGMj726IxsyxB7+3PCzSfiiwzv3JvxkyA614AECQQDen/yaEpO0F+mh4aQe6e9pj2xjJuLb7sooDDFOJaotxnY/1aaNdongbqA2j7HeBf6isv19F+w+nWKWXLVoOTQBAkEAul3HsmFH5z8fwiv+B9AGrK6vfbadBqlTeK1gMxqDPDUuDMNbW4rPRlCXEBaoUMSFRDFNMrfFPejnn5tu1pzgaQJBANvj8kDMcI/FvsJieRT/w7XkMA6PbiwF5C9CO8EQetLT4CCVCvlXSEAhhKXfsLO4ABb77F0OsA34rlQOJjBXsAECQQCS+cS07D2NpN3B/3nO5YNuCjICfdMm3sEiqfD1PJKFGBeiHytcbYN8G7CXEpdZYzMKjaspNX8LjTOmTynBfWUJAkEAtWOybjlu0zLSiC3gTTyK1Q+aS1eLji/a9l/XdG7L9dQ0fEpfB8XTtUQdmmt3vDAe3qwyV5t/vJkfZOrHY+yIYg==");
            serial_number =
                    new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(serial_number))), StandardCharsets.UTF_8);
            revoke_pwd =
                    new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(revoke_pwd))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(serial_number)).equalsIgnoreCase(sign_serial_number)
                || !Objects.requireNonNull(SHADigest.getDigest(revoke_pwd)).equalsIgnoreCase(sign_revoke_pwd)) {
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
            request.setAttribute("organization", certificateDao.getCa("serial_number").getUsername());
            request.getRequestDispatcher("revoke_result.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "此证书不存在或已失效！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
        }

    }
}
