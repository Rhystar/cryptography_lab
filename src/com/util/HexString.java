package com.util;

public class HexString {
    public static HexString getInstance() {
        return new HexString();
    }

    public String bytesToHexString(byte[] bArray) {
        StringBuilder stringBuffer = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                stringBuffer.append(0);
            stringBuffer.append(sTemp.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public byte[] hexStringToByte(String hex) {
        int len;
        //判断字符串的长度是否是两位
        if (hex.length() >= 2) {
            //判断字符喜欢是否是偶数
            len = (hex.length() / 2);
            if (hex.length() % 2 == 1) {
                hex = "0" + hex;
                len = len + 1;
            }
        } else {
            hex = "0" + hex;
            len = 1;
        }
        byte[] result = new byte[len];
        char[] aChar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(aChar[pos]) << 4 | toByte(aChar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
}
