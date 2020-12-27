package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Key {
    /**
     * 从文件中输入流中加载密钥
     *
     * @throws Exception 加载密钥时产生的异常
     */
    public static String loadKeyByFile(String file_path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_path));
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("密钥数据流读取错误");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new Exception("密钥输入流为空");
        }
    }
}
