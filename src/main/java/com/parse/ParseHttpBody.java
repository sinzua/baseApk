package com.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class ParseHttpBody {
    protected final int contentLength;
    protected final String contentType;

    public abstract InputStream getContent();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public ParseHttpBody(String contentType, int contentLength) {
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getContentType() {
        return this.contentType;
    }
}
