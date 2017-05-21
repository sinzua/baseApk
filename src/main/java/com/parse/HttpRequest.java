package com.parse;

import com.supersonicads.sdk.utils.Constants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

class HttpRequest {
    public static final String POST_CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String POST_CONTENT_TYPE_JSON = "application/json";
    private HttpConnectionProvider mConnectionProvider;

    public HttpRequest(HttpConnectionProvider connectionProvider) {
        this.mConnectionProvider = connectionProvider;
    }

    public void sendPost(URL url, String data, ACRAResponse response) throws IOException {
        sendPost(url, data, response, "application/x-www-form-urlencoded");
    }

    public void sendPost(URL url, String data, ACRAResponse response, String contentType) throws IOException {
        HttpURLConnection urlConnection = this.mConnectionProvider.getConnection(url);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", Constants.JAVASCRIPT_INTERFACE_NAME);
        urlConnection.setRequestProperty("Content-Type", contentType);
        urlConnection.setRequestProperty("Content-Encoding", "gzip");
        urlConnection.setDoOutput(true);
        try {
            GZIPOutputStream gzipStream = new GZIPOutputStream(urlConnection.getOutputStream());
            gzipStream.write(data.getBytes());
            gzipStream.close();
            response.setStatusCode(urlConnection.getResponseCode());
            urlConnection.getInputStream().close();
        } finally {
            urlConnection.disconnect();
        }
    }
}
