package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ld<T> implements lb<T> {
    private final String a;
    private final int b;
    private final le<T> c;

    public ld(String str, int i, le<T> leVar) {
        this.a = str;
        this.b = i;
        this.c = leVar;
    }

    public void a(OutputStream outputStream, T t) throws IOException {
        if (outputStream != null && this.c != null) {
            OutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                final /* synthetic */ ld a;

                public void close() {
                }
            };
            anonymousClass1.writeUTF(this.a);
            anonymousClass1.writeInt(this.b);
            lb a = this.c.a(this.b);
            if (a == null) {
                throw new IOException("No serializer for version: " + this.b);
            }
            a.a(anonymousClass1, t);
            anonymousClass1.flush();
        }
    }

    public T b(InputStream inputStream) throws IOException {
        if (inputStream == null || this.c == null) {
            return null;
        }
        InputStream anonymousClass2 = new DataInputStream(this, inputStream) {
            final /* synthetic */ ld a;

            public void close() {
            }
        };
        String readUTF = anonymousClass2.readUTF();
        if (this.a.equals(readUTF)) {
            int readInt = anonymousClass2.readInt();
            lb a = this.c.a(readInt);
            if (a != null) {
                return a.b(anonymousClass2);
            }
            throw new IOException("No serializer for version: " + readInt);
        }
        throw new IOException("Signature: " + readUTF + " is invalid");
    }
}
