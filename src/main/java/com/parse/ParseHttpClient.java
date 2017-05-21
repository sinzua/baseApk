package com.parse;

import android.net.SSLSessionCache;
import android.os.Build.VERSION;
import com.parse.ParseNetworkInterceptor.Chain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class ParseHttpClient<LibraryRequest, LibraryResponse> {
    private static final String APACHE_HTTPCLIENT_NAME = "org.apache.http";
    private static final String KEEP_ALIVE_PROPERTY_NAME = "http.keepAlive";
    private static final String MAX_CONNECTIONS_PROPERTY_NAME = "http.maxConnections";
    private static final String OKHTTPCLIENT_PATH = "com.squareup.okhttp.OkHttpClient";
    private static final String OKHTTP_NAME = "com.squareup.okhttp";
    private static final String TAG = "com.parse.ParseHttpClient";
    private static final String URLCONNECTION_NAME = "net.java.URLConnection";
    private List<ParseNetworkInterceptor> externalInterceptors;
    private boolean hasExecuted;
    private List<ParseNetworkInterceptor> internalInterceptors;

    private class ParseNetworkInterceptorChain implements Chain {
        private final int externalIndex;
        private final int internalIndex;
        private final ParseHttpRequest request;

        ParseNetworkInterceptorChain(int internalIndex, int externalIndex, ParseHttpRequest request) {
            this.internalIndex = internalIndex;
            this.externalIndex = externalIndex;
            this.request = request;
        }

        public ParseHttpRequest getRequest() {
            return this.request;
        }

        public ParseHttpResponse proceed(ParseHttpRequest request) throws IOException {
            if (ParseHttpClient.this.internalInterceptors != null && this.internalIndex < ParseHttpClient.this.internalInterceptors.size()) {
                return ((ParseNetworkInterceptor) ParseHttpClient.this.internalInterceptors.get(this.internalIndex)).intercept(new ParseNetworkInterceptorChain(this.internalIndex + 1, this.externalIndex, request));
            } else if (ParseHttpClient.this.externalInterceptors == null || this.externalIndex >= ParseHttpClient.this.externalInterceptors.size()) {
                return ParseHttpClient.this.executeInternal(request);
            } else {
                return ((ParseNetworkInterceptor) ParseHttpClient.this.externalInterceptors.get(this.externalIndex)).intercept(new ParseNetworkInterceptorChain(this.internalIndex, this.externalIndex + 1, request));
            }
        }
    }

    abstract ParseHttpResponse executeInternal(ParseHttpRequest parseHttpRequest) throws IOException;

    abstract LibraryRequest getRequest(ParseHttpRequest parseHttpRequest) throws IOException;

    abstract ParseHttpResponse getResponse(LibraryResponse libraryResponse) throws IOException;

    ParseHttpClient() {
    }

    public static ParseHttpClient createClient(int socketOperationTimeout, SSLSessionCache sslSessionCache) {
        String httpClientLibraryName;
        ParseHttpClient httpClient;
        if (hasOkHttpOnClasspath()) {
            httpClientLibraryName = OKHTTP_NAME;
            httpClient = new ParseOkHttpClient(socketOperationTimeout, sslSessionCache);
        } else if (VERSION.SDK_INT >= 19) {
            httpClientLibraryName = URLCONNECTION_NAME;
            httpClient = new ParseURLConnectionHttpClient(socketOperationTimeout, sslSessionCache);
        } else {
            httpClientLibraryName = APACHE_HTTPCLIENT_NAME;
            httpClient = new ParseApacheHttpClient(socketOperationTimeout, sslSessionCache);
        }
        PLog.i(TAG, "Using " + httpClientLibraryName + " library for networking communication.");
        return httpClient;
    }

    public static void setMaxConnections(int maxConnections) {
        if (maxConnections <= 0) {
            throw new IllegalArgumentException("Max connections should be large than 0");
        }
        System.setProperty(MAX_CONNECTIONS_PROPERTY_NAME, String.valueOf(maxConnections));
    }

    public static void setKeepAlive(boolean isKeepAlive) {
        System.setProperty(KEEP_ALIVE_PROPERTY_NAME, String.valueOf(isKeepAlive));
    }

    private static boolean hasOkHttpOnClasspath() {
        try {
            Class.forName(OKHTTPCLIENT_PATH);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    void addInternalInterceptor(ParseNetworkInterceptor interceptor) {
        if (this.hasExecuted) {
            throw new IllegalStateException("`ParseHttpClient#addInternalInterceptor(ParseNetworkInterceptor)` can only be invoked before `ParseHttpClient` execute any request");
        }
        if (this.internalInterceptors == null) {
            this.internalInterceptors = new ArrayList();
        }
        this.internalInterceptors.add(interceptor);
    }

    void addExternalInterceptor(ParseNetworkInterceptor interceptor) {
        if (this.externalInterceptors == null) {
            this.externalInterceptors = new ArrayList();
        }
        this.externalInterceptors.add(interceptor);
    }

    public final ParseHttpResponse execute(ParseHttpRequest request) throws IOException {
        if (!this.hasExecuted) {
            this.hasExecuted = true;
        }
        return new ParseNetworkInterceptorChain(0, 0, request).proceed(request);
    }
}
