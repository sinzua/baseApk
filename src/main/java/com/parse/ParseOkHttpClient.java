package com.parse;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import com.parse.ParseHttpResponse.Builder;
import com.parse.ParseRequest.Method;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okio.BufferedSink;

class ParseOkHttpClient extends ParseHttpClient<Request, Response> {
    private OkHttpClient okHttpClient = new OkHttpClient();

    private static class CountingOkHttpRequestBody extends RequestBody {
        private ParseHttpBody parseBody;

        public CountingOkHttpRequestBody(ParseHttpBody parseBody) {
            this.parseBody = parseBody;
        }

        public long contentLength() throws IOException {
            return (long) this.parseBody.getContentLength();
        }

        public MediaType contentType() {
            return this.parseBody.getContentType() == null ? null : MediaType.parse(this.parseBody.getContentType());
        }

        public void writeTo(BufferedSink bufferedSink) throws IOException {
            this.parseBody.writeTo(bufferedSink.outputStream());
        }
    }

    public ParseOkHttpClient(int socketOperationTimeout, SSLSessionCache sslSessionCache) {
        this.okHttpClient.setConnectTimeout((long) socketOperationTimeout, TimeUnit.MILLISECONDS);
        this.okHttpClient.setReadTimeout((long) socketOperationTimeout, TimeUnit.MILLISECONDS);
        this.okHttpClient.setFollowRedirects(false);
        this.okHttpClient.setSslSocketFactory(SSLCertificateSocketFactory.getDefault(socketOperationTimeout, sslSessionCache));
    }

    ParseHttpResponse executeInternal(ParseHttpRequest parseRequest) throws IOException {
        return getResponse(this.okHttpClient.newCall(getRequest(parseRequest)).execute());
    }

    ParseHttpResponse getResponse(Response okHttpResponse) throws IOException {
        int statusCode = okHttpResponse.code();
        InputStream content = okHttpResponse.body().byteStream();
        int totalSize = (int) okHttpResponse.body().contentLength();
        String reasonPhrase = okHttpResponse.message();
        Map<String, String> headers = new HashMap();
        for (String name : okHttpResponse.headers().names()) {
            headers.put(name, okHttpResponse.header(name));
        }
        String contentType = null;
        ResponseBody body = okHttpResponse.body();
        if (!(body == null || body.contentType() == null)) {
            contentType = body.contentType().toString();
        }
        return ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) new Builder().setStatusCode(statusCode)).setContent(content)).setTotalSize(totalSize)).setReasonPhase(reasonPhrase)).setHeaders(headers)).setContentType(contentType)).build();
    }

    Request getRequest(ParseHttpRequest parseRequest) throws IOException {
        Request.Builder okHttpRequestBuilder = new Request.Builder();
        Method method = parseRequest.getMethod();
        switch (method) {
            case GET:
                okHttpRequestBuilder.get();
                break;
            case DELETE:
                okHttpRequestBuilder.delete();
                break;
            case POST:
            case PUT:
                break;
            default:
                throw new IllegalStateException("Unsupported http method " + method.toString());
        }
        okHttpRequestBuilder.url(parseRequest.getUrl());
        Headers.Builder okHttpHeadersBuilder = new Headers.Builder();
        for (Entry<String, String> entry : parseRequest.getAllHeaders().entrySet()) {
            okHttpHeadersBuilder.add((String) entry.getKey(), (String) entry.getValue());
        }
        okHttpRequestBuilder.headers(okHttpHeadersBuilder.build());
        ParseHttpBody parseBody = parseRequest.getBody();
        CountingOkHttpRequestBody okHttpRequestBody = null;
        if (parseBody instanceof ParseByteArrayHttpBody) {
            okHttpRequestBody = new CountingOkHttpRequestBody(parseBody);
        }
        switch (method) {
            case POST:
                okHttpRequestBuilder.post(okHttpRequestBody);
                break;
            case PUT:
                okHttpRequestBuilder.put(okHttpRequestBody);
                break;
        }
        return okHttpRequestBuilder.build();
    }
}
