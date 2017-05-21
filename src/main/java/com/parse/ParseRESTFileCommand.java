package com.parse;

import com.parse.ParseRequest.Method;

class ParseRESTFileCommand extends ParseRESTCommand {
    private final String contentType;
    private final byte[] data;

    public static class Builder extends Init<Builder> {
        private String contentType = null;
        private byte[] data = null;

        public Builder() {
            method(Method.POST);
        }

        public Builder fileName(String fileName) {
            return (Builder) httpPath(String.format("files/%s", new Object[]{fileName}));
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        Builder self() {
            return this;
        }

        public ParseRESTFileCommand build() {
            return new ParseRESTFileCommand(this);
        }
    }

    public ParseRESTFileCommand(Builder builder) {
        super(builder);
        this.data = builder.data;
        this.contentType = builder.contentType;
    }

    protected ParseHttpBody newBody(ProgressCallback progressCallback) {
        if (progressCallback == null) {
            return new ParseByteArrayHttpBody(this.data, this.contentType);
        }
        return new ParseCountingByteArrayHttpBody(this.data, this.contentType, progressCallback);
    }
}
