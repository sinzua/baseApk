package org.codehaus.jackson.io;

import android.support.v4.internal.view.SupportMenu;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8Writer extends Writer {
    static final int SURR1_FIRST = 55296;
    static final int SURR1_LAST = 56319;
    static final int SURR2_FIRST = 56320;
    static final int SURR2_LAST = 57343;
    protected final IOContext _context;
    OutputStream _out;
    byte[] _outBuffer;
    final int _outBufferEnd;
    int _outPtr;
    int _surrogate = 0;

    public UTF8Writer(IOContext ctxt, OutputStream out) {
        this._context = ctxt;
        this._out = out;
        this._outBuffer = ctxt.allocWriteEncodingBuffer();
        this._outBufferEnd = this._outBuffer.length - 4;
        this._outPtr = 0;
    }

    public Writer append(char c) throws IOException {
        write((int) c);
        return this;
    }

    public void close() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            OutputStream out = this._out;
            this._out = null;
            byte[] buf = this._outBuffer;
            if (buf != null) {
                this._outBuffer = null;
                this._context.releaseWriteEncodingBuffer(buf);
            }
            out.close();
            int code = this._surrogate;
            this._surrogate = 0;
            if (code > 0) {
                throwIllegal(code);
            }
        }
    }

    public void flush() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            this._out.flush();
        }
    }

    public void write(char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len >= 2) {
            int off2;
            if (this._surrogate > 0) {
                off2 = off + 1;
                len--;
                write(convertSurrogate(cbuf[off]));
                off = off2;
            }
            int outPtr = this._outPtr;
            byte[] outBuf = this._outBuffer;
            int outBufLast = this._outBufferEnd;
            len += off;
            off2 = off;
            while (off2 < len) {
                int outPtr2;
                if (outPtr >= outBufLast) {
                    this._out.write(outBuf, 0, outPtr);
                    outPtr = 0;
                }
                off = off2 + 1;
                int c = cbuf[off2];
                if (c < 128) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) c;
                    int maxInCount = len - off;
                    int maxOutCount = outBufLast - outPtr2;
                    if (maxInCount > maxOutCount) {
                        maxInCount = maxOutCount;
                    }
                    maxInCount += off;
                    off2 = off;
                    while (off2 < maxInCount) {
                        off = off2 + 1;
                        c = cbuf[off2];
                        if (c >= 128) {
                            off2 = off;
                        } else {
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (byte) c;
                            outPtr2 = outPtr;
                            off2 = off;
                        }
                    }
                    outPtr = outPtr2;
                } else {
                    outPtr2 = outPtr;
                    off2 = off;
                }
                if (c < 2048) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 6) | 192);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                    off = off2;
                } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 12) | 224);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 6) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c & 63) | 128);
                } else {
                    if (c > SURR1_LAST) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    this._surrogate = c;
                    if (off2 >= len) {
                        outPtr = outPtr2;
                        off = off2;
                        break;
                    }
                    off = off2 + 1;
                    c = convertSurrogate(cbuf[off2]);
                    if (c > 1114111) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 18) | 240);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 12) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) (((c >> 6) & 63) | 128);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                }
                off2 = off;
            }
            off = off2;
            this._outPtr = outPtr;
        } else if (len == 1) {
            write(cbuf[off]);
        }
    }

    public void write(int c) throws IOException {
        if (this._surrogate > 0) {
            c = convertSurrogate(c);
        } else if (c >= SURR1_FIRST && c <= SURR2_LAST) {
            if (c > SURR1_LAST) {
                throwIllegal(c);
            }
            this._surrogate = c;
            return;
        }
        if (this._outPtr >= this._outBufferEnd) {
            this._out.write(this._outBuffer, 0, this._outPtr);
            this._outPtr = 0;
        }
        if (c < 128) {
            byte[] bArr = this._outBuffer;
            int i = this._outPtr;
            this._outPtr = i + 1;
            bArr[i] = (byte) c;
            return;
        }
        int i2 = this._outPtr;
        int i3;
        if (c < 2048) {
            i3 = i2 + 1;
            this._outBuffer[i2] = (byte) ((c >> 6) | 192);
            i2 = i3 + 1;
            this._outBuffer[i3] = (byte) ((c & 63) | 128);
        } else if (c <= SupportMenu.USER_MASK) {
            i3 = i2 + 1;
            this._outBuffer[i2] = (byte) ((c >> 12) | 224);
            i2 = i3 + 1;
            this._outBuffer[i3] = (byte) (((c >> 6) & 63) | 128);
            i3 = i2 + 1;
            this._outBuffer[i2] = (byte) ((c & 63) | 128);
            i2 = i3;
        } else {
            if (c > 1114111) {
                throwIllegal(c);
            }
            i3 = i2 + 1;
            this._outBuffer[i2] = (byte) ((c >> 18) | 240);
            i2 = i3 + 1;
            this._outBuffer[i3] = (byte) (((c >> 12) & 63) | 128);
            i3 = i2 + 1;
            this._outBuffer[i2] = (byte) (((c >> 6) & 63) | 128);
            i2 = i3 + 1;
            this._outBuffer[i3] = (byte) ((c & 63) | 128);
        }
        this._outPtr = i2;
    }

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int off, int len) throws IOException {
        if (len >= 2) {
            int off2;
            if (this._surrogate > 0) {
                off2 = off + 1;
                len--;
                write(convertSurrogate(str.charAt(off)));
                off = off2;
            }
            int outPtr = this._outPtr;
            byte[] outBuf = this._outBuffer;
            int outBufLast = this._outBufferEnd;
            len += off;
            off2 = off;
            while (off2 < len) {
                int outPtr2;
                if (outPtr >= outBufLast) {
                    this._out.write(outBuf, 0, outPtr);
                    outPtr = 0;
                }
                off = off2 + 1;
                int c = str.charAt(off2);
                if (c < 128) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) c;
                    int maxInCount = len - off;
                    int maxOutCount = outBufLast - outPtr2;
                    if (maxInCount > maxOutCount) {
                        maxInCount = maxOutCount;
                    }
                    maxInCount += off;
                    off2 = off;
                    while (off2 < maxInCount) {
                        off = off2 + 1;
                        c = str.charAt(off2);
                        if (c >= 128) {
                            off2 = off;
                        } else {
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (byte) c;
                            outPtr2 = outPtr;
                            off2 = off;
                        }
                    }
                    outPtr = outPtr2;
                } else {
                    outPtr2 = outPtr;
                    off2 = off;
                }
                if (c < 2048) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 6) | 192);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                    off = off2;
                } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 12) | 224);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 6) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c & 63) | 128);
                } else {
                    if (c > SURR1_LAST) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    this._surrogate = c;
                    if (off2 >= len) {
                        outPtr = outPtr2;
                        off = off2;
                        break;
                    }
                    off = off2 + 1;
                    c = convertSurrogate(str.charAt(off2));
                    if (c > 1114111) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 18) | 240);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 12) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) (((c >> 6) & 63) | 128);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                }
                off2 = off;
            }
            off = off2;
            this._outPtr = outPtr;
        } else if (len == 1) {
            write(str.charAt(off));
        }
    }

    private int convertSurrogate(int secondPart) throws IOException {
        int firstPart = this._surrogate;
        this._surrogate = 0;
        if (secondPart >= SURR2_FIRST && secondPart <= SURR2_LAST) {
            return (65536 + ((firstPart - SURR1_FIRST) << 10)) + (secondPart - SURR2_FIRST);
        }
        throw new IOException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
    }

    private void throwIllegal(int code) throws IOException {
        if (code > 1114111) {
            throw new IOException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        } else if (code < SURR1_FIRST) {
            throw new IOException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
        } else if (code <= SURR1_LAST) {
            throw new IOException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        } else {
            throw new IOException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
    }
}
