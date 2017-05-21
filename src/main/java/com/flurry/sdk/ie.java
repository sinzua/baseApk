package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ie {
    private long a;
    private boolean b;
    private byte[] c;

    public static class a implements lb<ie> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, ie ieVar) throws IOException {
            if (outputStream != null && ieVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                anonymousClass1.writeLong(ieVar.a);
                anonymousClass1.writeBoolean(ieVar.b);
                anonymousClass1.writeInt(ieVar.c.length);
                anonymousClass1.write(ieVar.c);
                anonymousClass1.flush();
            }
        }

        public ie a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            ie ieVar = new ie();
            ieVar.a = anonymousClass2.readLong();
            ieVar.b = anonymousClass2.readBoolean();
            ieVar.c = new byte[anonymousClass2.readInt()];
            anonymousClass2.readFully(ieVar.c);
            return ieVar;
        }
    }

    public long a() {
        return this.a;
    }

    public boolean b() {
        return this.b;
    }

    public byte[] c() {
        return this.c;
    }

    public void a(long j) {
        this.a = j;
    }

    public void a(boolean z) {
        this.b = z;
    }

    public void a(byte[] bArr) {
        this.c = bArr;
    }
}
