package com.flurry.sdk;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class jz<T> {
    private static final String a = jz.class.getSimpleName();
    private final File b;
    private final lb<T> c;

    public jz(File file, String str, int i, le<T> leVar) {
        this.b = file;
        this.c = new kz(new ld(str, i, leVar));
    }

    public T a() {
        Throwable e;
        Throwable th;
        T t = null;
        if (this.b != null) {
            if (this.b.exists()) {
                Object obj = null;
                Closeable fileInputStream;
                try {
                    fileInputStream = new FileInputStream(this.b);
                    try {
                        t = this.c.b(fileInputStream);
                        lt.a(fileInputStream);
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            kg.a(3, a, "Error reading data file:" + this.b.getName(), e);
                            obj = 1;
                            lt.a(fileInputStream);
                            if (obj != null) {
                                kg.a(3, a, "Deleting data file:" + this.b.getName());
                                this.b.delete();
                            }
                            return t;
                        } catch (Throwable th2) {
                            th = th2;
                            lt.a(fileInputStream);
                            throw th;
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    fileInputStream = t;
                    kg.a(3, a, "Error reading data file:" + this.b.getName(), e);
                    obj = 1;
                    lt.a(fileInputStream);
                    if (obj != null) {
                        kg.a(3, a, "Deleting data file:" + this.b.getName());
                        this.b.delete();
                    }
                    return t;
                } catch (Throwable e4) {
                    fileInputStream = t;
                    th = e4;
                    lt.a(fileInputStream);
                    throw th;
                }
                if (obj != null) {
                    kg.a(3, a, "Deleting data file:" + this.b.getName());
                    this.b.delete();
                }
            } else {
                kg.a(5, a, "No data to read for file:" + this.b.getName());
            }
        }
        return t;
    }

    public void a(T t) {
        Throwable e;
        int i;
        Object obj = null;
        Closeable closeable = null;
        if (t == null) {
            kg.a(3, a, "No data to write for file:" + this.b.getName());
            obj = 1;
        } else {
            try {
                if (ls.a(this.b)) {
                    Closeable fileOutputStream = new FileOutputStream(this.b);
                    try {
                        this.c.a(fileOutputStream, t);
                        lt.a(fileOutputStream);
                    } catch (Exception e2) {
                        e = e2;
                        closeable = fileOutputStream;
                        try {
                            kg.a(3, a, "Error writing data file:" + this.b.getName(), e);
                            lt.a(closeable);
                            i = 1;
                            if (obj == null) {
                                kg.a(3, a, "Deleting data file:" + this.b.getName());
                                this.b.delete();
                            }
                        } catch (Throwable th) {
                            e = th;
                            lt.a(closeable);
                            throw e;
                        }
                    } catch (Throwable th2) {
                        e = th2;
                        closeable = fileOutputStream;
                        lt.a(closeable);
                        throw e;
                    }
                }
                throw new IOException("Cannot create parent directory!");
            } catch (Exception e3) {
                e = e3;
                kg.a(3, a, "Error writing data file:" + this.b.getName(), e);
                lt.a(closeable);
                i = 1;
                if (obj == null) {
                    kg.a(3, a, "Deleting data file:" + this.b.getName());
                    this.b.delete();
                }
            }
        }
        if (obj == null) {
            kg.a(3, a, "Deleting data file:" + this.b.getName());
            this.b.delete();
        }
    }

    public boolean b() {
        if (this.b == null) {
            return false;
        }
        return this.b.delete();
    }
}
