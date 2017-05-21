package com.lidroid.xutils.http.client.multipart.content;

import com.lidroid.xutils.http.client.multipart.MIME;
import com.lidroid.xutils.http.client.multipart.MultipartEntity.CallBackInfo;
import com.lidroid.xutils.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public class InputStreamBody extends AbstractContentBody {
    private final String filename;
    private final InputStream in;
    private long length;

    public InputStreamBody(InputStream in, long length, String filename, String mimeType) {
        super(mimeType);
        if (in == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        }
        this.in = in;
        this.filename = filename;
        this.length = length;
    }

    public InputStreamBody(InputStream in, long length, String filename) {
        this(in, length, filename, "application/octet-stream");
    }

    public InputStreamBody(InputStream in, long length) {
        this(in, length, "no_name", "application/octet-stream");
    }

    public InputStream getInputStream() {
        return this.in;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        try {
            byte[] tmp = new byte[4096];
            while (true) {
                int l = this.in.read(tmp);
                if (l == -1) {
                    break;
                }
                out.write(tmp, 0, l);
                CallBackInfo callBackInfo = this.callBackInfo;
                callBackInfo.pos += (long) l;
                if (!this.callBackInfo.doCallBack(false)) {
                    throw new InterruptedIOException("cancel");
                }
            }
            out.flush();
        } finally {
            IOUtils.closeQuietly(this.in);
        }
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return this.length;
    }

    public String getFilename() {
        return this.filename;
    }
}
