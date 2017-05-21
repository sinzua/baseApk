package com.parse;

import com.parse.ParseRequest.Method;
import java.util.HashMap;
import java.util.Map;

class ParseHttpRequest {
    private final ParseHttpBody body;
    private final Map<String, String> headers;
    private final Method method;
    private final String url;

    public static class Builder {
        protected ParseHttpBody body;
        protected Map<String, String> headers;
        protected Method method;
        protected String url;

        public Builder() {
            this.headers = new HashMap();
        }

        public Builder(ParseHttpRequest request) {
            this.url = request.url;
            this.method = request.method;
            this.headers = new HashMap(request.headers);
            this.body = request.body;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder setBody(ParseHttpBody body) {
            this.body = body;
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ParseHttpRequest build() {
            return new ParseHttpRequest(this);
        }
    }

    protected ParseHttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getUrl() {
        return this.url;
    }

    public Method getMethod() {
        return this.method;
    }

    public Map<String, String> getAllHeaders() {
        return this.headers;
    }

    public String getHeader(String name) {
        return (String) this.headers.get(name);
    }

    public ParseHttpBody getBody() {
        return this.body;
    }
}
