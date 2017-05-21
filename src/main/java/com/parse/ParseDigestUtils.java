package com.parse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class ParseDigestUtils {
    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    private ParseDigestUtils() {
    }

    public static String md5(String string) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(string.getBytes());
            return toHex(digester.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }
}
