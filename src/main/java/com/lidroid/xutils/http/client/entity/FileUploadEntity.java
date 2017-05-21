package com.lidroid.xutils.http.client.entity;

import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.lidroid.xutils.util.IOUtils;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import org.apache.http.entity.FileEntity;

public class FileUploadEntity extends FileEntity implements UploadEntity {
    private RequestCallBackHandler callBackHandler = null;
    private long fileSize;
    private long uploadedSize = 0;

    public FileUploadEntity(File file, String contentType) {
        super(file, contentType);
        this.fileSize = file.length();
    }

    public void writeTo(OutputStream outStream) throws IOException {
        Throwable th;
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        Closeable inStream = null;
        try {
            Closeable inStream2 = new BufferedInputStream(new FileInputStream(this.file));
            try {
                byte[] tmp = new byte[4096];
                while (true) {
                    int len = inStream2.read(tmp);
                    if (len == -1) {
                        break;
                    }
                    outStream.write(tmp, 0, len);
                    this.uploadedSize += (long) len;
                    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.fileSize, this.uploadedSize, false)) {
                        throw new InterruptedIOException("cancel");
                    }
                }
                outStream.flush();
                if (this.callBackHandler != null) {
                    this.callBackHandler.updateProgress(this.fileSize, this.uploadedSize, true);
                }
                IOUtils.closeQuietly(inStream2);
            } catch (Throwable th2) {
                th = th2;
                inStream = inStream2;
            }
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly(inStream);
            throw th;
        }
    }

    public void setCallBackHandler(RequestCallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }
}
