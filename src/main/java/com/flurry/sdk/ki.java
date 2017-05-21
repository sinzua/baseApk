package com.flurry.sdk;

import android.content.Context;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ki {
    private static final String a = ki.class.getSimpleName();
    private static final Map<Class<? extends kj>, kh> b = new LinkedHashMap();
    private final Map<Class<? extends kj>, kj> c = new LinkedHashMap();

    public static void a(Class<? extends kj> cls, int i) {
        if (cls != null) {
            synchronized (b) {
                b.put(cls, new kh(cls, i));
            }
        }
    }

    public synchronized void a(Context context) {
        if (context == null) {
            kg.a(5, a, "Null context.");
        } else {
            synchronized (b) {
                List<kh> arrayList = new ArrayList(b.values());
            }
            for (kh khVar : arrayList) {
                try {
                    if (khVar.b()) {
                        kj kjVar = (kj) khVar.a().newInstance();
                        kjVar.a(context);
                        this.c.put(khVar.a(), kjVar);
                    }
                } catch (Throwable e) {
                    kg.a(5, a, "Flurry Module for class " + khVar.a() + " is not available:", e);
                }
            }
            lh.a().a(context);
            jw.a();
        }
    }

    public synchronized void a() {
        jw.b();
        lh.b();
        List b = b();
        for (int size = b.size() - 1; size >= 0; size--) {
            try {
                ((kj) this.c.remove(((kj) b.get(size)).getClass())).b();
            } catch (Throwable e) {
                kg.a(5, a, "Error destroying module:", e);
            }
        }
    }

    public kj a(Class<? extends kj> cls) {
        if (cls == null) {
            return null;
        }
        synchronized (this.c) {
            kj kjVar = (kj) this.c.get(cls);
        }
        if (kjVar != null) {
            return kjVar;
        }
        throw new IllegalStateException("Module was not registered/initialized. " + cls);
    }

    private List<kj> b() {
        List<kj> arrayList = new ArrayList();
        synchronized (this.c) {
            arrayList.addAll(this.c.values());
        }
        return arrayList;
    }
}
