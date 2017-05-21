package com.supersonicads.sdk.utils;

import android.os.AsyncTask;
import com.google.android.gms.common.api.CommonStatusCodes;
import java.net.HttpURLConnection;
import java.net.URL;

public class SupersonicAsyncHttpRequestTask extends AsyncTask<String, Integer, Integer> {
    protected Integer doInBackground(String... urls) {
        Exception e;
        Throwable th;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urls[0]);
            URL url2;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS);
                urlConnection.getInputStream();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    url2 = url;
                }
            } catch (Exception e2) {
                e = e2;
                url2 = url;
                try {
                    e.printStackTrace();
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    return Integer.valueOf(1);
                } catch (Throwable th2) {
                    th = th2;
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                url2 = url;
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return Integer.valueOf(1);
        }
        return Integer.valueOf(1);
    }
}
