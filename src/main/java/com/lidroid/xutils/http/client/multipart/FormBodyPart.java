package com.lidroid.xutils.http.client.multipart;

import com.lidroid.xutils.http.client.multipart.content.ContentBody;

public class FormBodyPart {
    private final ContentBody body;
    private final MinimalFieldHeader header;
    private final String name;

    public FormBodyPart(String name, ContentBody body) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (body == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = name;
            this.body = body;
            this.header = new MinimalFieldHeader();
            generateContentDisposition(body);
            generateContentType(body);
            generateTransferEncoding(body);
        }
    }

    public FormBodyPart(String name, ContentBody body, String contentDisposition) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (body == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = name;
            this.body = body;
            this.header = new MinimalFieldHeader();
            if (contentDisposition != null) {
                addField(MIME.CONTENT_DISPOSITION, contentDisposition);
            } else {
                generateContentDisposition(body);
            }
            generateContentType(body);
            generateTransferEncoding(body);
        }
    }

    public String getName() {
        return this.name;
    }

    public ContentBody getBody() {
        return this.body;
    }

    public MinimalFieldHeader getHeader() {
        return this.header;
    }

    public void addField(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Field name may not be null");
        }
        this.header.addField(new MinimalField(name, value));
    }

    protected void generateContentDisposition(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("form-data; name=\"");
        buffer.append(getName());
        buffer.append("\"");
        if (body.getFilename() != null) {
            buffer.append("; filename=\"");
            buffer.append(body.getFilename());
            buffer.append("\"");
        }
        addField(MIME.CONTENT_DISPOSITION, buffer.toString());
    }

    protected void generateContentType(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(body.getMimeType());
        if (body.getCharset() != null) {
            buffer.append("; charset=");
            buffer.append(body.getCharset());
        }
        addField("Content-Type", buffer.toString());
    }

    protected void generateTransferEncoding(ContentBody body) {
        addField(MIME.CONTENT_TRANSFER_ENC, body.getTransferEncoding());
    }
}
