package com.lidroid.xutils.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.DefaultHttpRedirectHandler;
import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class SyncHttpHandler {
    private String charset;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private long expiry = HttpCache.getDefaultExpiryTime();
    private HttpRedirectHandler httpRedirectHandler;
    private String requestMethod;
    private String requestUrl;
    private int retriedTimes = 0;

    public void setHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        this.httpRedirectHandler = httpRedirectHandler;
    }

    public SyncHttpHandler(AbstractHttpClient client, HttpContext context, String charset) {
        this.client = client;
        this.context = context;
        this.charset = charset;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public ResponseStream sendRequest(HttpRequestBase request) throws HttpException {
        Throwable exception;
        int i;
        HttpRequestRetryHandler retryHandler = this.client.getHttpRequestRetryHandler();
        boolean retry;
        do {
            try {
                this.requestUrl = request.getURI().toString();
                this.requestMethod = request.getMethod();
                if (HttpUtils.sHttpCache.isEnabled(this.requestMethod)) {
                    String result = HttpUtils.sHttpCache.get(this.requestUrl);
                    if (result != null) {
                        return new ResponseStream(result);
                    }
                }
                return handleResponse(this.client.execute(request, this.context));
            } catch (Throwable e) {
                exception = e;
                i = this.retriedTimes + 1;
                this.retriedTimes = i;
                retry = retryHandler.retryRequest(exception, i, this.context);
                continue;
            } catch (Throwable e2) {
                exception = e2;
                i = this.retriedTimes + 1;
                this.retriedTimes = i;
                retry = retryHandler.retryRequest(exception, i, this.context);
                continue;
            } catch (NullPointerException e3) {
                exception = new IOException(e3.getMessage());
                exception.initCause(e3);
                i = this.retriedTimes + 1;
                this.retriedTimes = i;
                retry = retryHandler.retryRequest(exception, i, this.context);
                continue;
            } catch (HttpException e4) {
                throw e4;
            } catch (Throwable e22) {
                exception = new IOException(e22.getMessage());
                exception.initCause(e22);
                i = this.retriedTimes + 1;
                this.retriedTimes = i;
                retry = retryHandler.retryRequest(exception, i, this.context);
                continue;
            }
        } while (retry);
        throw new HttpException(exception);
    }

    private ResponseStream handleResponse(HttpResponse response) throws HttpException, IOException {
        if (response == null) {
            throw new HttpException("response is null");
        }
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode < 300) {
            ResponseStream responseStream = new ResponseStream(response, this.charset, this.requestUrl, this.expiry);
            responseStream.setRequestMethod(this.requestMethod);
            return responseStream;
        } else if (statusCode == 301 || statusCode == 302) {
            if (this.httpRedirectHandler == null) {
                this.httpRedirectHandler = new DefaultHttpRedirectHandler();
            }
            HttpRequestBase request = this.httpRedirectHandler.getDirectRequest(response);
            if (request != null) {
                return sendRequest(request);
            }
            return null;
        } else if (statusCode == 416) {
            throw new HttpException(statusCode, "maybe the file has downloaded completely");
        } else {
            throw new HttpException(statusCode, status.getReasonPhrase());
        }
    }
}
