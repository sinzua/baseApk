package com.flurry.sdk;

import android.os.Build;
import android.os.Build.VERSION;
import com.flurry.sdk.ip.a;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.CRC32;

public class io {
    private static final String b = io.class.getName();
    private static io c = null;
    private String a;
    private jz<List<ip>> d;
    private List<ip> e;
    private boolean f;

    private io() {
    }

    public static synchronized io a() {
        io ioVar;
        synchronized (io.class) {
            if (c == null) {
                c = new io();
                c.e();
            }
            ioVar = c;
        }
        return ioVar;
    }

    private void e() {
        this.d = new jz(js.a().c().getFileStreamPath(f()), ".yflurrypulselogging.", 1, new le<List<ip>>(this) {
            final /* synthetic */ io a;

            {
                this.a = r1;
            }

            public lb<List<ip>> a(int i) {
                return new la(new a());
            }
        });
        this.f = ((Boolean) lk.a().a("UseHttps")).booleanValue();
        kg.a(4, b, "initSettings, UseHttps = " + this.f);
        this.e = (List) this.d.a();
        if (this.e == null) {
            this.e = new ArrayList();
        }
    }

    public synchronized void a(in inVar) {
        try {
            this.e.add(new ip(inVar.h()));
            kg.a(4, b, "Saving persistent Pulse logging data.");
            this.d.a(this.e);
        } catch (IOException e) {
            kg.a(6, b, "Error when generating pulse log report in addReport part");
        }
    }

    public byte[] b() throws IOException {
        Throwable e;
        Closeable closeable = null;
        Closeable dataOutputStream;
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                byte[] toByteArray;
                if (this.e == null || this.e.isEmpty()) {
                    toByteArray = byteArrayOutputStream.toByteArray();
                    lt.a(dataOutputStream);
                } else {
                    dataOutputStream.writeShort(1);
                    dataOutputStream.writeShort(1);
                    dataOutputStream.writeLong(System.currentTimeMillis());
                    dataOutputStream.writeUTF(js.a().d());
                    dataOutputStream.writeUTF(jo.a().e());
                    dataOutputStream.writeShort(jt.a());
                    dataOutputStream.writeShort(3);
                    dataOutputStream.writeUTF(jo.a().d());
                    dataOutputStream.writeBoolean(jf.a().e());
                    List<hu> arrayList = new ArrayList();
                    for (Entry entry : jf.a().h().entrySet()) {
                        hu huVar = new hu();
                        huVar.a = ((jn) entry.getKey()).d;
                        if (((jn) entry.getKey()).e) {
                            huVar.b = new String((byte[]) entry.getValue());
                        } else {
                            huVar.b = lt.b((byte[]) entry.getValue());
                        }
                        arrayList.add(huVar);
                    }
                    dataOutputStream.writeShort(arrayList.size());
                    for (hu huVar2 : arrayList) {
                        dataOutputStream.writeShort(huVar2.a);
                        toByteArray = huVar2.b.getBytes();
                        dataOutputStream.writeShort(toByteArray.length);
                        dataOutputStream.write(toByteArray);
                    }
                    dataOutputStream.writeShort(6);
                    dataOutputStream.writeShort(ih.MODEL.a());
                    dataOutputStream.writeUTF(Build.MODEL);
                    dataOutputStream.writeShort(ih.BRAND.a());
                    dataOutputStream.writeUTF(Build.BOARD);
                    dataOutputStream.writeShort(ih.ID.a());
                    dataOutputStream.writeUTF(Build.ID);
                    dataOutputStream.writeShort(ih.DEVICE.a());
                    dataOutputStream.writeUTF(Build.DEVICE);
                    dataOutputStream.writeShort(ih.PRODUCT.a());
                    dataOutputStream.writeUTF(Build.PRODUCT);
                    dataOutputStream.writeShort(ih.VERSION_RELEASE.a());
                    dataOutputStream.writeUTF(VERSION.RELEASE);
                    dataOutputStream.writeShort(this.e.size());
                    for (ip a : this.e) {
                        dataOutputStream.write(a.a());
                    }
                    toByteArray = byteArrayOutputStream.toByteArray();
                    CRC32 crc32 = new CRC32();
                    crc32.update(toByteArray);
                    dataOutputStream.writeInt((int) crc32.getValue());
                    toByteArray = byteArrayOutputStream.toByteArray();
                    lt.a(dataOutputStream);
                }
                return toByteArray;
            } catch (IOException e2) {
                e = e2;
                closeable = dataOutputStream;
            } catch (Throwable th) {
                e = th;
            }
        } catch (IOException e3) {
            e = e3;
            try {
                kg.a(6, b, "Error when generating report", e);
                throw e;
            } catch (Throwable th2) {
                e = th2;
                dataOutputStream = closeable;
                lt.a(dataOutputStream);
                throw e;
            }
        } catch (Throwable th3) {
            e = th3;
            dataOutputStream = null;
            lt.a(dataOutputStream);
            throw e;
        }
    }

    public synchronized void a(byte[] bArr) {
        if (jl.a().c()) {
            if (bArr != null) {
                if (bArr.length != 0) {
                    String g = g();
                    kg.a(4, b, "PulseLoggingManager: start upload data " + Arrays.toString(bArr) + " to " + g);
                    lz knVar = new kn();
                    knVar.a(g);
                    knVar.d(100000);
                    knVar.a(kp.a.kPost);
                    knVar.b(true);
                    knVar.a("Content-Type", "application/octet-stream");
                    knVar.a(new kx());
                    knVar.a((Object) bArr);
                    knVar.a(new kn.a<byte[], Void>(this) {
                        final /* synthetic */ io a;

                        {
                            this.a = r1;
                        }

                        public void a(kn<byte[], Void> knVar, Void voidR) {
                            int h = knVar.h();
                            if (h <= 0) {
                                kg.e(io.b, "Server Error: " + h);
                            } else if (h < 200 || h >= 300) {
                                kg.a(3, io.b, "Pulse logging report sent unsuccessfully, HTTP response:" + h);
                            } else {
                                kg.a(3, io.b, "Pulse logging report sent successfully HTTP response:" + h);
                                this.a.e.clear();
                                this.a.d.a(this.a.e);
                            }
                        }
                    });
                    jq.a().a((Object) this, knVar);
                }
            }
            kg.a(3, b, "No report need be sent");
        } else {
            kg.a(5, b, "Reports were not sent! No Internet connection!");
        }
    }

    public synchronized void c() {
        try {
            a(b());
        } catch (IOException e) {
            kg.a(6, b, "Report not send due to exception in generate data");
        }
    }

    private String f() {
        return ".yflurrypulselogging." + Long.toString(lt.i(js.a().d()), 16);
    }

    private String g() {
        if (this.a != null) {
            return this.a;
        }
        if (this.f) {
            return "https://data.flurry.com/pcr.do";
        }
        return "https://data.flurry.com/pcr.do";
    }

    public void a(String str) {
        if (!(str == null || str.endsWith(".do"))) {
            kg.a(5, b, "overriding analytics agent report URL without an endpoint, are you sure?");
        }
        this.a = str;
    }
}
