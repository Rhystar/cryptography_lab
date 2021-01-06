/*package com.servlet;

import com.bean.Certificate;
import com.dao.CertificateData;
import com.util.EncodingDecoding;
import com.util.Key;
import com.util.RSASignature;
import com.util.SHADigest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/applyServlet")
public class ApplyServlet111 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String upload_path = this.getServletContext().getRealPath("/upload");
        File file = new File(upload_path);
        if (!file.exists()) {
            boolean flag;
            flag = file.mkdirs();
            if (!flag)
                throw new IOException("新建文件夹失败");
        }
        Map<String, String> parameters = new HashMap<>();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> fileItemList = servletFileUpload.parseRequest(request);
                for (FileItem fileItem : fileItemList) {
                    if (!fileItem.isFormField()) {
                        String file_name = fileItem.getName();
                        if (file_name.isEmpty()) {
                            return;
                        }
                        InputStream inputStream = fileItem.getInputStream();
                        String line;
                        StringBuilder content = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        while ((line = bufferedReader.readLine()) != null) {
                            content.append(line);
                        }
                        bufferedReader.close();
                        upload_path += "\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".key";
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(upload_path)));
                        bufferedWriter.write(content.toString());
                        parameters.put("public_key", content.toString());
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        fileItem.delete();
                    } else {
                        parameters.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
        try {
            RSAPrivateKey privateKey = EncodingDecoding.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIsXFT8gGPB/RSMaxfL93HpCJEfqm+9mga+1S0OEZPVarRWBraHsJQrA1rM3z+XxiSwqDPTUmogSHw1QAY4Vje1UyT+w2UyBioJbJHDpC8y2yksN7d3Mmf/zeoCwyrfCvc6jGCTXjcBdXpiVeEO+KlirBqw/uW7BGtuB1MA0i2XfAgMBAAECgYBbfR94mkBNUSnE4YN7RgiUUpVSyLsxSZfcX7/V9WwDB1X6Y4Y2kAH9hMK0t+2ELtAvwKktEftjrafHNe0P7JWhomzE37/qoiJ2bpw9+P5Obnkq9nYhZKRR7mEz5Sr8IH3lnRqXehtiyxyvT4edcZkWpIMRTvWsGAgoPe28rNt5YQJBAMCCdFIOvldIfxlCvW4dWzxqMdk5lbRcq5OVcS0UIcRSV8yLokQFXpySgmiLyfvXaGe0+almQMgZnSvgF5MnI10CQQC49nAUVzC6LbkjOQS2aRcQh1T/ZKJkXKVulH8M8sucQUmv5kAVebhYr2yv9XLvNF2F8JRJ15d0krymETInAjZrAkBoNTXqRXjbnq7OacZJGTMOHR4mzHkxTQjDtx2wnTk6IKjOXLfVwmJYtyZImYMZBJ3LpbeP734Z02O1IHUifwkxAkBU8FboAGJQHU837adMXVZKMNvHrN8mV6Vg8rClsZnvV8wPCx3CvvL5RxYSeBUf5FxOdfyjLG5RClG3sY3mfA2hAkEAmByH9dFOd4o6WfyTH8kGK5Kaagy55n3YsjcnEvib6rsciFlAaPRAqs+IK1IqQET+JfDM5edUolrG2PRaKPchbg==");
            parameters.put("organization", new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(parameters.get("organization")))), StandardCharsets.UTF_8));
            parameters.put("registration_number", new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(parameters.get("registration_number")))), StandardCharsets.UTF_8));
            parameters.put("juridical_person", new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(parameters.get("juridical_person")))), StandardCharsets.UTF_8));
            parameters.put("charge_person", new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(parameters.get("charge_person")))), StandardCharsets.UTF_8));
            parameters.put("charge_phone", new String(Objects.requireNonNull(EncodingDecoding.decrypt(privateKey, Base64.getDecoder().decode(parameters.get("charge_phone")))), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(SHADigest.getDigest(parameters.get("organization"))).equalsIgnoreCase(parameters.get("sign_organization"))
                || !Objects.requireNonNull(SHADigest.getDigest(parameters.get("registration_number"))).equalsIgnoreCase(parameters.get("sign_registration_number"))
                || !Objects.requireNonNull(SHADigest.getDigest(parameters.get("juridical_person"))).equalsIgnoreCase(parameters.get("sign_juridical_person"))
                || !Objects.requireNonNull(SHADigest.getDigest(parameters.get("charge_person"))).equalsIgnoreCase(parameters.get("sign_charge_person"))
                || !Objects.requireNonNull(SHADigest.getDigest(parameters.get("charge_phone"))).equalsIgnoreCase(parameters.get("sign_charge_phone"))) {
            request.setAttribute("isApplied", "报文可能被损毁！");
            request.getRequestDispatcher("apply.jsp").forward(request, response);
            return;
        }
        try {
            String serial_number = getSerialNumber(new String[] {new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), parameters.get("organization")});
            List<String> cerLines = new ArrayList<>();
            cerLines.add("Serial Number: " + serial_number);
            cerLines.add("Sign Algorithm: sha1RSA");
            cerLines.add("Encrypt Algorithm: sha256");
            cerLines.add("GothamCityTrust RSA CA 2019, " + "www.tofushen.com, " + "Gotham City Trust, " + "CN");
            String[] valid_time = getTTL(Integer.parseInt(parameters.get("valid_time")));
            cerLines.add("Valid Time From: " + valid_time[0]);
            cerLines.add("Valid Time To: " + valid_time[1]);
            String organization = parameters.get("organization");
            cerLines.add("User: " + organization);
            cerLines.add("Public Key: " + parameters.get("public_key"));
            cerLines.add("Sign: " + RSASignature.sign(parameters.get("public_key"), Key.loadKeyByFile(this.getServletContext().getRealPath("/download/sk.key")), "utf-8"));
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

    private String getSign(String message) {
        byte[] plaintext;
        plaintext = message.getBytes(StandardCharsets.UTF_8);
        String file_path = this.getServletContext().getRealPath("/download/sk.key");
        File file = new File(file_path);
        String line;
        StringBuilder key_file = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                key_file.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            byte[] buffer = Base64.getDecoder().decode(key_file.toString());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] ciphertext = cipher.doFinal(plaintext);
            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/