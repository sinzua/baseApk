package com.parse;

import java.io.IOException;
import java.io.OutputStream;

class ParseCountingByteArrayHttpBody extends ParseByteArrayHttpBody {
    private static final int DEFAULT_CHUNK_SIZE = 4096;
    private final ProgressCallback progressCallback;

    public ParseCountingByteArrayHttpBody(byte[] content, String contentType, ProgressCallback progressCallback) {
        super(content, contentType);
        this.progressCallback = progressCallback;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        int position = 0;
        int totalLength = this.content.length;
        while (position < totalLength) {
            int length = Math.min(totalLength - position, 4096);
            out.write(this.content, position, length);
            out.flush();
            if (this.progressCallback != null) {
                position += length;
                this.progressCallback.done(Integer.valueOf((position * 100) / totalLength));
            }
        }
    }
}
