package com.forwardwin.base.widgets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encryption {
    public static String getMD5(String source) {
        char[] hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] hashCalc = md.digest();
            char[] result = new char[32];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte everyByte = hashCalc[i];
                System.out.println(everyByte);
                int i2 = k + 1;
                result[k] = hexChar[(everyByte >>> 4) & 15];
                k = i2 + 1;
                result[i2] = hexChar[everyByte & 15];
            }
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
