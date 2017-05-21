package com.flurry.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import com.lidroid.xutils.util.CharsetUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public final class lt {
    private static final String a = lt.class.getSimpleName();

    public static void a() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Must be called from the main thread!");
        }
    }

    public static void b() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new IllegalStateException("Must be called from a background thread!");
        }
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Uri parse = Uri.parse(str);
        if (parse == null || parse.getScheme() != null) {
            return str;
        }
        return "http://" + str;
    }

    public static String b(String str) {
        return a(str, 255);
    }

    public static String a(String str, int i) {
        if (str == null) {
            return "";
        }
        return str.length() > i ? str.substring(0, i) : str;
    }

    public static String c(String str) {
        try {
            return URLEncoder.encode(str, DownloadManager.UTF8_CHARSET);
        } catch (UnsupportedEncodingException e) {
            kg.a(5, a, "Cannot encode '" + str + "'");
            return "";
        }
    }

    public static String d(String str) {
        try {
            return URLDecoder.decode(str, DownloadManager.UTF8_CHARSET);
        } catch (UnsupportedEncodingException e) {
            kg.a(5, a, "Cannot decode '" + str + "'");
            return "";
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static byte[] e(String str) {
        byte[] bArr = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                bArr = str.getBytes(DownloadManager.UTF8_CHARSET);
            } catch (UnsupportedEncodingException e) {
                kg.a(5, a, "Unsupported UTF-8: " + e.getMessage());
            }
        }
        return bArr;
    }

    public static byte[] f(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(str.getBytes(), 0, str.length());
            return instance.digest();
        } catch (NoSuchAlgorithmException e) {
            kg.a(6, a, "Unsupported SHA1: " + e.getMessage());
            return null;
        }
    }

    public static String a(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : bArr) {
            byte b2 = (byte) (b & 15);
            stringBuilder.append(cArr[(byte) ((b & 240) >> 4)]);
            stringBuilder.append(cArr[b2]);
        }
        return stringBuilder.toString();
    }

    public static String b(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, CharsetUtils.DEFAULT_ENCODING_CHARSET);
        } catch (UnsupportedEncodingException e) {
            kg.a(5, a, "Unsupported ISO-8859-1:" + e.getMessage());
            return null;
        }
    }

    public static boolean a(long j) {
        if (j == 0 || System.currentTimeMillis() <= j) {
            return true;
        }
        return false;
    }

    public static boolean a(Intent intent) {
        return js.a().e().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static String g(String str) {
        return str.replace("\\b", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
    }

    public static Map<String, String> h(String str) {
        Map<String, String> hashMap = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            for (String split : str.split(RequestParameters.AMPERSAND)) {
                String[] split2 = split.split(RequestParameters.EQUAL);
                if (!split2[0].equals("event")) {
                    hashMap.put(d(split2[0]), d(split2[1]));
                }
            }
        }
        return hashMap;
    }

    public static long i(String str) {
        if (str == null) {
            return 0;
        }
        long j = 1125899906842597L;
        int i = 0;
        while (i < str.length()) {
            long charAt = ((long) str.charAt(i)) + (j * 31);
            i++;
            j = charAt;
        }
        return j;
    }

    public static long a(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read < 0) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static byte[] a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        a(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static double a(double d, int i) {
        return ((double) Math.round(Math.pow(10.0d, (double) i) * d)) / Math.pow(10.0d, (double) i);
    }

    public static boolean a(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            if (context.getPackageManager().checkPermission(str, context.getPackageName()) == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            kg.a(6, a, "Error occured when checking if app has permission.  Error: " + e.getMessage());
            return false;
        }
    }
}
