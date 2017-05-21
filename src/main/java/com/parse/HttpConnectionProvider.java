package com.parse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

interface HttpConnectionProvider {
    HttpURLConnection getConnection(URL url) throws IOException;
}
