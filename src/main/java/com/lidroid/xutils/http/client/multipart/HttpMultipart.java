package com.lidroid.xutils.http.client.multipart;

import com.lidroid.xutils.http.client.multipart.MultipartEntity.CallBackInfo;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;

class HttpMultipart {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$lidroid$xutils$http$client$multipart$HttpMultipartMode;
    private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
    private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
    private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
    private final String boundary;
    private final Charset charset;
    private final HttpMultipartMode mode;
    private final List<FormBodyPart> parts;
    private String subType;

    static /* synthetic */ int[] $SWITCH_TABLE$com$lidroid$xutils$http$client$multipart$HttpMultipartMode() {
        int[] iArr = $SWITCH_TABLE$com$lidroid$xutils$http$client$multipart$HttpMultipartMode;
        if (iArr == null) {
            iArr = new int[HttpMultipartMode.values().length];
            try {
                iArr[HttpMultipartMode.BROWSER_COMPATIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[HttpMultipartMode.STRICT.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$lidroid$xutils$http$client$multipart$HttpMultipartMode = iArr;
        }
        return iArr;
    }

    private static ByteArrayBuffer encode(Charset charset, String string) {
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
        out.flush();
    }

    private static void writeBytes(String s, Charset charset, OutputStream out) throws IOException {
        writeBytes(encode(charset, s), out);
    }

    private static void writeBytes(String s, OutputStream out) throws IOException {
        writeBytes(encode(MIME.DEFAULT_CHARSET, s), out);
    }

    private static void writeField(MinimalField field, OutputStream out) throws IOException {
        writeBytes(field.getName(), out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), out);
        writeBytes(CR_LF, out);
    }

    private static void writeField(MinimalField field, Charset charset, OutputStream out) throws IOException {
        writeBytes(field.getName(), charset, out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), charset, out);
        writeBytes(CR_LF, out);
    }

    public HttpMultipart(String subType, Charset charset, String boundary, HttpMultipartMode mode) {
        if (subType == null) {
            throw new IllegalArgumentException("Multipart subtype may not be null");
        } else if (boundary == null) {
            throw new IllegalArgumentException("Multipart boundary may not be null");
        } else {
            this.subType = subType;
            if (charset == null) {
                charset = MIME.DEFAULT_CHARSET;
            }
            this.charset = charset;
            this.boundary = boundary;
            this.parts = new ArrayList();
            this.mode = mode;
        }
    }

    public HttpMultipart(String subType, Charset charset, String boundary) {
        this(subType, charset, boundary, HttpMultipartMode.STRICT);
    }

    public HttpMultipart(String subType, String boundary) {
        this(subType, null, boundary);
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubType() {
        return this.subType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public HttpMultipartMode getMode() {
        return this.mode;
    }

    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    public void addBodyPart(FormBodyPart part) {
        if (part != null) {
            this.parts.add(part);
        }
    }

    public String getBoundary() {
        return this.boundary;
    }

    private void doWriteTo(HttpMultipartMode mode, OutputStream out, boolean writeContent) throws IOException {
        doWriteTo(mode, out, CallBackInfo.DEFAULT, writeContent);
    }

    private void doWriteTo(HttpMultipartMode mode, OutputStream out, CallBackInfo callBackInfo, boolean writeContent) throws IOException {
        callBackInfo.pos = 0;
        ByteArrayBuffer boundary = encode(this.charset, getBoundary());
        for (FormBodyPart part : this.parts) {
            if (callBackInfo.doCallBack(true)) {
                writeBytes(TWO_DASHES, out);
                callBackInfo.pos += (long) TWO_DASHES.length();
                writeBytes(boundary, out);
                callBackInfo.pos += (long) boundary.length();
                writeBytes(CR_LF, out);
                callBackInfo.pos += (long) CR_LF.length();
                MinimalFieldHeader header = part.getHeader();
                switch ($SWITCH_TABLE$com$lidroid$xutils$http$client$multipart$HttpMultipartMode()[mode.ordinal()]) {
                    case 1:
                        Iterator it = header.iterator();
                        while (it.hasNext()) {
                            MinimalField field = (MinimalField) it.next();
                            writeField(field, out);
                            callBackInfo.pos += (long) ((encode(MIME.DEFAULT_CHARSET, field.getName() + field.getBody()).length() + FIELD_SEP.length()) + CR_LF.length());
                        }
                        break;
                    case 2:
                        MinimalField cd = header.getField(MIME.CONTENT_DISPOSITION);
                        writeField(cd, this.charset, out);
                        callBackInfo.pos += (long) ((encode(this.charset, cd.getName() + cd.getBody()).length() + FIELD_SEP.length()) + CR_LF.length());
                        if (part.getBody().getFilename() != null) {
                            MinimalField ct = header.getField("Content-Type");
                            writeField(ct, this.charset, out);
                            callBackInfo.pos += (long) ((encode(this.charset, ct.getName() + ct.getBody()).length() + FIELD_SEP.length()) + CR_LF.length());
                            break;
                        }
                        break;
                }
                writeBytes(CR_LF, out);
                callBackInfo.pos += (long) CR_LF.length();
                if (writeContent) {
                    ContentBody body = part.getBody();
                    body.setCallBackInfo(callBackInfo);
                    body.writeTo(out);
                }
                writeBytes(CR_LF, out);
                callBackInfo.pos += (long) CR_LF.length();
            } else {
                throw new InterruptedIOException("cancel");
            }
        }
        writeBytes(TWO_DASHES, out);
        callBackInfo.pos += (long) TWO_DASHES.length();
        writeBytes(boundary, out);
        callBackInfo.pos += (long) boundary.length();
        writeBytes(TWO_DASHES, out);
        callBackInfo.pos += (long) TWO_DASHES.length();
        writeBytes(CR_LF, out);
        callBackInfo.pos += (long) CR_LF.length();
        callBackInfo.doCallBack(true);
    }

    public void writeTo(OutputStream out, CallBackInfo callBackInfo) throws IOException {
        doWriteTo(this.mode, out, callBackInfo, true);
    }

    public long getTotalLength() {
        long contentLen = 0;
        for (FormBodyPart part : this.parts) {
            long len = part.getBody().getContentLength();
            if (len < 0) {
                return -1;
            }
            contentLen += len;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doWriteTo(this.mode, out, false);
            return ((long) out.toByteArray().length) + contentLen;
        } catch (Throwable th) {
            return -1;
        }
    }
}
