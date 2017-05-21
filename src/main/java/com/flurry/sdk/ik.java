package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ik {
    private static final String m = ik.class.getName();
    public int a;
    public long b;
    public long c;
    public boolean d;
    public int e;
    public il f;
    public String g;
    public int h;
    public long i;
    public boolean j;
    public long k = 0;
    public ij l;

    public static class a implements lb<ik> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, ik ikVar) throws IOException {
            if (outputStream != null && ikVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                anonymousClass1.writeInt(ikVar.a);
                anonymousClass1.writeLong(ikVar.b);
                anonymousClass1.writeLong(ikVar.c);
                anonymousClass1.writeBoolean(ikVar.d);
                anonymousClass1.writeInt(ikVar.e);
                anonymousClass1.writeInt(ikVar.f.a());
                if (ikVar.g != null) {
                    anonymousClass1.writeUTF(ikVar.g);
                } else {
                    anonymousClass1.writeUTF("");
                }
                anonymousClass1.writeInt(ikVar.h);
                anonymousClass1.writeLong(ikVar.i);
                anonymousClass1.writeBoolean(ikVar.j);
                anonymousClass1.writeLong(ikVar.k);
                anonymousClass1.flush();
            }
        }

        public ik a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            int readInt = anonymousClass2.readInt();
            long readLong = anonymousClass2.readLong();
            long readLong2 = anonymousClass2.readLong();
            boolean readBoolean = anonymousClass2.readBoolean();
            int readInt2 = anonymousClass2.readInt();
            il a = il.a(anonymousClass2.readInt());
            String readUTF = anonymousClass2.readUTF();
            int readInt3 = anonymousClass2.readInt();
            long readLong3 = anonymousClass2.readLong();
            boolean readBoolean2 = anonymousClass2.readBoolean();
            long readLong4 = anonymousClass2.readLong();
            ik ikVar = new ik(null, readLong, readLong2, readInt);
            ikVar.d = readBoolean;
            ikVar.e = readInt2;
            ikVar.f = a;
            ikVar.g = readUTF;
            ikVar.h = readInt3;
            ikVar.i = readLong3;
            ikVar.j = readBoolean2;
            ikVar.k = readLong4;
            return ikVar;
        }
    }

    public ik(ij ijVar, long j, long j2, int i) {
        this.l = ijVar;
        this.b = j;
        this.c = j2;
        this.a = i;
        this.e = 0;
        this.f = il.PENDING_COMPLETION;
    }

    public void a() {
        this.l.a(this);
        if (this.d) {
            this.l.a(true);
        }
    }

    public boolean b() {
        return this.l.c();
    }

    public boolean c() {
        return this.l.a(this.h);
    }

    public void a(int i) {
        if (i >= 0) {
            this.k += (long) i;
        } else if (this.k <= 0) {
            this.k = 0;
        }
    }

    public String d() {
        return this.l.m();
    }

    public String e() {
        return this.l.i();
    }

    public String f() {
        return this.l.t();
    }

    public void a(String str) {
        this.l.c(str);
    }

    public void g() {
        this.l.b();
    }
}
