package com.flurry.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ky<ObjectType> implements lb<ObjectType> {
    protected final lb<ObjectType> a;

    public ky(lb<ObjectType> lbVar) {
        this.a = lbVar;
    }

    public void a(OutputStream outputStream, ObjectType objectType) throws IOException {
        if (this.a != null && outputStream != null && objectType != null) {
            this.a.a(outputStream, objectType);
        }
    }

    public ObjectType b(InputStream inputStream) throws IOException {
        if (this.a == null || inputStream == null) {
            return null;
        }
        return this.a.b(inputStream);
    }
}
