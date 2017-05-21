package com.lidroid.xutils.http.client.multipart;

import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.lidroid.xutils.http.client.entity.UploadEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

public class MultipartEntity implements HttpEntity, UploadEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final String boundary;
    private CallBackInfo callBackInfo;
    private final Charset charset;
    private Header contentType;
    private volatile boolean dirty;
    private long length;
    private final HttpMultipart multipart;
    private String multipartSubtype;

    public static class CallBackInfo {
        public static final CallBackInfo DEFAULT = new CallBackInfo();
        public RequestCallBackHandler callBackHandler = null;
        public long pos = 0;
        public long totalLength = 0;

        public boolean doCallBack(boolean forceUpdateUI) {
            if (this.callBackHandler != null) {
                return this.callBackHandler.updateProgress(this.totalLength, this.pos, forceUpdateUI);
            }
            return true;
        }
    }

    public void setCallBackHandler(RequestCallBackHandler callBackHandler) {
        this.callBackInfo.callBackHandler = callBackHandler;
    }

    public MultipartEntity(HttpMultipartMode mode, String boundary, Charset charset) {
        this.callBackInfo = new CallBackInfo();
        this.multipartSubtype = "form-data";
        if (boundary == null) {
            boundary = generateBoundary();
        }
        this.boundary = boundary;
        if (mode == null) {
            mode = HttpMultipartMode.STRICT;
        }
        if (charset == null) {
            charset = MIME.DEFAULT_CHARSET;
        }
        this.charset = charset;
        this.multipart = new HttpMultipart(this.multipartSubtype, this.charset, this.boundary, mode);
        this.contentType = new BasicHeader("Content-Type", generateContentType(this.boundary, this.charset));
        this.dirty = true;
    }

    public MultipartEntity(HttpMultipartMode mode) {
        this(mode, null, null);
    }

    public MultipartEntity() {
        this(HttpMultipartMode.STRICT, null, null);
    }

    public void setMultipartSubtype(String multipartSubtype) {
        this.multipartSubtype = multipartSubtype;
        this.multipart.setSubType(multipartSubtype);
        this.contentType = new BasicHeader("Content-Type", generateContentType(this.boundary, this.charset));
    }

    protected String generateContentType(String boundary, Charset charset) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/" + this.multipartSubtype + "; boundary=");
        buffer.append(boundary);
        return buffer.toString();
    }

    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30;
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    public void addPart(FormBodyPart bodyPart) {
        this.multipart.addBodyPart(bodyPart);
        this.dirty = true;
    }

    public void addPart(String name, ContentBody contentBody) {
        addPart(new FormBodyPart(name, contentBody));
    }

    public void addPart(String name, ContentBody contentBody, String contentDisposition) {
        addPart(new FormBodyPart(name, contentBody, contentDisposition));
    }

    public boolean isRepeatable() {
        for (FormBodyPart part : this.multipart.getBodyParts()) {
            if (part.getBody().getContentLength() < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isChunked() {
        return !isRepeatable();
    }

    public boolean isStreaming() {
        return !isRepeatable();
    }

    public long getContentLength() {
        if (this.dirty) {
            this.length = this.multipart.getTotalLength();
            this.dirty = false;
        }
        return this.length;
    }

    public Header getContentType() {
        return this.contentType;
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }

    public void writeTo(OutputStream outStream) throws IOException {
        this.callBackInfo.totalLength = getContentLength();
        this.multipart.writeTo(outStream, this.callBackInfo);
    }
}
