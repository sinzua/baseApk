package com.ty.followboom.helpers;

import com.supersonicads.sdk.precache.DownloadManager;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignBodyHelper {
    private static final String KEY = "a3c8fb21c7340792a02d7d6967d2c04c4c66a7fecc1b157bca1faad882c7bc6a";

    public static String getSignature(String content) {
        try {
            return encode(content);
        } catch (Exception e) {
            return "";
        }
    }

    public static String encode(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        sha256_HMAC.init(new SecretKeySpec(KEY.getBytes(DownloadManager.UTF8_CHARSET), "HmacSHA256"));
        return byte2HexStr(sha256_HMAC.doFinal(data.getBytes(DownloadManager.UTF8_CHARSET)));
    }

    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (byte b2 : b) {
            String str;
            stmp = Integer.toHexString(b2 & 255);
            if (stmp.length() == 1) {
                str = "0" + stmp;
            } else {
                str = stmp;
            }
            sb.append(str);
        }
        return sb.toString().toLowerCase().trim();
    }
}
