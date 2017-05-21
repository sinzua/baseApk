package com.parse;

import com.supersonicads.sdk.precache.DownloadManager;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.json.JSONObject;

class HttpUtils {
    HttpUtils() {
    }

    public static void doPost(Map<?, ?> parameters, URL url) throws IOException {
        doPost(parameters, url, "application/x-www-form-urlencoded");
    }

    public static void doPost(Map<?, ?> parameters, URL url, String contentType) throws IOException {
        String content;
        HttpConnectionProvider provider;
        if (contentType == "application/json") {
            content = encodeParametersJson(parameters);
        } else {
            content = encodeParametersFormUrlEncoded(parameters);
        }
        if (ACRA.getConfig().checkSSLCertsOnCrashReport()) {
            provider = new SSLConnectionProvider();
        } else {
            provider = new UnsafeConnectionProvider();
        }
        new HttpRequest(provider).sendPost(url, content, new ACRAResponse(), contentType);
    }

    public static String encodeParametersFormUrlEncoded(Map<?, ?> parameters) throws IOException {
        StringBuilder dataBfr = new StringBuilder();
        for (Object key : parameters.keySet()) {
            if (dataBfr.length() != 0) {
                dataBfr.append('&');
            }
            Object value = parameters.get(key);
            if (value == null) {
                value = "";
            }
            dataBfr.append(URLEncoder.encode(key.toString(), DownloadManager.UTF8_CHARSET)).append('=').append(URLEncoder.encode(value.toString(), DownloadManager.UTF8_CHARSET));
        }
        return dataBfr.toString();
    }

    public static String encodeParametersJson(Map<?, ?> parameters) throws IOException {
        return new JSONObject(parameters).toString();
    }
}
