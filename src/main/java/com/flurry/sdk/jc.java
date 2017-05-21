package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class jc {
    private static final String a = jc.class.getSimpleName();
    private boolean b;
    private long c;
    private final List<iz> d = new ArrayList();

    public static class a implements lb<jc> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, jc jcVar) throws IOException {
            throw new UnsupportedOperationException("Serialization not supported");
        }

        public jc a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass1 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            jc jcVar = new jc();
            anonymousClass1.readUTF();
            anonymousClass1.readUTF();
            jcVar.b = anonymousClass1.readBoolean();
            jcVar.c = anonymousClass1.readLong();
            while (true) {
                int readUnsignedShort = anonymousClass1.readUnsignedShort();
                if (readUnsignedShort == 0) {
                    return jcVar;
                }
                byte[] bArr = new byte[readUnsignedShort];
                anonymousClass1.readFully(bArr);
                jcVar.d.add(0, new iz(bArr));
            }
        }
    }

    public boolean a() {
        return this.b;
    }

    public long b() {
        return this.c;
    }

    public List<iz> c() {
        return Collections.unmodifiableList(this.d);
    }
}
