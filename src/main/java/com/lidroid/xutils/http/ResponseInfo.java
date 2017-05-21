package com.lidroid.xutils.http;

import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

public final class ResponseInfo<T> {
    public final Header contentEncoding;
    public final long contentLength;
    public final Header contentType;
    public final Locale locale;
    public final ProtocolVersion protocolVersion;
    public final String reasonPhrase;
    private final HttpResponse response;
    public T result;
    public final boolean resultFormCache;
    public final int statusCode;

    public Header[] getAllHeaders() {
        if (this.response == null) {
            return null;
        }
        return this.response.getAllHeaders();
    }

    public Header[] getHeaders(String name) {
        if (this.response == null) {
            return null;
        }
        return this.response.getHeaders(name);
    }

    public Header getFirstHeader(String name) {
        if (this.response == null) {
            return null;
        }
        return this.response.getFirstHeader(name);
    }

    public Header getLastHeader(String name) {
        if (this.response == null) {
            return null;
        }
        return this.response.getLastHeader(name);
    }

    public ResponseInfo(HttpResponse response, T result, boolean resultFormCache) {
        this.response = response;
        this.result = result;
        this.resultFormCache = resultFormCache;
        if (response != null) {
            this.locale = response.getLocale();
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) {
                this.statusCode = statusLine.getStatusCode();
                this.protocolVersion = statusLine.getProtocolVersion();
                this.reasonPhrase = statusLine.getReasonPhrase();
            } else {
                this.statusCode = 0;
                this.protocolVersion = null;
                this.reasonPhrase = null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.contentLength = entity.getContentLength();
                this.contentType = entity.getContentType();
                this.contentEncoding = entity.getContentEncoding();
                return;
            }
            this.contentLength = 0;
            this.contentType = null;
            this.contentEncoding = null;
            return;
        }
        this.locale = null;
        this.statusCode = 0;
        this.protocolVersion = null;
        this.reasonPhrase = null;
        this.contentLength = 0;
        this.contentType = null;
        this.contentEncoding = null;
    }
}
