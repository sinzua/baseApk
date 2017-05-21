package com.ty.followboom.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class JsonCallback<T> implements Callback {
    private final Call mCall;
    private final Class<T> mClazz;

    public abstract void onFailure(Exception exception);

    public abstract void onResponse(T t);

    public void onFailure(Request request, final IOException e) {
        HttpSingleton.getMainThreadHandler().post(new Runnable() {
            public void run() {
                if (!JsonCallback.this.isCanceled()) {
                    JsonCallback.this.onFailure(e);
                }
            }
        });
    }

    public JsonCallback(Call call, Class<T> clazz) {
        this.mCall = call;
        this.mClazz = clazz;
    }

    protected Class<T> getResponseClass() {
        return this.mClazz;
    }

    protected boolean isCanceled() {
        return this.mCall.isCanceled();
    }

    public void onResponse(Response response) throws IOException {
        try {
            String s = response.body().string();
            log2Disk(s);
            final T t = JsonHelper.gson.fromJson(s, getResponseClass());
            HttpSingleton.getMainThreadHandler().post(new Runnable() {
                public void run() {
                    if (!JsonCallback.this.isCanceled()) {
                        JsonCallback.this.onResponse(t);
                    }
                }
            });
        } catch (final Exception e) {
            HttpSingleton.getMainThreadHandler().post(new Runnable() {
                public void run() {
                    if (!JsonCallback.this.isCanceled()) {
                        JsonCallback.this.onFailure(e);
                    }
                }
            });
        }
    }

    private void log2Disk(String s) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("/sdcard/response.txt"), false));
            pw.write(s);
            pw.close();
        } catch (FileNotFoundException e) {
        }
    }
}
