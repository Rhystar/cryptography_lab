package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Key {
    /**
     * 从文件中输入流中加载密钥
     */
    public static String loadKeyByFile(String file_path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_path));
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
