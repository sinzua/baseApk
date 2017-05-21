package com.lidroid.xutils.http.client.multipart.content;

import com.lidroid.xutils.http.client.multipart.MIME;
import com.lidroid.xutils.http.client.multipart.MultipartEntity.CallBackInfo;
import com.lidroid.xutils.util.IOUtils;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public class FileBody extends AbstractContentBody {
    private final String charset;
    private final File file;
    private final String filename;

    public FileBody(File file, String filename, String mimeType, String charset) {
        super(mimeType);
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
        if (filename != null) {
            this.filename = filename;
        } else {
            this.filename = file.getName();
        }
        this.charset = charset;
    }

    public FileBody(File file, String mimeType, String charset) {
        this(file, null, mimeType, charset);
    }

    public FileBody(File file, String mimeType) {
        this(file, null, mimeType, null);
    }

    public FileBody(File file) {
        this(file, null, "application/octet-stream", null);
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public void writeTo(OutputStream out) throws IOException {
        Throwable th;
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        Closeable in = null;
        try {
            Closeable in2 = new BufferedInputStream(new FileInputStream(this.file));
            try {
                byte[] tmp = new byte[4096];
                do {
                    int l = in2.read(tmp);
                    if (l == -1) {
                        out.flush();
                        IOUtils.closeQuietly(in2);
                        return;
                    }
                    out.write(tmp, 0, l);
                    CallBackInfo callBackInfo = this.callBackInfo;
                    callBackInfo.pos += (long) l;
                } while (this.callBackInfo.doCallBack(false));
                throw new InterruptedIOException("cancel");
            } catch (Throwable th2) {
                th = th2;
                in = in2;
            }
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly(in);
            throw th;
        }
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return this.charset;
    }

    public long getContentLength() {
        return this.file.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
