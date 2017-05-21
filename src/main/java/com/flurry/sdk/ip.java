package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ip {
    private byte[] a;

    public static class a implements lb<ip> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, ip ipVar) throws IOException {
            if (outputStream != null && ipVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                anonymousClass1.writeShort(ipVar.a.length);
                anonymousClass1.write(ipVar.a);
                anonymousClass1.writeShort(0);
                anonymousClass1.flush();
            }
        }

        public ip a(InputStream inputStream) throws IOException {
            ip ipVar = null;
            if (inputStream != null) {
                DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                short readShort = anonymousClass2.readShort();
                if (readShort != (short) 0) {
                    ipVar = new ip();
                    ipVar.a = new byte[readShort];
                    anonymousClass2.readFully(ipVar.a);
                    if (anonymousClass2.readUnsignedShort() == 0) {
                    }
                }
            }
            return ipVar;
        }
    }

    public ip(byte[] bArr) {
        this.a = bArr;
    }

    public byte[] a() {
        return this.a;
    }
}
