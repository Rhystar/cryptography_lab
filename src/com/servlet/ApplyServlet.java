package com.servlet;

import com.bean.Certificate;
import com.dao.CertificateData;
import com.util.*;
import com.util.Key;
import org.springframework.util.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/applyServlet")
public class ApplyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String organization = request.getParameter("organization");
        String registration_number = request.getParameter("registration_number");
        String juridical_person = request.getParameter("juridical_person");
        String charge_person = request.getParameter("charge_person");
        String charge_phone = request.getParameter("charge_phone");
        String ttl = request.getParameter("ttl");
        String pk = request.getParameter("pk");
        String sign_organization = request.getParameter("sign_organization");
        String sign_registration_number = request.getParameter("sign_registration_number");
        String sign_juridical_person = request.getParameter("sign_juridical_person");
        String sign_charge_person = request.getParameter("sign_charge_person");
        String sign_charge_phone = request.getParameter("sign_charge_phone");
        String sign_ttl = request.getParameter("sign_ttl");
        RSAPrivateKey privateKey;
        try {
            privateKey = EncodingDecoding.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOZ+aoIMcSx2MVAn/XjDFH/p8Wpj10Np7f1lnIsHqX7mWh4KBwRrEkGZ6kZxb4QEmV8oUA6lJmmKOSl0oeEO+2ZvIy+nNh6pQank27Kra2JsqvNNI8V++YPG7QFEHBY8Z/cANQHLrTO1tQ/bTGPnVVeLhXGOT3h7gkGrNg3cJGG9AgMBAAECgYBwItiWZI863lWndY0vj1kN0jcNV32G4qZSPXknepbPkioNqzs2vxCmscb0doOWatZjIS2xsk850XF15bRL1gogI1GPgvfoic4XabP3tqqnw/WV1vh3myjkr6oSSU3rlbGDCN6sYFTRqMJmOL+RSDCkU+5ww947DZ4d3QRXNYCkUQJBAPfVcR+XLo+0izDWuVe0rK6IeLsmat/YtA8B2XbhSrMh6y/cKiN9yk0aTWKcS2RLGJaK8BXXjzXCWuhLJAp7GasCQQDuFrTDGulHAcoDi28b4Fhsa27PqrKfo1VyXUgVbPxjboHKXBN/c7UYJwUvIHynH5lkZnm+fYIVZ66IUdADw5o3AkBGUY5mWzv/1EdGFTbDduUkJF61I0JhvxffxjOQsn3Cc9ZKXxqptVBILjVUzGnrzA7u7/8NA3uD0mB+1oskWic/AkAE0iLg3Heiv2+GuNkMGHPR5i79N3iccOM3CJqADI/jt4YbQdgHOaGOFqQtOxwrCiHB/a0zZTkwE8Rd8EIlAV3rAkEAuULX+i6UroEPhyvlt8xvBE8ooGHaUgHwrXuRiN1gLUaiGMMLqRWbYDM1ZiebIRfoKFk7REsgzBc9CfIz6mOFNw==");

            organization = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(organization))), StandardCharsets.UTF_8);
            registration_number = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(registration_number))), StandardCharsets.UTF_8);
            juridical_person = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(juridical_person))), StandardCharsets.UTF_8);
            charge_person = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(charge_person))), StandardCharsets.UTF_8);
            charge_phone = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(charge_phone))), StandardCharsets.UTF_8);
            ttl = new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(ttl))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Objects.requireNonNull(SHADigest.getDigest(organization)).equalsIgnoreCase(sign_organization)
                || !Objects.requireNonNull(SHADigest.getDigest(registration_number)).equalsIgnoreCase(sign_registration_number)
                || !Objects.requireNonNull(SHADigest.getDigest(juridical_person)).equalsIgnoreCase(sign_juridical_person)
                || !Objects.requireNonNull(SHADigest.getDigest(charge_person)).equalsIgnoreCase(sign_charge_person)
                || !Objects.requireNonNull(SHADigest.getDigest(charge_phone)).equalsIgnoreCase(sign_charge_phone)
                || !Objects.requireNonNull(SHADigest.getDigest(ttl)).equalsIgnoreCase(sign_ttl)) {

            request.setAttribute("isApplied", "报文可能被损毁！");
            request.getRequestDispatcher("apply.jsp").forward(request, response);
            return;
        }
        System.out.println("sha ok");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("organization", organization);
        parameters.put("registration_number", registration_number);
        parameters.put("juridical_person", juridical_person);
        parameters.put("charge_person", charge_person);
        parameters.put("charge_phone", charge_phone);
        parameters.put("valid_time", ttl);
        parameters.put("public_key", pk);
        System.out.println("put ok");
        try {
            String serial_number = getSerialNumber(new String[] {new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), parameters.get("organization")});
            List<String> cerLines = new ArrayList<>();
            cerLines.add("Serial Number: " + serial_number);
            cerLines.add("Sign Algorithm: sha1RSA");
            cerLines.add("Encrypt Algorithm: sha256");
            cerLines.add("HIT CS CA 2019, " + "www.hit.edu.cn, " + "Harbin, " + "CN");
            String[] valid_time = getTTL(Integer.parseInt(parameters.get("valid_time")));
            cerLines.add("Valid Time From: " + valid_time[0]);
            cerLines.add("Valid Time To: " + valid_time[1]);
            cerLines.add("Public Key: " + parameters.get("public_key"));
            cerLines.add("Sign: " + RSASignature.sign(parameters.get("public_key"), Key.loadKeyByFile(this.getServletContext().getRealPath("/download/sk.key")), "utf-8"));
            cerLines.add("Organization: " + organization);
            cerLines.add("Charge Person: " + parameters.get("charge_person"));

            String upload_cer_path = makeCertificate(cerLines);
            CertificateData certificateDao = new CertificateData();
            var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Certificate ca = new Certificate(serial_number, upload_cer_path, sdf.parse(valid_time[0]), sdf.parse(valid_time[1]), request.getSession().getAttribute("username").toString());
            certificateDao.register(ca);
            request.setAttribute("serial_number", cerLines.get(0).substring(cerLines.get(0).indexOf(":") + 2));
            request.setAttribute("organization", organization);
            request.setAttribute("charge_person", parameters.get("charge_person"));
            request.getRequestDispatcher("apply_success.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    private String makeCertificate(List<String> cerLines) {
        String cer_path = this.getServletContext().getRealPath("/download/");
        String cer_name = cerLines.get(0).substring(cerLines.get(0).indexOf(":") + 2);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cer_path + cer_name)));
            for (String cerLine : cerLines) {
                System.out.println(cerLine);
                bufferedWriter.write(cerLine);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cer_path + cer_name;
    }

    private String getSerialNumber(String[] message) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : message) {
            stringBuilder.append(s);
        }
        return DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes());
    }

    private String[] getTTL(int ttl) {
        String present_time =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Calendar temp = Calendar.getInstance();
        temp.add(Calendar.YEAR, ttl);
        String future_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(temp.getTime());
        return new String[] {present_time, future_time};
    }
}
