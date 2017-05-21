package com.lidroid.xutils.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.IOUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import org.apache.http.HttpResponse;

public class ResponseStream extends InputStream {
    private String _directResult;
    private HttpResponse baseResponse;
    private InputStream baseStream;
    private String charset;
    private long expiry;
    private String requestMethod;
    private String requestUrl;

    public ResponseStream(HttpResponse baseResponse, String requestUrl, long expiry) throws IOException {
        this(baseResponse, DownloadManager.UTF8_CHARSET, requestUrl, expiry);
    }

    public ResponseStream(HttpResponse baseResponse, String charset, String requestUrl, long expiry) throws IOException {
        if (baseResponse == null) {
            throw new IllegalArgumentException("baseResponse may not be null");
        }
        this.baseResponse = baseResponse;
        this.baseStream = baseResponse.getEntity().getContent();
        this.charset = charset;
        this.requestUrl = requestUrl;
        this.expiry = expiry;
    }

    public ResponseStream(String result) throws IOException {
        if (result == null) {
            throw new IllegalArgumentException("result may not be null");
        }
        this._directResult = result;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public InputStream getBaseStream() {
        return this.baseStream;
    }

    public HttpResponse getBaseResponse() {
        return this.baseResponse;
    }

    public int getStatusCode() {
        if (this._directResult != null) {
            return 200;
        }
        return this.baseResponse.getStatusLine().getStatusCode();
    }

    public Locale getLocale() {
        if (this._directResult != null) {
            return Locale.getDefault();
        }
        return this.baseResponse.getLocale();
    }

    public String getReasonPhrase() {
        if (this._directResult != null) {
            return "";
        }
        return this.baseResponse.getStatusLine().getReasonPhrase();
    }

    public String readString() throws IOException {
        if (this._directResult != null) {
            return this._directResult;
        }
        if (this.baseStream == null) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.baseStream, this.charset));
            StringBuilder sb = new StringBuilder();
            String str = "";
            while (true) {
                str = reader.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str);
            }
            this._directResult = sb.toString();
            if (this.requestUrl != null && HttpUtils.sHttpCache.isEnabled(this.requestMethod)) {
                HttpUtils.sHttpCache.put(this.requestUrl, this._directResult, this.expiry);
            }
            String str2 = this._directResult;
            return str2;
        } finally {
            IOUtils.closeQuietly(this.baseStream);
        }
    }

    public void readFile(String savePath) throws IOException {
        Throwable th;
        if (this._directResult == null && this.baseStream != null) {
            Closeable out = null;
            try {
                Closeable out2 = new BufferedOutputStream(new FileOutputStream(savePath));
                try {
                    BufferedInputStream ins = new BufferedInputStream(this.baseStream);
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int len = ins.read(buffer);
                        if (len == -1) {
                            out2.flush();
                            IOUtils.closeQuietly(out2);
                            IOUtils.closeQuietly(this.baseStream);
                            return;
                        }
                        out2.write(buffer, 0, len);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                }
            } catch (Throwable th3) {
                th = th3;
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(this.baseStream);
                throw th;
            }
        }
    }

    public int read() throws IOException {
        if (this.baseStream == null) {
            return -1;
        }
        return this.baseStream.read();
    }

    public int available() throws IOException {
        if (this.baseStream == null) {
            return 0;
        }
        return this.baseStream.available();
    }

    public void close() throws IOException {
        if (this.baseStream != null) {
            this.baseStream.close();
        }
    }

    public void mark(int readLimit) {
        if (this.baseStream != null) {
            this.baseStream.mark(readLimit);
        }
    }

    public boolean markSupported() {
        if (this.baseStream == null) {
            return false;
        }
        return this.baseStream.markSupported();
    }

    public int read(byte[] buffer) throws IOException {
        if (this.baseStream == null) {
            return -1;
        }
        return this.baseStream.read(buffer);
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (this.baseStream == null) {
            return -1;
        }
        return this.baseStream.read(buffer, offset, length);
    }

    public synchronized void reset() throws IOException {
        if (this.baseStream != null) {
            this.baseStream.reset();
        }
    }

    public long skip(long byteCount) throws IOException {
        if (this.baseStream == null) {
            return 0;
        }
        return this.baseStream.skip(byteCount);
    }

    public long getContentLength() {
        if (this.baseStream == null) {
            return 0;
        }
        return this.baseResponse.getEntity().getContentLength();
    }
}
