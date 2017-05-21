package com.nativex.network.volley.toolbox;

import android.os.Handler;
import android.os.Looper;
import com.nativex.network.volley.Cache;
import com.nativex.network.volley.NetworkResponse;
import com.nativex.network.volley.Request;
import com.nativex.network.volley.Request.Priority;
import com.nativex.network.volley.Response;

public class ClearCacheRequest extends Request<Object> {
    private final Cache mCache;
    private final Runnable mCallback;

    public ClearCacheRequest(Cache cache, Runnable callback) {
        super(0, null, null);
        this.mCache = cache;
        this.mCallback = callback;
    }

    public boolean isCanceled() {
        this.mCache.clear();
        if (this.mCallback != null) {
            new Handler(Looper.getMainLooper()).postAtFrontOfQueue(this.mCallback);
        }
        return true;
    }

    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }

    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    protected void deliverResponse(Object response) {
    }
}
