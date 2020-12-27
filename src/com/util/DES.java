package com.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;

public class DES {

    public static final String DES_ALGORITHM = "DES";

    static { Security.addProvider(new BouncyCastleProvider()); }

    public static String decrypt(String content, String key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            keyFactory.generateSecret(keySpec);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert cipher != null;
            byte[] buf = cipher.doFinal(hexStr2Bytes(content));
            int num = 0;
            for (byte b : buf) {
                String name = b + "";
                if (name.length() == 1) {
                    num++;
                }
            }
            byte[] bytes = new byte[buf.length - num];
            for (int i = 0; i < buf.length; i++) {
                String name = buf[i] + "";
                if (name.length() != 1) {
                    bytes[i] = buf[i];
                }
            }
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hexStr2Bytes(String src) {
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化
        int m, n;
        int iLen = src.length() / 2; //计算长度
        byte[] ret = new byte[iLen]; //分配存储空间
        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0X" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }
}
