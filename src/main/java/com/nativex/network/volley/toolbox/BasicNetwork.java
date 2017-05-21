package com.nativex.network.volley.toolbox;

import android.os.SystemClock;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.nativex.common.Log;
import com.nativex.network.volley.AuthFailureError;
import com.nativex.network.volley.Cache.Entry;
import com.nativex.network.volley.Network;
import com.nativex.network.volley.NetworkError;
import com.nativex.network.volley.NetworkResponse;
import com.nativex.network.volley.NoConnectionError;
import com.nativex.network.volley.Request;
import com.nativex.network.volley.RetryPolicy;
import com.nativex.network.volley.ServerError;
import com.nativex.network.volley.TimeoutError;
import com.nativex.network.volley.VolleyError;
import com.nativex.network.volley.VolleyLog;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    private static int DEFAULT_POOL_SIZE = 4096;
    private static int SLOW_REQUEST_THRESHOLD_MS = CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        this.mHttpStack = httpStack;
        this.mPool = pool;
    }

    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        int statusCode;
        long requestStart = SystemClock.elapsedRealtime();
        HttpResponse httpResponse = null;
        Map<String, String> responseHeaders = new HashMap();
        try {
            Map<String, String> headers = new HashMap();
            addCacheHeaders(headers, request.getCacheEntry());
            String requestName = request.getRequestName() != null ? request.getRequestName() : "";
            if (request.isLoggingEnabled()) {
                Log.d(requestName + " request URL -> " + request.getUrl());
                if (request.getBody() != null) {
                    Log.d(requestName + " request body -> " + new String(request.getBody(), DownloadManager.UTF8_CHARSET));
                } else {
                    Log.d(requestName + " request body -> null ");
                }
            }
            request.preExecuteJustBefore();
            httpResponse = this.mHttpStack.performRequest(request, headers);
            request.postExecuteImmediatelyAfter();
            StatusLine statusLine = httpResponse.getStatusLine();
            statusCode = statusLine.getStatusCode();
            responseHeaders = convertHeaders(httpResponse.getAllHeaders());
            if (statusCode == 304) {
                return new NetworkResponse(304, request.getCacheEntry().data, responseHeaders, true);
            }
            byte[] responseContents;
            if (httpResponse.getEntity() != null) {
                responseContents = entityToBytes(httpResponse.getEntity());
            } else {
                responseContents = new byte[0];
            }
            if (request.isLoggingEnabled()) {
                Log.d(requestName + " request response -> " + new String(responseContents));
            }
            logSlowRequests(SystemClock.elapsedRealtime() - requestStart, request, responseContents, statusLine);
            if (statusCode >= 200 && statusCode <= 299) {
                return new NetworkResponse(statusCode, responseContents, responseHeaders, false);
            }
            throw new IOException();
        } catch (SocketTimeoutException e) {
            throw new TimeoutError();
        } catch (ConnectTimeoutException e2) {
            throw new TimeoutError();
        } catch (MalformedURLException e3) {
            throw new RuntimeException("Bad URL " + request.getUrl(), e3);
        } catch (IOException e4) {
            if (httpResponse != null) {
                statusCode = httpResponse.getStatusLine().getStatusCode();
                VolleyLog.e("Unexpected response code %d for %s", Integer.valueOf(statusCode), request.getUrl());
                if (null != null) {
                    NetworkResponse networkResponse = new NetworkResponse(statusCode, null, responseHeaders, false);
                    if (statusCode == 401 || statusCode == 403) {
                        throw new AuthFailureError(networkResponse);
                    }
                    throw new ServerError(networkResponse);
                }
                throw new NetworkError(null);
            }
            throw new NoConnectionError(e4);
        }
    }

    private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, StatusLine statusLine) {
        if (DEBUG || requestLifetime > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(requestLifetime);
            objArr[2] = responseContents != null ? Integer.valueOf(responseContents.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d(str, objArr);
        }
    }

    private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
            throw e;
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
            }
        }
    }

    protected void logError(String what, String url, long start) {
        long now = SystemClock.elapsedRealtime();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(now - start), url);
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = this.mPool.getBuf(1024);
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                bytes.write(buffer, 0, count);
            }
            byte[] toByteArray = bytes.toByteArray();
            return toByteArray;
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(buffer);
            bytes.close();
        }
    }

    private static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new HashMap();
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }
}
