package com.flurry.sdk;

public class lo {
    private static long a = 100;
    private static lo b = null;
    private final lp c = new lp();

    public static synchronized lo a() {
        lo loVar;
        synchronized (lo.class) {
            if (b == null) {
                b = new lo();
            }
            loVar = b;
        }
        return loVar;
    }

    public static synchronized void b() {
        synchronized (lo.class) {
            if (b != null) {
                b.c();
                b = null;
            }
        }
    }

    public lo() {
        this.c.a(a);
        this.c.a(true);
    }

    public synchronized void a(kb<ln> kbVar) {
        kc.a().a("com.flurry.android.sdk.TickEvent", kbVar);
        if (kc.a().b("com.flurry.android.sdk.TickEvent") > 0) {
            this.c.a();
        }
    }

    public synchronized void b(kb<ln> kbVar) {
        kc.a().b("com.flurry.android.sdk.TickEvent", kbVar);
        if (kc.a().b("com.flurry.android.sdk.TickEvent") == 0) {
            this.c.b();
        }
    }

    public synchronized void c() {
        kc.a().a("com.flurry.android.sdk.TickEvent");
        this.c.b();
    }
}
