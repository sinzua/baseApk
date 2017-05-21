package com.parse;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import com.parse.ParseHttpResponse.Builder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

class ParseURLConnectionHttpClient extends ParseHttpClient<HttpURLConnection, HttpURLConnection> {
    private int socketOperationTimeout;

    public ParseURLConnectionHttpClient(int socketOperationTimeout, SSLSessionCache sslSessionCache) {
        this.socketOperationTimeout = socketOperationTimeout;
        HttpsURLConnection.setDefaultSSLSocketFactory(SSLCertificateSocketFactory.getDefault(socketOperationTimeout, sslSessionCache));
    }

    ParseHttpResponse executeInternal(ParseHttpRequest parseRequest) throws IOException {
        HttpURLConnection connection = getRequest(parseRequest);
        ParseHttpBody body = parseRequest.getBody();
        if (body != null) {
            OutputStream outputStream = connection.getOutputStream();
            body.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        }
        return getResponse(connection);
    }

    HttpURLConnection getRequest(ParseHttpRequest parseRequest) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(parseRequest.getUrl()).openConnection();
        connection.setRequestMethod(parseRequest.getMethod().toString());
        connection.setConnectTimeout(this.socketOperationTimeout);
        connection.setReadTimeout(this.socketOperationTimeout);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        for (Entry<String, String> entry : parseRequest.getAllHeaders().entrySet()) {
            connection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }
        ParseHttpBody body = parseRequest.getBody();
        if (body != null) {
            connection.setRequestProperty("Content-Length", String.valueOf(body.getContentLength()));
            connection.setRequestProperty("Content-Type", body.getContentType());
            connection.setFixedLengthStreamingMode(body.getContentLength());
            connection.setDoOutput(true);
        }
        return connection;
    }

    ParseHttpResponse getResponse(HttpURLConnection connection) throws IOException {
        InputStream content;
        int statusCode = connection.getResponseCode();
        if (statusCode < 400) {
            content = connection.getInputStream();
        } else {
            content = connection.getErrorStream();
        }
        int totalSize = connection.getContentLength();
        String reasonPhrase = connection.getResponseMessage();
        Map<String, String> headers = new HashMap();
        for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null && ((List) entry.getValue()).size() > 0) {
                Object obj;
                Object key = entry.getKey();
                if (entry.getValue() == null) {
                    obj = "";
                } else {
                    String str = (String) ((List) entry.getValue()).get(0);
                }
                headers.put(key, obj);
            }
        }
        return ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) new Builder().setStatusCode(statusCode)).setContent(content)).setTotalSize(totalSize)).setReasonPhase(reasonPhrase)).setHeaders(headers)).setContentType(connection.getContentType())).build();
    }
}
