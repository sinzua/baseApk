package com.flurry.sdk;

import com.nativex.network.volley.Request;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ij extends kr {
    private static final String c = ij.class.getName();
    public ArrayList<ik> a;
    public in b;
    private final int d = ControllerParameters.SECOND;
    private final int e = Request.HTTP_CONNECTION_TIMEOUT_SLOW_NETWORK;
    private final long f;
    private final int g;
    private final int h;
    private final iq i;
    private final Map<String, String> j;
    private long k;
    private int l;
    private int m;
    private String n;
    private String o;
    private boolean p;

    public static class a implements lb<ij> {
        la<ik> a = new la(new com.flurry.sdk.ik.a());

        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, ij ijVar) throws IOException {
            if (outputStream != null && ijVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                if (ijVar.o != null) {
                    anonymousClass1.writeUTF(ijVar.o);
                } else {
                    anonymousClass1.writeUTF("");
                }
                if (ijVar.t() != null) {
                    anonymousClass1.writeUTF(ijVar.t());
                } else {
                    anonymousClass1.writeUTF("");
                }
                anonymousClass1.writeLong(ijVar.p());
                anonymousClass1.writeInt(ijVar.r());
                anonymousClass1.writeLong(ijVar.f);
                anonymousClass1.writeInt(ijVar.g);
                anonymousClass1.writeInt(ijVar.h);
                anonymousClass1.writeInt(ijVar.i.a());
                Map f = ijVar.j;
                if (f != null) {
                    anonymousClass1.writeInt(ijVar.j.size());
                    for (String str : ijVar.j.keySet()) {
                        anonymousClass1.writeUTF(str);
                        anonymousClass1.writeUTF((String) f.get(str));
                    }
                } else {
                    anonymousClass1.writeInt(0);
                }
                anonymousClass1.writeLong(ijVar.k);
                anonymousClass1.writeInt(ijVar.l);
                anonymousClass1.writeInt(ijVar.m);
                if (ijVar.n != null) {
                    anonymousClass1.writeUTF(ijVar.n);
                } else {
                    anonymousClass1.writeUTF("");
                }
                anonymousClass1.writeBoolean(ijVar.p);
                anonymousClass1.flush();
                this.a.a(outputStream, ijVar.a);
            }
        }

        public ij a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            String readUTF = anonymousClass2.readUTF();
            if (readUTF.equals("")) {
                readUTF = null;
            }
            String readUTF2 = anonymousClass2.readUTF();
            long readLong = anonymousClass2.readLong();
            int readInt = anonymousClass2.readInt();
            long readLong2 = anonymousClass2.readLong();
            int readInt2 = anonymousClass2.readInt();
            int readInt3 = anonymousClass2.readInt();
            iq a = iq.a(anonymousClass2.readInt());
            Map map = null;
            int readInt4 = anonymousClass2.readInt();
            if (readInt4 != 0) {
                map = new HashMap();
                for (int i = 0; i < readInt4; i++) {
                    map.put(anonymousClass2.readUTF(), anonymousClass2.readUTF());
                }
            }
            long readLong3 = anonymousClass2.readLong();
            readInt4 = anonymousClass2.readInt();
            int readInt5 = anonymousClass2.readInt();
            String readUTF3 = anonymousClass2.readUTF();
            if (readUTF3.equals("")) {
                readUTF3 = null;
            }
            boolean readBoolean = anonymousClass2.readBoolean();
            ij ijVar = new ij(readUTF, readLong2, readUTF2, readLong, readInt2, readInt3, a, map, readInt4, readInt5, readUTF3);
            ijVar.k = readLong3;
            ijVar.p = readBoolean;
            ijVar.b(readInt);
            ijVar.a = (ArrayList) this.a.a(inputStream);
            ijVar.o();
            return ijVar;
        }
    }

    public ij(String str, long j, String str2, long j2, int i, int i2, iq iqVar, Map<String, String> map, int i3, int i4, String str3) {
        a(str2);
        a(j2);
        a_();
        this.o = str;
        this.f = j;
        c(i);
        this.g = i;
        this.h = i2;
        this.i = iqVar;
        this.j = map;
        this.l = i3;
        this.m = i4;
        this.n = str3;
        this.k = 30000;
        this.a = new ArrayList();
    }

    public void a_() {
        super.a_();
        if (super.r() != 1) {
            this.k *= 3;
        }
    }

    public synchronized void b() {
        this.b.g();
    }

    public boolean c() {
        return r() >= this.g;
    }

    public boolean a(int i) {
        return i > this.h;
    }

    public long e() {
        return this.f;
    }

    public iq f() {
        return this.i;
    }

    public long g() {
        return this.k;
    }

    public Map<String, String> h() {
        return this.j;
    }

    public String i() {
        return this.o;
    }

    public int j() {
        return this.l;
    }

    public int k() {
        return this.m;
    }

    public String l() {
        return this.n;
    }

    public String m() {
        return this.b.c();
    }

    public boolean n() {
        return this.p;
    }

    public void a(boolean z) {
        this.p = z;
    }

    public void a(in inVar) {
        this.b = inVar;
    }

    public void o() {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            ((ik) it.next()).l = this;
        }
    }

    public void a(ik ikVar) {
        this.a.add(ikVar);
    }
}
