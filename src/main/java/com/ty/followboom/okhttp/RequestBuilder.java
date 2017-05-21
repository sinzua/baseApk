package com.ty.followboom.okhttp;

import android.text.TextUtils;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.HttpUrl.Builder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.ty.followboom.helpers.SignBodyHelper;
import com.ty.followboom.helpers.VLTools;
import java.util.Locale;
import java.util.TimeZone;

public abstract class RequestBuilder {
    public static final String DEFAULT_APPNAME = "instaviewandroid";
    public static final String DEFAULT_APP_VERSION = "2";
    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_DEVICE_MODEL = VLTools.getPhoneInfo();
    public static final String DEFAULT_SYSTEM_VERSION = ("instaviewandroid/  (" + VLTools.getPhoneInfo() + ")");
    public static final String DEFAULT_USER_AGENT = ("instaview 2 (" + VLTools.getPhoneInfo() + ")");
    public static final String HEADER_APPNAME = "appName";
    public static final String HEADER_APP_VERSION = "appVersion";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_DEVICE_MODEL = "deviceModel";
    public static final String HEADER_LANGUAGE = "language";
    public static final String HEADER_SYSTEM_VERSION = "systemVersion";
    public static final String HEADER_TIMEZONE = "timeZone";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public abstract String getPath();

    public void get(Builder urlBuilder) {
    }

    public String post() {
        return null;
    }

    public static Request build(RequestBuilder builder) {
        Builder urlBuilder = HttpUrl.parse(builder.getPath()).newBuilder();
        builder.get(urlBuilder);
        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());
        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("appVersion", "2");
        requestBuilder.addHeader("systemVersion", DEFAULT_SYSTEM_VERSION);
        requestBuilder.addHeader("User-Agent", DEFAULT_USER_AGENT);
        requestBuilder.addHeader(HEADER_APPNAME, DEFAULT_APPNAME);
        requestBuilder.addHeader("language", Locale.getDefault().getLanguage());
        requestBuilder.addHeader("deviceModel", DEFAULT_DEVICE_MODEL);
        requestBuilder.addHeader(HEADER_TIMEZONE, TimeZone.getDefault().getDisplayName(false, 0));
        String body = builder.post();
        if (!TextUtils.isEmpty(body)) {
            requestBuilder.post(RequestBody.create(JSON, body));
            requestBuilder.addHeader("Signature", SignBodyHelper.getSignature(body));
        }
        return requestBuilder.build();
    }
}
