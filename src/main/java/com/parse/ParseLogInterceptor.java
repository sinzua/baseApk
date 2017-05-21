package com.parse;

import android.util.Log;
import com.parse.ParseHttpResponse.Builder;
import com.parse.ParseNetworkInterceptor.Chain;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;
import org.json.JSONObject;

class ParseLogInterceptor implements ParseNetworkInterceptor {
    private static final String IGNORED_BODY_INFO = "Ignored";
    private static final String KEY_BODY = "Body";
    private static final String KEY_CONTENT_LENGTH = "Content-Length";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String KEY_HEADERS = "Headers";
    private static final String KEY_METHOD = "Method";
    private static final String KEY_REASON_PHASE = "Reason-Phase";
    private static final String KEY_REQUEST_ID = "Request-Id";
    private static final String KEY_STATUS_CODE = "Status-Code";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_URL = "Url";
    private static final String LOG_PARAGRAPH_BREAKER = "--------------";
    private static final String TAG = "ParseLogNetworkInterceptor";
    private static final String TYPE_REQUEST = "Request";
    private static final String TYPE_RESPONSE = "Response";
    private Logger logger;
    private final AtomicInteger nextRequestId = new AtomicInteger(0);

    private static abstract class Logger {
        public static String NEW_LINE = "\n";
        private ReentrantLock lock = new ReentrantLock();

        public abstract void write(String str);

        public void lock() {
            this.lock.lock();
        }

        public void unlock() {
            this.lock.unlock();
        }

        public void write(String name, String value) {
            write(name + " : " + value);
        }

        public void writeLine(String str) {
            write(str);
            write(NEW_LINE);
        }

        public void writeLine(String name, String value) {
            writeLine(name + " : " + value);
        }
    }

    private static class ProxyInputStream extends InputStream {
        private ByteArrayOutputStream bodyOutput = new ByteArrayOutputStream();
        private boolean hasBeenPrinted;
        private Logger logger;
        private boolean needsToBePrinted;
        private String requestId;
        private ParseHttpResponse response;

        public ProxyInputStream(String requestId, ParseHttpResponse response, Logger logger) throws FileNotFoundException {
            this.requestId = requestId;
            this.response = response;
            this.logger = logger;
            this.needsToBePrinted = ParseLogInterceptor.isContentTypePrintable(response.getContentType());
        }

        public int read() throws IOException {
            int n = this.response.getContent().read();
            if (n == -1) {
                if (!this.hasBeenPrinted) {
                    this.hasBeenPrinted = true;
                    if (this.needsToBePrinted) {
                        this.bodyOutput.write(n);
                        this.bodyOutput.close();
                        logResponseInfo(this.requestId, this.response, ParseLogInterceptor.formatBytes(this.bodyOutput.toByteArray(), this.response.getContentType()));
                    } else {
                        logResponseInfo(this.requestId, this.response, ParseLogInterceptor.IGNORED_BODY_INFO);
                    }
                }
            } else if (this.needsToBePrinted) {
                this.bodyOutput.write(n);
            }
            return n;
        }

        private void logResponseInfo(String requestId, ParseHttpResponse response, String responseBodyInfo) {
            this.logger.lock();
            this.logger.writeLine("Type", ParseLogInterceptor.TYPE_RESPONSE);
            this.logger.writeLine(ParseLogInterceptor.KEY_REQUEST_ID, requestId);
            this.logger.writeLine(ParseLogInterceptor.KEY_STATUS_CODE, String.valueOf(response.getStatusCode()));
            this.logger.writeLine(ParseLogInterceptor.KEY_REASON_PHASE, response.getReasonPhrase());
            this.logger.writeLine(ParseLogInterceptor.KEY_HEADERS, response.getAllHeaders().toString());
            if (responseBodyInfo != null) {
                this.logger.writeLine(ParseLogInterceptor.KEY_BODY, responseBodyInfo);
            }
            this.logger.writeLine(ParseLogInterceptor.LOG_PARAGRAPH_BREAKER);
            this.logger.unlock();
        }
    }

    private static class LogcatLogger extends Logger {
        private LogcatLogger() {
        }

        public void write(String str) {
            Log.i(ParseLogInterceptor.TAG, str);
        }

        public void writeLine(String str) {
            write(str);
        }
    }

    ParseLogInterceptor() {
    }

    private static String formatBytes(byte[] bytes, String contentType) {
        if (contentType.contains("json")) {
            try {
                return new JSONObject(new String(bytes)).toString(4);
            } catch (JSONException e) {
                return new String(bytes).trim();
            }
        } else if (contentType.contains("text")) {
            return new String(bytes).trim();
        } else {
            throw new IllegalStateException("We can not print this " + contentType);
        }
    }

    private static boolean isContentTypePrintable(String contentType) {
        return contentType.contains("json") || contentType.contains("text");
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = new LogcatLogger();
        }
        return this.logger;
    }

    public ParseHttpResponse intercept(Chain chain) throws IOException {
        String requestId = String.valueOf(this.nextRequestId.getAndIncrement());
        ParseHttpRequest request = chain.getRequest();
        logRequestInfo(requestId, request, getRequestBodyInfo(request));
        ParseHttpResponse response = chain.proceed(request);
        return ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) ((Builder) new Builder().setContent(new ProxyInputStream(requestId, response, getLogger()))).setContentType(response.getContentType())).setHeaders(response.getAllHeaders())).setReasonPhase(response.getReasonPhrase())).setStatusCode(response.getStatusCode())).setTotalSize(response.getTotalSize())).build();
    }

    private void logRequestInfo(String requestId, ParseHttpRequest request, String requestBodyInfo) throws IOException {
        Logger logger = getLogger();
        logger.lock();
        logger.writeLine("Type", TYPE_REQUEST);
        logger.writeLine(KEY_REQUEST_ID, requestId);
        logger.writeLine("Url", request.getUrl());
        logger.writeLine(KEY_METHOD, request.getMethod().toString());
        Map<String, String> headers = new HashMap(request.getAllHeaders());
        if (request.getBody() != null) {
            headers.put(KEY_CONTENT_LENGTH, String.valueOf(request.getBody().getContentLength()));
            headers.put("Content-Type", request.getBody().getContentType());
        }
        logger.writeLine(KEY_HEADERS, headers.toString());
        if (requestBodyInfo != null) {
            logger.writeLine(KEY_BODY, requestBodyInfo);
        }
        logger.writeLine(LOG_PARAGRAPH_BREAKER);
        logger.unlock();
    }

    private String getRequestBodyInfo(ParseHttpRequest request) throws IOException {
        if (request.getBody() == null) {
            return null;
        }
        String requestContentType = request.getBody().getContentType();
        if (!isContentTypePrintable(requestContentType)) {
            return IGNORED_BODY_INFO;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        request.getBody().writeTo(output);
        return formatBytes(output.toByteArray(), requestContentType);
    }
}
