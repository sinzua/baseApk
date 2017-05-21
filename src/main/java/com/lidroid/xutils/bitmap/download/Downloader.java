package com.lidroid.xutils.bitmap.download;

import android.content.Context;
import com.lidroid.xutils.BitmapUtils.BitmapLoadTask;
import java.io.OutputStream;

public abstract class Downloader {
    private Context context;
    private int defaultConnectTimeout;
    private long defaultExpiry;
    private int defaultReadTimeout;

    public abstract long downloadToStream(String str, OutputStream outputStream, BitmapLoadTask<?> bitmapLoadTask);

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDefaultExpiry(long expiry) {
        this.defaultExpiry = expiry;
    }

    public long getDefaultExpiry() {
        return this.defaultExpiry;
    }

    public int getDefaultConnectTimeout() {
        return this.defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public int getDefaultReadTimeout() {
        return this.defaultReadTimeout;
    }

    public void setDefaultReadTimeout(int defaultReadTimeout) {
        this.defaultReadTimeout = defaultReadTimeout;
    }
}
