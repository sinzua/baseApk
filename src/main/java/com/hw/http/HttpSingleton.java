package com.hw.http;

import android.os.Handler;
import android.os.Looper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.util.concurrent.TimeUnit;

public class HttpSingleton {
    private static final OkHttpClient instance = OkHttpClientEx.getUnsafeOkHttpClient();
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    static {
        instance.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        instance.setReadTimeout(20000, TimeUnit.MILLISECONDS);
        instance.setWriteTimeout(60000, TimeUnit.MILLISECONDS);
    }

    public static Call newCall(RequestBuilder requestBuilder) {
        return instance.newCall(RequestBuilder.build(requestBuilder));
    }

    public static Call newCall(Request request) {
        return instance.newCall(request);
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }
}
