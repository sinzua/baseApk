package com.flurry.sdk;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class iz {
    private static final String b = iz.class.getSimpleName();
    byte[] a;

    public static class a implements lb<iz> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, iz izVar) throws IOException {
            if (outputStream != null && izVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                int i = 0;
                if (izVar.a != null) {
                    i = izVar.a.length;
                }
                anonymousClass1.writeShort(i);
                if (i > 0) {
                    anonymousClass1.write(izVar.a);
                }
                anonymousClass1.flush();
            }
        }

        public iz a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            iz izVar = new iz();
            int readUnsignedShort = anonymousClass2.readUnsignedShort();
            if (readUnsignedShort > 0) {
                byte[] bArr = new byte[readUnsignedShort];
                anonymousClass2.readFully(bArr);
                izVar.a = bArr;
            }
            return izVar;
        }
    }

    private iz() {
    }

    public iz(byte[] bArr) {
        this.a = bArr;
    }

    public iz(ja jaVar) throws IOException {
        Throwable e;
        Closeable closeable = null;
        Closeable dataOutputStream;
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                int i;
                int i2;
                dataOutputStream.writeShort(7);
                dataOutputStream.writeUTF(jaVar.a());
                dataOutputStream.writeLong(jaVar.b());
                dataOutputStream.writeLong(jaVar.c());
                dataOutputStream.writeLong(jaVar.d());
                dataOutputStream.writeBoolean(true);
                dataOutputStream.writeByte(-1);
                if (TextUtils.isEmpty(jaVar.f())) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeUTF(jaVar.f());
                }
                if (TextUtils.isEmpty(jaVar.g())) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeUTF(jaVar.g());
                }
                Map h = jaVar.h();
                if (h == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(h.size());
                    for (Entry entry : h.entrySet()) {
                        dataOutputStream.writeUTF((String) entry.getKey());
                        dataOutputStream.writeUTF((String) entry.getValue());
                    }
                }
                h = jaVar.e();
                if (h == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(h.size());
                    for (Entry entry2 : h.entrySet()) {
                        dataOutputStream.writeUTF((String) entry2.getKey());
                        dataOutputStream.writeUTF((String) entry2.getValue());
                        dataOutputStream.writeByte(0);
                    }
                }
                dataOutputStream.writeUTF(jaVar.i());
                dataOutputStream.writeUTF(jaVar.j());
                dataOutputStream.writeByte(jaVar.k());
                dataOutputStream.writeByte(jaVar.l());
                dataOutputStream.writeUTF(jaVar.m());
                if (jaVar.n() == null) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeDouble(lt.a(jaVar.n().getLatitude(), 3));
                    dataOutputStream.writeDouble(lt.a(jaVar.n().getLongitude(), 3));
                    dataOutputStream.writeFloat(jaVar.n().getAccuracy());
                }
                dataOutputStream.writeInt(jaVar.o());
                dataOutputStream.writeByte(-1);
                dataOutputStream.writeByte(-1);
                dataOutputStream.writeByte(jaVar.p());
                if (jaVar.q() == null) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeLong(jaVar.q().longValue());
                }
                h = jaVar.r();
                if (h == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(h.size());
                    for (Entry entry22 : h.entrySet()) {
                        dataOutputStream.writeUTF((String) entry22.getKey());
                        dataOutputStream.writeInt(((iv) entry22.getValue()).a);
                    }
                }
                List<iw> s = jaVar.s();
                if (s == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(s.size());
                    for (iw e2 : s) {
                        dataOutputStream.write(e2.e());
                    }
                }
                dataOutputStream.writeBoolean(jaVar.t());
                List v = jaVar.v();
                if (v != null) {
                    int i3 = 0;
                    i = 0;
                    for (i2 = 0; i2 < v.size(); i2++) {
                        i3 += ((iu) v.get(i2)).a();
                        if (i3 > 160000) {
                            kg.a(5, b, "Error Log size exceeded. No more event details logged.");
                            i2 = i;
                            break;
                        }
                        i++;
                    }
                    i2 = i;
                } else {
                    i2 = 0;
                }
                dataOutputStream.writeInt(jaVar.u());
                dataOutputStream.writeShort(i2);
                for (i = 0; i < i2; i++) {
                    dataOutputStream.write(((iu) v.get(i)).b());
                }
                dataOutputStream.writeInt(-1);
                dataOutputStream.writeShort(0);
                dataOutputStream.writeShort(0);
                dataOutputStream.writeShort(0);
                this.a = byteArrayOutputStream.toByteArray();
                lt.a(dataOutputStream);
            } catch (IOException e3) {
                e = e3;
                closeable = dataOutputStream;
                try {
                    kg.a(6, b, "", e);
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
        } catch (IOException e4) {
            e = e4;
            kg.a(6, b, "", e);
            throw e;
        } catch (Throwable th3) {
            e = th3;
            dataOutputStream = null;
            lt.a(dataOutputStream);
            throw e;
        }
    }

    public byte[] a() {
        return this.a;
    }
}
