package com.flurry.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

public class js {
    private static js a;
    private static final String b = js.class.getSimpleName();
    private final Context c;
    private final Handler d = new Handler(Looper.getMainLooper());
    private final Handler e;
    private final HandlerThread f = new HandlerThread("FlurryAgent");
    private final String g;
    private final ki h;

    public static js a() {
        return a;
    }

    public static synchronized void a(Context context, String str) {
        synchronized (js.class) {
            if (a != null) {
                if (!a.d().equals(str)) {
                    throw new IllegalStateException("Only one API key per application is supported!");
                }
            } else if (context == null) {
                throw new IllegalArgumentException("Context cannot be null");
            } else if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("API key must be specified");
            } else {
                a = new js(context, str);
                a.a(context);
            }
        }
    }

    public static synchronized void b() {
        synchronized (js.class) {
            if (a != null) {
                a.h();
                a = null;
            }
        }
    }

    private js(Context context, String str) {
        this.c = context.getApplicationContext();
        this.f.start();
        this.e = new Handler(this.f.getLooper());
        this.g = str;
        this.h = new ki();
    }

    private void h() {
        i();
        this.f.quit();
    }

    private void a(Context context) {
        this.h.a(context);
    }

    private void i() {
        this.h.a();
    }

    public Context c() {
        return this.c;
    }

    public String d() {
        return this.g;
    }

    public PackageManager e() {
        return this.c.getPackageManager();
    }

    public Handler f() {
        return this.d;
    }

    public void a(Runnable runnable) {
        if (runnable != null) {
            this.d.post(runnable);
        }
    }

    public void a(Runnable runnable, long j) {
        if (runnable != null) {
            this.d.postDelayed(runnable, j);
        }
    }

    public Handler g() {
        return this.e;
    }

    public void b(Runnable runnable) {
        if (runnable != null) {
            this.e.post(runnable);
        }
    }

    public void b(Runnable runnable, long j) {
        if (runnable != null) {
            this.e.postDelayed(runnable, j);
        }
    }

    public void c(Runnable runnable) {
        if (runnable != null) {
            this.e.removeCallbacks(runnable);
        }
    }

    public kj a(Class<? extends kj> cls) {
        return this.h.a((Class) cls);
    }
}
