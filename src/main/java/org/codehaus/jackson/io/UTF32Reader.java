package org.codehaus.jackson.io;

import android.support.v4.internal.view.SupportMenu;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;

public class UTF32Reader extends BaseReader {
    protected final boolean _bigEndian;
    protected int _byteCount = 0;
    protected int _charCount = 0;
    protected final boolean _managedBuffers;
    protected char _surrogate = '\u0000';

    public /* bridge */ /* synthetic */ void close() throws IOException {
        super.close();
    }

    public /* bridge */ /* synthetic */ int read() throws IOException {
        return super.read();
    }

    public UTF32Reader(IOContext ctxt, InputStream in, byte[] buf, int ptr, int len, boolean isBigEndian) {
        boolean z = false;
        super(ctxt, in, buf, ptr, len);
        this._bigEndian = isBigEndian;
        if (in != null) {
            z = true;
        }
        this._managedBuffers = z;
    }

    public int read(char[] cbuf, int start, int len) throws IOException {
        if (this._buffer == null) {
            return -1;
        }
        if (len < 1) {
            return len;
        }
        int outPtr;
        if (start < 0 || start + len > cbuf.length) {
            reportBounds(cbuf, start, len);
        }
        len += start;
        int outPtr2 = start;
        if (this._surrogate != '\u0000') {
            outPtr = outPtr2 + 1;
            cbuf[outPtr2] = this._surrogate;
            this._surrogate = '\u0000';
        } else {
            int left = this._length - this._ptr;
            if (left < 4 && !loadMore(left)) {
                return -1;
            }
            outPtr = outPtr2;
        }
        while (outPtr < len) {
            int ch;
            int ptr = this._ptr;
            if (this._bigEndian) {
                ch = (((this._buffer[ptr] << 24) | ((this._buffer[ptr + 1] & 255) << 16)) | ((this._buffer[ptr + 2] & 255) << 8)) | (this._buffer[ptr + 3] & 255);
            } else {
                ch = (((this._buffer[ptr] & 255) | ((this._buffer[ptr + 1] & 255) << 8)) | ((this._buffer[ptr + 2] & 255) << 16)) | (this._buffer[ptr + 3] << 24);
            }
            this._ptr += 4;
            if (ch > SupportMenu.USER_MASK) {
                if (ch > 1114111) {
                    reportInvalid(ch, outPtr - start, "(above " + Integer.toHexString(1114111) + ") ");
                }
                ch -= 65536;
                outPtr2 = outPtr + 1;
                cbuf[outPtr] = (char) (55296 + (ch >> 10));
                ch = 56320 | (ch & 1023);
                if (outPtr2 >= len) {
                    this._surrogate = (char) ch;
                    break;
                }
            }
            outPtr2 = outPtr;
            outPtr = outPtr2 + 1;
            cbuf[outPtr2] = (char) ch;
            if (this._ptr >= this._length) {
                outPtr2 = outPtr;
                break;
            }
        }
        outPtr2 = outPtr;
        len = outPtr2 - start;
        this._charCount += len;
        return len;
    }

    private void reportUnexpectedEOF(int gotBytes, int needed) throws IOException {
        throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + gotBytes + ", needed " + needed + ", at char #" + this._charCount + ", byte #" + (this._byteCount + gotBytes) + ")");
    }

    private void reportInvalid(int value, int offset, String msg) throws IOException {
        throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(value) + msg + " at char #" + (this._charCount + offset) + ", byte #" + ((this._byteCount + this._ptr) - 1) + ")");
    }

    private boolean loadMore(int available) throws IOException {
        int count;
        this._byteCount += this._length - available;
        if (available > 0) {
            if (this._ptr > 0) {
                for (int i = 0; i < available; i++) {
                    this._buffer[i] = this._buffer[this._ptr + i];
                }
                this._ptr = 0;
            }
            this._length = available;
        } else {
            this._ptr = 0;
            count = this._in == null ? -1 : this._in.read(this._buffer);
            if (count < 1) {
                this._length = 0;
                if (count >= 0) {
                    reportStrangeStream();
                } else if (!this._managedBuffers) {
                    return false;
                } else {
                    freeBuffers();
                    return false;
                }
            }
            this._length = count;
        }
        while (this._length < 4) {
            if (this._in == null) {
                count = -1;
            } else {
                count = this._in.read(this._buffer, this._length, this._buffer.length - this._length);
            }
            if (count < 1) {
                if (count < 0) {
                    if (this._managedBuffers) {
                        freeBuffers();
                    }
                    reportUnexpectedEOF(this._length, 4);
                }
                reportStrangeStream();
            }
            this._length += count;
        }
        return true;
    }
}
