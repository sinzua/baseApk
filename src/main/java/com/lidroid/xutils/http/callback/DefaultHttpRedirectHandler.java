package com.lidroid.xutils.http.callback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class DefaultHttpRedirectHandler implements HttpRedirectHandler {
    public HttpRequestBase getDirectRequest(HttpResponse response) {
        if (!response.containsHeader("Location")) {
            return null;
        }
        HttpGet request = new HttpGet(response.getFirstHeader("Location").getValue());
        if (!response.containsHeader("Set-Cookie")) {
            return request;
        }
        request.addHeader("Cookie", response.getFirstHeader("Set-Cookie").getValue());
        return request;
    }
}
