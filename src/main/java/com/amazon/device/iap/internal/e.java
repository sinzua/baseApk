package com.amazon.device.iap.internal;

import android.util.Log;
import com.amazon.device.iap.internal.a.d;
import com.amazon.device.iap.internal.b.g;

/* compiled from: ImplementationFactory */
public final class e {
    private static final String a = e.class.getName();
    private static volatile boolean b;
    private static volatile boolean c;
    private static volatile c d;
    private static volatile a e;
    private static volatile b f;

    private static b d() {
        if (f == null) {
            synchronized (e.class) {
                if (f == null) {
                    if (a()) {
                        f = new d();
                    } else {
                        f = new g();
                    }
                }
            }
        }
        return f;
    }

    public static boolean a() {
        if (c) {
            return b;
        }
        synchronized (e.class) {
            if (c) {
                boolean z = b;
                return z;
            }
            try {
                e.class.getClassLoader().loadClass("com.amazon.android.Kiwi");
                b = false;
            } catch (Throwable th) {
                b = true;
            }
            c = true;
            return b;
        }
    }

    public static c b() {
        if (d == null) {
            synchronized (e.class) {
                if (d == null) {
                    d = (c) a(c.class);
                }
            }
        }
        return d;
    }

    public static a c() {
        if (e == null) {
            synchronized (e.class) {
                if (e == null) {
                    e = (a) a(a.class);
                }
            }
        }
        return e;
    }

    private static <T> T a(Class<T> cls) {
        T t = null;
        try {
            t = d().a(cls).newInstance();
        } catch (Throwable e) {
            Log.e(a, "error getting instance for " + cls, e);
        }
        return t;
    }
}
