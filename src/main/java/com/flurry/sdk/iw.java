package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class iw {
    private final Map<String, String> a = new HashMap();
    private int b;
    private String c;
    private long d;
    private boolean e;
    private boolean f;
    private long g;

    public iw(int i, String str, Map<String, String> map, long j, boolean z) {
        this.b = i;
        this.c = str;
        if (map != null) {
            this.a.putAll(map);
        }
        this.d = j;
        this.e = z;
        if (this.e) {
            this.f = false;
        } else {
            this.f = true;
        }
    }

    public boolean a() {
        return this.e;
    }

    public boolean b() {
        return this.f;
    }

    public boolean a(String str) {
        return this.e && this.g == 0 && this.c.equals(str);
    }

    public void a(long j) {
        this.f = true;
        this.g = j - this.d;
        kg.a(3, "FlurryAgent", "Ended event '" + this.c + "' (" + this.d + ") after " + this.g + "ms");
    }

    public synchronized void a(Map<String, String> map) {
        if (map != null) {
            this.a.putAll(map);
        }
    }

    public synchronized Map<String, String> c() {
        return new HashMap(this.a);
    }

    public synchronized void b(Map<String, String> map) {
        this.a.clear();
        if (map != null) {
            this.a.putAll(map);
        }
    }

    public int d() {
        return e().length;
    }

    public synchronized byte[] e() {
        Closeable dataOutputStream;
        byte[] toByteArray;
        Closeable closeable;
        Throwable th;
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeShort(this.b);
                dataOutputStream.writeUTF(this.c);
                dataOutputStream.writeShort(this.a.size());
                for (Entry entry : this.a.entrySet()) {
                    dataOutputStream.writeUTF(lt.b((String) entry.getKey()));
                    dataOutputStream.writeUTF(lt.b((String) entry.getValue()));
                }
                dataOutputStream.writeLong(this.d);
                dataOutputStream.writeLong(this.g);
                dataOutputStream.flush();
                toByteArray = byteArrayOutputStream.toByteArray();
                lt.a(dataOutputStream);
            } catch (IOException e) {
                closeable = dataOutputStream;
                try {
                    toByteArray = new byte[0];
                    lt.a(closeable);
                    return toByteArray;
                } catch (Throwable th2) {
                    th = th2;
                    dataOutputStream = closeable;
                    lt.a(dataOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                lt.a(dataOutputStream);
                throw th;
            }
        } catch (IOException e2) {
            closeable = null;
            toByteArray = new byte[0];
            lt.a(closeable);
            return toByteArray;
        } catch (Throwable th4) {
            dataOutputStream = null;
            th = th4;
            lt.a(dataOutputStream);
            throw th;
        }
        return toByteArray;
    }
}
