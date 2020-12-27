package com.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHADigest {
    public static String getDigest(String message) {
        try {
            byte[] plaintext = message.getBytes(StandardCharsets.UTF_8);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plaintext);
            byte[] ciphertext = messageDigest.digest();
            return HexString.getInstance().bytesToHexString(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
