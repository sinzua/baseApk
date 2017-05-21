package com.parse;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ParseHttpResponse {
    InputStream content;
    String contentType;
    Map<String, String> headers;
    String reasonPhrase;
    int statusCode;
    int totalSize;

    static abstract class Init<T extends Init<T>> {
        private InputStream content;
        private String contentType;
        private Map<String, String> headers;
        private String reasonPhrase;
        private int statusCode;
        private int totalSize;

        abstract T self();

        Init() {
        }

        public T setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return self();
        }

        public T setContent(InputStream content) {
            this.content = content;
            return self();
        }

        public T setTotalSize(int totalSize) {
            this.totalSize = totalSize;
            return self();
        }

        public T setReasonPhase(String reasonPhase) {
            this.reasonPhrase = reasonPhase;
            return self();
        }

        public T setHeaders(Map<String, String> headers) {
            this.headers = Collections.unmodifiableMap(new HashMap(headers));
            return self();
        }

        public T setContentType(String contentType) {
            this.contentType = contentType;
            return self();
        }
    }

    public static class Builder extends Init<Builder> {
        Builder self() {
            return this;
        }

        public ParseHttpResponse build() {
            return new ParseHttpResponse(this);
        }
    }

    ParseHttpResponse(Init<?> builder) {
        this.statusCode = builder.statusCode;
        this.content = builder.content;
        this.totalSize = builder.totalSize;
        this.reasonPhrase = builder.reasonPhrase;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public InputStream getContent() {
        return this.content;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getHeader(String name) {
        return this.headers == null ? null : (String) this.headers.get(name);
    }

    public Map<String, String> getAllHeaders() {
        return this.headers;
    }
}
