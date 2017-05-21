package com.hw.http;

import android.text.TextUtils;
import com.hw.hwapi.HewService;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.HttpUrl.Builder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public abstract class RequestBuilder {
    public static final String DEFAULT_COOKIE = "csrftoken=eHBnbdaaqff9CobdkDd5LFi6XokCRPZE; mid=VcA-qQAAAAH3cCgrJoKW3HYC2T1n";
    public static final String DEFAULT_USER_AGENT = "Instagram 7.2.0 (iPhone6,2; iPhone OS 7_1; zh_CN; zh-Hans; scale=2.00; 640x1136) AppleWebKit/420+";
    public static final MediaType FORM = MediaType.parse("multipart/form-data;");
    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_USER_AGENT = "User-Agent";

    public abstract String getPath();

    public void get(Builder urlBuilder) {
    }

    public RequestBody post() {
        return null;
    }

    public static Request build(RequestBuilder builder) {
        Builder urlBuilder = HttpUrl.parse(builder.getPath()).newBuilder();
        builder.get(urlBuilder);
        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", "Instagram 7.2.0 (iPhone6,2; iPhone OS 7_1; zh_CN; zh-Hans; scale=2.00; 640x1136) AppleWebKit/420+");
        if (HewService.getInstance().getUserInfo() == null || TextUtils.isEmpty(HewService.getInstance().getUserInfo().getCookie())) {
            requestBuilder.addHeader("Cookie", DEFAULT_COOKIE);
        } else {
            requestBuilder.addHeader("Cookie", HewService.getInstance().getUserInfo().getCookie());
        }
        RequestBody body = builder.post();
        if (body != null) {
            requestBuilder.post(body);
        }
        return requestBuilder.build();
    }
}
