package com.supersonic.mediationsdk.server;

import android.text.TextUtils;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import java.net.URI;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpFunctions {
    private static final int SERVER_REQUEST_TIMEOUT = 15000;

    public static String getStringFromURL(String link) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        request.setURI(new URI(link));
        request.getParams().setIntParameter("http.socket.timeout", SERVER_REQUEST_TIMEOUT);
        String retVal = EntityUtils.toString(client.execute(request).getEntity());
        if (TextUtils.isEmpty(retVal)) {
            return null;
        }
        return retVal;
    }

    public static boolean getStringFromPost(String url, String json) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(new StringEntity(json, DownloadManager.UTF8_CHARSET));
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            if (client.execute(httpost).getStatusLine().getStatusCode() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean getStringFromPostWithAutho(String url, String json, String userName, String password) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(new StringEntity(json, DownloadManager.UTF8_CHARSET));
            httpost.setHeader("Authorization", SupersonicUtils.getBase64Auth(userName, password));
            if (client.execute(httpost).getStatusLine().getStatusCode() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
