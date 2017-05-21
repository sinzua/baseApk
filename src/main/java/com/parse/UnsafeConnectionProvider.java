package com.parse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

class UnsafeConnectionProvider implements HttpConnectionProvider {
    UnsafeConnectionProvider() {
    }

    public HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{new TrustEveryoneTrustManager()}, null);
                HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
                httpsConnection.setSSLSocketFactory(ctx.getSocketFactory());
                httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
            } catch (NoSuchAlgorithmException e) {
            } catch (KeyManagementException e2) {
            }
        }
        return initializeConnectionParameters(conn);
    }

    public HttpURLConnection initializeConnectionParameters(HttpURLConnection conn) {
        conn.setConnectTimeout(ACRA.getConfig().socketTimeout());
        conn.setReadTimeout(ACRA.getConfig().socketTimeout());
        return conn;
    }
}
