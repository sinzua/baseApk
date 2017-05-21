package org.codehaus.jackson.io;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.BufferRecycler.ByteBufferType;
import org.codehaus.jackson.util.BufferRecycler.CharBufferType;
import org.codehaus.jackson.util.TextBuffer;

public final class IOContext {
    protected final BufferRecycler _bufferRecycler;
    protected char[] _concatCBuffer = null;
    protected JsonEncoding _encoding;
    protected final boolean _managedResource;
    protected char[] _nameCopyBuffer = null;
    protected byte[] _readIOBuffer = null;
    protected final Object _sourceRef;
    protected char[] _tokenCBuffer = null;
    protected byte[] _writeEncodingBuffer = null;

    public IOContext(BufferRecycler br, Object sourceRef, boolean managedResource) {
        this._bufferRecycler = br;
        this._sourceRef = sourceRef;
        this._managedResource = managedResource;
    }

    public void setEncoding(JsonEncoding enc) {
        this._encoding = enc;
    }

    public final Object getSourceReference() {
        return this._sourceRef;
    }

    public final JsonEncoding getEncoding() {
        return this._encoding;
    }

    public final boolean isResourceManaged() {
        return this._managedResource;
    }

    public final TextBuffer constructTextBuffer() {
        return new TextBuffer(this._bufferRecycler);
    }

    public final byte[] allocReadIOBuffer() {
        if (this._readIOBuffer != null) {
            throw new IllegalStateException("Trying to call allocReadIOBuffer() second time");
        }
        this._readIOBuffer = this._bufferRecycler.allocByteBuffer(ByteBufferType.READ_IO_BUFFER);
        return this._readIOBuffer;
    }

    public final byte[] allocWriteEncodingBuffer() {
        if (this._writeEncodingBuffer != null) {
            throw new IllegalStateException("Trying to call allocWriteEncodingBuffer() second time");
        }
        this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(ByteBufferType.WRITE_ENCODING_BUFFER);
        return this._writeEncodingBuffer;
    }

    public final char[] allocTokenBuffer() {
        if (this._tokenCBuffer != null) {
            throw new IllegalStateException("Trying to call allocTokenBuffer() second time");
        }
        this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(CharBufferType.TOKEN_BUFFER);
        return this._tokenCBuffer;
    }

    public final char[] allocConcatBuffer() {
        if (this._concatCBuffer != null) {
            throw new IllegalStateException("Trying to call allocConcatBuffer() second time");
        }
        this._concatCBuffer = this._bufferRecycler.allocCharBuffer(CharBufferType.CONCAT_BUFFER);
        return this._concatCBuffer;
    }

    public final char[] allocNameCopyBuffer(int minSize) {
        if (this._nameCopyBuffer != null) {
            throw new IllegalStateException("Trying to call allocNameCopyBuffer() second time");
        }
        this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(CharBufferType.NAME_COPY_BUFFER, minSize);
        return this._nameCopyBuffer;
    }

    public final void releaseReadIOBuffer(byte[] buf) {
        if (buf == null) {
            return;
        }
        if (buf != this._readIOBuffer) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
        this._readIOBuffer = null;
        this._bufferRecycler.releaseByteBuffer(ByteBufferType.READ_IO_BUFFER, buf);
    }

    public final void releaseWriteEncodingBuffer(byte[] buf) {
        if (buf == null) {
            return;
        }
        if (buf != this._writeEncodingBuffer) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
        this._writeEncodingBuffer = null;
        this._bufferRecycler.releaseByteBuffer(ByteBufferType.WRITE_ENCODING_BUFFER, buf);
    }

    public final void releaseTokenBuffer(char[] buf) {
        if (buf == null) {
            return;
        }
        if (buf != this._tokenCBuffer) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
        this._tokenCBuffer = null;
        this._bufferRecycler.releaseCharBuffer(CharBufferType.TOKEN_BUFFER, buf);
    }

    public final void releaseConcatBuffer(char[] buf) {
        if (buf == null) {
            return;
        }
        if (buf != this._concatCBuffer) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
        this._concatCBuffer = null;
        this._bufferRecycler.releaseCharBuffer(CharBufferType.CONCAT_BUFFER, buf);
    }

    public final void releaseNameCopyBuffer(char[] buf) {
        if (buf == null) {
            return;
        }
        if (buf != this._nameCopyBuffer) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
        this._nameCopyBuffer = null;
        this._bufferRecycler.releaseCharBuffer(CharBufferType.NAME_COPY_BUFFER, buf);
    }
}
