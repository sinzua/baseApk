package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class in {
    private static final String a = io.class.getName();
    private long b;
    private long c = System.currentTimeMillis();
    private long d;
    private ir e;
    private boolean f;
    private int g;
    private String h;
    private int i;
    private AtomicInteger j;
    private Map<Long, ij> k;

    public static class a implements lb<in> {
        la<ij> a = new la(new com.flurry.sdk.ij.a());

        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, in inVar) throws IOException {
            if (outputStream != null && inVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                anonymousClass1.writeLong(inVar.b);
                anonymousClass1.writeLong(inVar.c);
                anonymousClass1.writeLong(inVar.d);
                anonymousClass1.writeInt(inVar.e.a());
                anonymousClass1.writeBoolean(inVar.f);
                anonymousClass1.writeInt(inVar.g);
                if (inVar.h != null) {
                    anonymousClass1.writeUTF(inVar.h);
                } else {
                    anonymousClass1.writeUTF("");
                }
                anonymousClass1.writeInt(inVar.i);
                anonymousClass1.writeInt(inVar.j.intValue());
                anonymousClass1.flush();
                this.a.a(outputStream, inVar.d());
            }
        }

        public in a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            long readLong = anonymousClass2.readLong();
            long readLong2 = anonymousClass2.readLong();
            long readLong3 = anonymousClass2.readLong();
            ir a = ir.a(anonymousClass2.readInt());
            boolean readBoolean = anonymousClass2.readBoolean();
            int readInt = anonymousClass2.readInt();
            String readUTF = anonymousClass2.readUTF();
            int readInt2 = anonymousClass2.readInt();
            int readInt3 = anonymousClass2.readInt();
            in inVar = new in(readUTF, readBoolean, readLong, readLong3, a, null);
            inVar.c = readLong2;
            inVar.g = readInt;
            inVar.i = readInt2;
            inVar.j = new AtomicInteger(readInt3);
            List<ij> a2 = this.a.a(inputStream);
            if (a2 != null) {
                inVar.k = new HashMap();
                for (ij ijVar : a2) {
                    ijVar.b = inVar;
                    inVar.k.put(Long.valueOf(ijVar.e()), ijVar);
                }
            }
            return inVar;
        }
    }

    public in(String str, boolean z, long j, long j2, ir irVar, Map<Long, ij> map) {
        this.h = str;
        this.f = z;
        this.b = j;
        this.d = j2;
        this.e = irVar;
        this.k = map;
        if (map != null) {
            for (Object obj : map.keySet()) {
                ((ij) map.get(obj)).a(this);
            }
            this.i = map.size();
        } else {
            this.i = 0;
        }
        this.j = new AtomicInteger(0);
    }

    public int a() {
        return this.g;
    }

    public long b() {
        return this.b;
    }

    public String c() {
        return this.h;
    }

    public List<ij> d() {
        if (this.k != null) {
            return new ArrayList(this.k.values());
        }
        return Collections.emptyList();
    }

    public Map<Long, ij> e() {
        return this.k;
    }

    public void a(int i) {
        this.g = i;
    }

    public synchronized boolean f() {
        return this.j.intValue() >= this.i;
    }

    public synchronized void g() {
        this.j.incrementAndGet();
    }

    public byte[] h() throws IOException {
        Throwable e;
        Closeable closeable = null;
        Closeable dataOutputStream;
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeShort(this.e.a());
                dataOutputStream.writeLong(this.b);
                dataOutputStream.writeLong(this.d);
                dataOutputStream.writeBoolean(this.f);
                if (this.f) {
                    dataOutputStream.writeShort(this.g);
                    dataOutputStream.writeUTF(this.h);
                }
                dataOutputStream.writeShort(this.k.size());
                if (this.k != null) {
                    for (Entry entry : this.k.entrySet()) {
                        ij ijVar = (ij) entry.getValue();
                        dataOutputStream.writeLong(((Long) entry.getKey()).longValue());
                        dataOutputStream.writeUTF(ijVar.t());
                        dataOutputStream.writeShort(ijVar.a.size());
                        Iterator it = ijVar.a.iterator();
                        while (it.hasNext()) {
                            ik ikVar = (ik) it.next();
                            dataOutputStream.writeShort(ikVar.a);
                            dataOutputStream.writeLong(ikVar.b);
                            dataOutputStream.writeLong(ikVar.c);
                            dataOutputStream.writeBoolean(ikVar.d);
                            dataOutputStream.writeShort(ikVar.e);
                            dataOutputStream.writeShort(ikVar.f.a());
                            if ((ikVar.e < 200 || ikVar.e >= 400) && ikVar.g != null) {
                                byte[] bytes = ikVar.g.getBytes();
                                dataOutputStream.writeShort(bytes.length);
                                dataOutputStream.write(bytes);
                            }
                            dataOutputStream.writeShort(ikVar.h);
                            dataOutputStream.writeInt((int) ikVar.k);
                        }
                    }
                }
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                lt.a(dataOutputStream);
                return toByteArray;
            } catch (IOException e2) {
                e = e2;
                closeable = dataOutputStream;
                try {
                    kg.a(6, a, "Error when generating report", e);
                    throw e;
                } catch (Throwable th) {
                    e = th;
                    dataOutputStream = closeable;
                    lt.a(dataOutputStream);
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                lt.a(dataOutputStream);
                throw e;
            }
        } catch (IOException e3) {
            e = e3;
            kg.a(6, a, "Error when generating report", e);
            throw e;
        } catch (Throwable th3) {
            e = th3;
            dataOutputStream = null;
            lt.a(dataOutputStream);
            throw e;
        }
    }
}
