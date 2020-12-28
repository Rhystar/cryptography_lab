package com.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordEncryptor {
    private final Object salt;
    private final String algorithm;
    private static final HexString hexString = HexString.getInstance();

    public PasswordEncryptor(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }

    public String encode(String rawPwd) {
        String result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            result = hexString.bytesToHexString(messageDigest.digest(mergePasswordAndSalt(rawPwd).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isPasswordValid(String encPwd, String rawPwd) {
        String password = this.encode(rawPwd);
        return encPwd.equals(password);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }
        if ((this.salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
}
