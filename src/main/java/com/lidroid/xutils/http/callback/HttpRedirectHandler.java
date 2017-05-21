package com.lidroid.xutils.http.callback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpRedirectHandler {
    HttpRequestBase getDirectRequest(HttpResponse httpResponse);
}
