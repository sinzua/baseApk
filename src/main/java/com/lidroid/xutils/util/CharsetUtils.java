package com.lidroid.xutils.util;

import com.supersonicads.sdk.precache.DownloadManager;
import java.util.ArrayList;
import java.util.List;

public class CharsetUtils {
    public static final String DEFAULT_ENCODING_CHARSET = "ISO-8859-1";
    public static final List<String> SUPPORT_CHARSET = new ArrayList();

    private CharsetUtils() {
    }

    public static String toCharset(String str, String charset, int judgeCharsetLength) {
        try {
            return new String(str.getBytes(getEncoding(str, judgeCharsetLength)), charset);
        } catch (Throwable ex) {
            LogUtils.w(ex);
            return str;
        }
    }

    public static String getEncoding(String str, int judgeCharsetLength) {
        String encode = DEFAULT_ENCODING_CHARSET;
        for (String charset : SUPPORT_CHARSET) {
            if (isCharset(str, charset, judgeCharsetLength)) {
                return charset;
            }
        }
        return encode;
    }

    public static boolean isCharset(String str, String charset, int judgeCharsetLength) {
        boolean z = false;
        try {
            String temp;
            if (str.length() > judgeCharsetLength) {
                temp = str.substring(0, judgeCharsetLength);
            } else {
                temp = str;
            }
            z = temp.equals(new String(temp.getBytes(charset), charset));
        } catch (Throwable th) {
        }
        return z;
    }

    static {
        SUPPORT_CHARSET.add(DEFAULT_ENCODING_CHARSET);
        SUPPORT_CHARSET.add("GB2312");
        SUPPORT_CHARSET.add("GBK");
        SUPPORT_CHARSET.add("GB18030");
        SUPPORT_CHARSET.add("US-ASCII");
        SUPPORT_CHARSET.add("ASCII");
        SUPPORT_CHARSET.add("ISO-2022-KR");
        SUPPORT_CHARSET.add("ISO-8859-2");
        SUPPORT_CHARSET.add("ISO-2022-JP");
        SUPPORT_CHARSET.add("ISO-2022-JP-2");
        SUPPORT_CHARSET.add(DownloadManager.UTF8_CHARSET);
    }
}
