package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class kw {
    private String a;

    public static class a implements lb<kw> {
        public /* synthetic */ Object b(InputStream inputStream) throws IOException {
            return a(inputStream);
        }

        public void a(OutputStream outputStream, kw kwVar) throws IOException {
            if (outputStream != null && kwVar != null) {
                DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                    final /* synthetic */ a a;

                    public void close() {
                    }
                };
                anonymousClass1.writeUTF(kwVar.a);
                anonymousClass1.flush();
            }
        }

        public kw a(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            DataInputStream anonymousClass2 = new DataInputStream(this, inputStream) {
                final /* synthetic */ a a;

                public void close() {
                }
            };
            kw kwVar = new kw();
            kwVar.a = anonymousClass2.readUTF();
            return kwVar;
        }
    }

    private kw() {
    }

    public kw(String str) {
        this.a = str;
    }

    public String a() {
        return this.a;
    }
}
