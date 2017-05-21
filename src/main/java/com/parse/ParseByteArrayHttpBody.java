package com.parse;

import com.supersonicads.sdk.precache.DownloadManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

class ParseByteArrayHttpBody extends ParseHttpBody {
    protected final byte[] content;
    protected final InputStream contentInputStream;

    public ParseByteArrayHttpBody(String content, String contentType) throws UnsupportedEncodingException {
        this(content.getBytes(DownloadManager.UTF8_CHARSET), contentType);
    }

    public ParseByteArrayHttpBody(byte[] content, String contentType) {
        super(contentType, content.length);
        this.content = content;
        this.contentInputStream = new ByteArrayInputStream(content);
    }

    public InputStream getContent() {
        return this.contentInputStream;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        out.write(this.content);
    }
}
