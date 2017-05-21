package com.lidroid.xutils.http.client.entity;

import android.support.v4.media.session.PlaybackStateCompat;
import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.lidroid.xutils.util.IOUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;

public class InputStreamUploadEntity extends AbstractHttpEntity implements UploadEntity {
    private static final int BUFFER_SIZE = 2048;
    private RequestCallBackHandler callBackHandler = null;
    private final InputStream content;
    private final long length;
    private long uploadedSize = 0;

    public InputStreamUploadEntity(InputStream inputStream, long length) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Source input stream may not be null");
        }
        this.content = inputStream;
        this.length = length;
    }

    public boolean isRepeatable() {
        return false;
    }

    public long getContentLength() {
        return this.length;
    }

    public InputStream getContent() throws IOException {
        return this.content;
    }

    public void writeTo(OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        Closeable inStream = this.content;
        try {
            byte[] buffer = new byte[2048];
            int l;
            if (this.length >= 0) {
                long remaining = this.length;
                while (remaining > 0) {
                    l = inStream.read(buffer, 0, (int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH, remaining));
                    if (l == -1) {
                        break;
                    }
                    outStream.write(buffer, 0, l);
                    remaining -= (long) l;
                    this.uploadedSize += (long) l;
                    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.length, this.uploadedSize, false)) {
                        throw new InterruptedIOException("cancel");
                    }
                }
            } else {
                while (true) {
                    l = inStream.read(buffer);
                    if (l == -1) {
                        break;
                    }
                    outStream.write(buffer, 0, l);
                    this.uploadedSize += (long) l;
                    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.uploadedSize + 1, this.uploadedSize, false)) {
                        throw new InterruptedIOException("cancel");
                    }
                }
            }
            outStream.flush();
            if (this.callBackHandler != null) {
                this.callBackHandler.updateProgress(this.length, this.uploadedSize, true);
            }
            IOUtils.closeQuietly(inStream);
        } catch (Throwable th) {
            IOUtils.closeQuietly(inStream);
        }
    }

    public boolean isStreaming() {
        return true;
    }

    public void consumeContent() throws IOException {
        this.content.close();
    }

    public void setCallBackHandler(RequestCallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }
}
