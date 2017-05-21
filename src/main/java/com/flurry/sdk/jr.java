package com.flurry.sdk;

import android.content.Context;
import android.os.SystemClock;
import com.flurry.sdk.lg.a;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class jr {
    private static final String a = jr.class.getSimpleName();
    private final kb<lg> b = new kb<lg>(this) {
        final /* synthetic */ jr a;

        {
            this.a = r1;
        }

        public void a(lg lgVar) {
            if (this.a.c == null || lgVar.b == this.a.c.get()) {
                switch (lgVar.c) {
                    case CREATE:
                        this.a.a(lgVar.b, (Context) lgVar.a.get());
                        return;
                    case START:
                        this.a.a((Context) lgVar.a.get());
                        return;
                    case END:
                        this.a.b((Context) lgVar.a.get());
                        return;
                    case FINALIZE:
                        kc.a().b("com.flurry.android.sdk.FlurrySessionEvent", this.a.b);
                        this.a.a();
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private WeakReference<lf> c;
    private volatile long d = 0;
    private volatile long e = 0;
    private volatile long f = -1;
    private volatile long g = 0;
    private volatile long h = 0;
    private String i;
    private String j;
    private Map<String, String> k;

    public jr() {
        kc.a().a("com.flurry.android.sdk.FlurrySessionEvent", this.b);
        this.k = new LinkedHashMap<String, String>(this) {
            final /* synthetic */ jr a;

            {
                this.a = r1;
            }

            protected boolean removeEldestEntry(Entry<String, String> entry) {
                return size() > 10;
            }
        };
    }

    public void a(lf lfVar, Context context) {
        this.c = new WeakReference(lfVar);
        this.d = System.currentTimeMillis();
        this.e = SystemClock.elapsedRealtime();
        b(lfVar, context);
        js.a().b(new ly(this) {
            final /* synthetic */ jr a;

            {
                this.a = r1;
            }

            public void a() {
                jj.a().c();
            }
        });
    }

    private void b(lf lfVar, Context context) {
        if (lfVar == null || context == null) {
            kg.a(3, a, "Flurry session id cannot be created.");
            return;
        }
        kg.a(3, a, "Flurry session id started:" + this.d);
        lg lgVar = new lg();
        lgVar.a = new WeakReference(context);
        lgVar.b = lfVar;
        lgVar.c = a.SESSION_ID_CREATED;
        lgVar.b();
    }

    public void a(Context context) {
        long c = lh.a().c();
        if (c > 0) {
            this.g = (System.currentTimeMillis() - c) + this.g;
        }
    }

    public void b(Context context) {
        this.f = SystemClock.elapsedRealtime() - this.e;
    }

    public void a() {
    }

    public String b() {
        return Long.toString(this.d);
    }

    public long c() {
        return this.d;
    }

    public long d() {
        return this.e;
    }

    public long e() {
        return this.f;
    }

    public long f() {
        return this.g;
    }

    public synchronized long g() {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.e;
        if (elapsedRealtime <= this.h) {
            elapsedRealtime = this.h + 1;
            this.h = elapsedRealtime;
        }
        this.h = elapsedRealtime;
        return this.h;
    }

    public synchronized void a(String str) {
        this.i = str;
    }

    public synchronized void b(String str) {
        this.j = str;
    }

    public synchronized String h() {
        return this.i;
    }

    public synchronized String i() {
        return this.j;
    }

    public synchronized void a(String str, String str2) {
        this.k.put(str, str2);
    }

    public synchronized Map<String, String> j() {
        return this.k;
    }
}
