package com.flurry.sdk;

import com.flurry.sdk.jc.a;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public final class ho {
    private static final String a = ho.class.getSimpleName();

    public static jc a(File file) {
        Throwable e;
        if (file == null || !file.exists()) {
            return null;
        }
        lb aVar = new a();
        Closeable fileInputStream;
        Closeable dataInputStream;
        jc jcVar;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                dataInputStream = new DataInputStream(fileInputStream);
            } catch (Exception e2) {
                e = e2;
                dataInputStream = null;
                try {
                    kg.a(3, a, "Error loading legacy agent data.", e);
                    lt.a(dataInputStream);
                    lt.a(fileInputStream);
                    jcVar = null;
                    return jcVar;
                } catch (Throwable th) {
                    e = th;
                    lt.a(dataInputStream);
                    lt.a(fileInputStream);
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                dataInputStream = null;
                lt.a(dataInputStream);
                lt.a(fileInputStream);
                throw e;
            }
            try {
                if (dataInputStream.readUnsignedShort() != 46586) {
                    kg.a(3, a, "Unexpected file type");
                    lt.a(dataInputStream);
                    lt.a(fileInputStream);
                    return null;
                }
                int readUnsignedShort = dataInputStream.readUnsignedShort();
                if (readUnsignedShort != 2) {
                    kg.a(6, a, "Unknown agent file version: " + readUnsignedShort);
                    lt.a(dataInputStream);
                    lt.a(fileInputStream);
                    return null;
                }
                jcVar = (jc) aVar.b(dataInputStream);
                lt.a(dataInputStream);
                lt.a(fileInputStream);
                return jcVar;
            } catch (Exception e3) {
                e = e3;
                kg.a(3, a, "Error loading legacy agent data.", e);
                lt.a(dataInputStream);
                lt.a(fileInputStream);
                jcVar = null;
                return jcVar;
            }
        } catch (Exception e4) {
            e = e4;
            dataInputStream = null;
            fileInputStream = null;
            kg.a(3, a, "Error loading legacy agent data.", e);
            lt.a(dataInputStream);
            lt.a(fileInputStream);
            jcVar = null;
            return jcVar;
        } catch (Throwable th3) {
            e = th3;
            dataInputStream = null;
            fileInputStream = null;
            lt.a(dataInputStream);
            lt.a(fileInputStream);
            throw e;
        }
    }
}
