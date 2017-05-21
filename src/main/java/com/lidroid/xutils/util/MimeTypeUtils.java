package com.lidroid.xutils.util;

import android.webkit.MimeTypeMap;

public class MimeTypeUtils {
    private MimeTypeUtils() {
    }

    public static String getMimeType(String fileName) {
        String result = "application/octet-stream";
        int extPos = fileName.lastIndexOf(".");
        if (extPos == -1) {
            return result;
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(extPos + 1));
    }
}
