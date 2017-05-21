package com.lidroid.xutils.http.client.entity;

import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.lidroid.xutils.util.IOUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

abstract class DecompressingEntity extends HttpEntityWrapper implements UploadEntity {
    private RequestCallBackHandler callBackHandler = null;
    private InputStream content;
    private long uncompressedLength;
    private long uploadedSize = 0;

    abstract InputStream decorate(InputStream inputStream) throws IOException;

    public DecompressingEntity(HttpEntity wrapped) {
        super(wrapped);
        this.uncompressedLength = wrapped.getContentLength();
    }

    private InputStream getDecompressingStream() throws IOException {
        Closeable in = null;
        try {
            in = this.wrappedEntity.getContent();
            return decorate(in);
        } catch (IOException ex) {
            IOUtils.closeQuietly(in);
            throw ex;
        }
    }

    public InputStream getContent() throws IOException {
        if (!this.wrappedEntity.isStreaming()) {
            return getDecompressingStream();
        }
        if (this.content == null) {
            this.content = getDecompressingStream();
        }
        return this.content;
    }

    public long getContentLength() {
        return -1;
    }

    public void writeTo(OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        Closeable inStream = null;
        try {
            inStream = getContent();
            byte[] tmp = new byte[4096];
            while (true) {
                int len = inStream.read(tmp);
                if (len == -1) {
                    break;
                }
                outStream.write(tmp, 0, len);
                this.uploadedSize += (long) len;
                if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.uncompressedLength, this.uploadedSize, false)) {
                    throw new InterruptedIOException("cancel");
                }
            }
            outStream.flush();
            if (this.callBackHandler != null) {
                this.callBackHandler.updateProgress(this.uncompressedLength, this.uploadedSize, true);
            }
            IOUtils.closeQuietly(inStream);
        } catch (Throwable th) {
            IOUtils.closeQuietly(inStream);
        }
    }

    public void setCallBackHandler(RequestCallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }
}
