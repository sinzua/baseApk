package com.lidroid.xutils.util;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;

public class OtherUtils {
    private static final int STRING_BUFFER_LENGTH = 100;
    private static SSLSocketFactory sslSocketFactory;

    private OtherUtils() {
    }

    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if (context != null) {
            try {
                webUserAgent = context.getString(((Integer) Class.forName("com.android.internal.R$string").getDeclaredField("web_user_agent").get(null)).intValue());
            } catch (Throwable th) {
            }
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }
        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        String version = VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            buffer.append("1.0");
        }
        buffer.append("; ");
        String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            buffer.append("en");
        }
        if ("REL".equals(VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, new Object[]{buffer, "Mobile "});
    }

    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }
        return new StringBuilder(String.valueOf(cachePath)).append(File.separator).append(dirName).toString();
    }

    public static long getAvailableSpace(File dir) {
        try {
            StatFs stats = new StatFs(dir.getPath());
            return ((long) stats.getBlockSize()) * ((long) stats.getAvailableBlocks());
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
            return -1;
        }
    }

    public static boolean isSupportRange(HttpResponse response) {
        if (response == null) {
            return false;
        }
        Header header = response.getFirstHeader("Accept-Ranges");
        if (header != null) {
            return "bytes".equals(header.getValue());
        }
        header = response.getFirstHeader("Content-Range");
        if (header == null) {
            return false;
        }
        String value = header.getValue();
        if (value == null || !value.startsWith("bytes")) {
            return false;
        }
        return true;
    }

    public static String getFileNameFromHttpResponse(HttpResponse response) {
        if (response == null) {
            return null;
        }
        Header header = response.getFirstHeader(MIME.CONTENT_DISPOSITION);
        if (header == null) {
            return null;
        }
        for (HeaderElement element : header.getElements()) {
            NameValuePair fileNamePair = element.getParameterByName("filename");
            if (fileNamePair != null) {
                String result = fileNamePair.getValue();
                return CharsetUtils.toCharset(result, DownloadManager.UTF8_CHARSET, result.length());
            }
        }
        return null;
    }

    public static Charset getCharsetFromHttpRequest(HttpRequestBase request) {
        if (request == null) {
            return null;
        }
        String charsetName = null;
        Header header = request.getFirstHeader("Content-Type");
        if (header != null) {
            for (HeaderElement element : header.getElements()) {
                NameValuePair charsetPair = element.getParameterByName("charset");
                if (charsetPair != null) {
                    charsetName = charsetPair.getValue();
                    break;
                }
            }
        }
        boolean isSupportedCharset = false;
        if (!TextUtils.isEmpty(charsetName)) {
            try {
                isSupportedCharset = Charset.isSupported(charsetName);
            } catch (Throwable th) {
            }
        }
        if (isSupportedCharset) {
            return Charset.forName(charsetName);
        }
        return null;
    }

    public static long sizeOfString(String str, String charset) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int len = str.length();
        if (len < 100) {
            return (long) str.getBytes(charset).length;
        }
        long size = 0;
        for (int i = 0; i < len; i += 100) {
            int end = i + 100;
            if (end >= len) {
                end = len;
            }
            size += (long) getSubString(str, i, end).getBytes(charset).length;
        }
        return size;
    }

    public static String getSubString(String str, int start, int end) {
        return new String(str.substring(start, end));
    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void trustAllHttpsURLConnection() {
        if (sslSocketFactory == null) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        if (sslSocketFactory != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            HttpsURLConnection.setDefaultHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
    }
}
