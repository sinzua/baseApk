package com.parse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class SSLConnectionProvider implements HttpConnectionProvider {
    SSLConnectionProvider() {
    }

    public HttpURLConnection getConnection(URL url) throws IOException {
        return initializeConnectionParameters((HttpURLConnection) url.openConnection());
    }

    public HttpURLConnection initializeConnectionParameters(HttpURLConnection conn) {
        conn.setConnectTimeout(ACRA.getConfig().socketTimeout());
        conn.setReadTimeout(ACRA.getConfig().socketTimeout());
        return conn;
    }
}
