package com.lidroid.xutils.http;

import android.text.TextUtils;
import com.lidroid.xutils.cache.LruMemoryCache;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import java.util.concurrent.ConcurrentHashMap;

public class HttpCache {
    private static final int DEFAULT_CACHE_SIZE = 102400;
    private static final long DEFAULT_EXPIRY_TIME = 60000;
    private static long defaultExpiryTime = DEFAULT_EXPIRY_TIME;
    private static final ConcurrentHashMap<String, Boolean> httpMethod_enabled_map = new ConcurrentHashMap(10);
    private int cacheSize;
    private final LruMemoryCache<String, String> mMemoryCache;

    static {
        httpMethod_enabled_map.put(HttpMethod.GET.toString(), Boolean.valueOf(true));
    }

    public HttpCache() {
        this(DEFAULT_CACHE_SIZE, DEFAULT_EXPIRY_TIME);
    }

    public HttpCache(int strLength, long defaultExpiryTime) {
        this.cacheSize = DEFAULT_CACHE_SIZE;
        this.cacheSize = strLength;
        defaultExpiryTime = defaultExpiryTime;
        this.mMemoryCache = new LruMemoryCache<String, String>(this.cacheSize) {
            protected int sizeOf(String key, String value) {
                if (value == null) {
                    return 0;
                }
                return value.length();
            }
        };
    }

    public void setCacheSize(int strLength) {
        this.mMemoryCache.setMaxSize(strLength);
    }

    public static void setDefaultExpiryTime(long defaultExpiryTime) {
        defaultExpiryTime = defaultExpiryTime;
    }

    public static long getDefaultExpiryTime() {
        return defaultExpiryTime;
    }

    public void put(String url, String result) {
        put(url, result, defaultExpiryTime);
    }

    public void put(String url, String result, long expiry) {
        if (url != null && result != null && expiry >= 1) {
            this.mMemoryCache.put(url, result, System.currentTimeMillis() + expiry);
        }
    }

    public String get(String url) {
        return url != null ? (String) this.mMemoryCache.get(url) : null;
    }

    public void clear() {
        this.mMemoryCache.evictAll();
    }

    public boolean isEnabled(HttpMethod method) {
        if (method == null) {
            return false;
        }
        Boolean enabled = (Boolean) httpMethod_enabled_map.get(method.toString());
        if (enabled != null) {
            return enabled.booleanValue();
        }
        return false;
    }

    public boolean isEnabled(String method) {
        if (TextUtils.isEmpty(method)) {
            return false;
        }
        Boolean enabled = (Boolean) httpMethod_enabled_map.get(method.toUpperCase());
        if (enabled != null) {
            return enabled.booleanValue();
        }
        return false;
    }

    public void setEnabled(HttpMethod method, boolean enabled) {
        httpMethod_enabled_map.put(method.toString(), Boolean.valueOf(enabled));
    }
}
