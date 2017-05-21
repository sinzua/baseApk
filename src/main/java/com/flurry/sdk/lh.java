package com.flurry.sdk;

import android.app.Activity;
import android.content.Context;
import com.flurry.sdk.ll.a;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class lh implements a {
    private static lh a;
    private static final String b = lh.class.getSimpleName();
    private final Map<Context, lf> c = new WeakHashMap();
    private final li d = new li();
    private final Object e = new Object();
    private long f;
    private long g;
    private lf h;
    private kb<lj> i = new kb<lj>(this) {
        final /* synthetic */ lh a;

        {
            this.a = r1;
        }

        public void a(lj ljVar) {
            this.a.i();
        }
    };
    private kb<jv> j = new kb<jv>(this) {
        final /* synthetic */ lh a;

        {
            this.a = r1;
        }

        public void a(jv jvVar) {
            switch (jvVar.b) {
                case kStarted:
                    kg.a(3, lh.b, "Automatic onStartSession for context:" + jvVar.a);
                    this.a.e(jvVar.a);
                    return;
                case kStopped:
                    kg.a(3, lh.b, "Automatic onEndSession for context:" + jvVar.a);
                    this.a.d(jvVar.a);
                    return;
                case kDestroyed:
                    kg.a(3, lh.b, "Automatic onEndSession (destroyed) for context:" + jvVar.a);
                    this.a.d(jvVar.a);
                    return;
                default:
                    return;
            }
        }
    };

    public static synchronized lh a() {
        lh lhVar;
        synchronized (lh.class) {
            if (a == null) {
                a = new lh();
            }
            lhVar = a;
        }
        return lhVar;
    }

    public static synchronized void b() {
        synchronized (lh.class) {
            if (a != null) {
                kc.a().a(a.i);
                kc.a().a(a.j);
                lk.a().b("ContinueSessionMillis", a);
            }
            a = null;
        }
    }

    private lh() {
        ll a = lk.a();
        this.f = 0;
        this.g = ((Long) a.a("ContinueSessionMillis")).longValue();
        a.a("ContinueSessionMillis", (a) this);
        kg.a(4, b, "initSettings, ContinueSessionMillis = " + this.g);
        kc.a().a("com.flurry.android.sdk.ActivityLifecycleEvent", this.j);
        kc.a().a("com.flurry.android.sdk.FlurrySessionTimerEvent", this.i);
    }

    public long c() {
        return this.f;
    }

    public synchronized int d() {
        return this.c.size();
    }

    public lf e() {
        lf lfVar;
        synchronized (this.e) {
            lfVar = this.h;
        }
        return lfVar;
    }

    private void a(lf lfVar) {
        synchronized (this.e) {
            this.h = lfVar;
        }
    }

    private void b(lf lfVar) {
        synchronized (this.e) {
            if (this.h == lfVar) {
                this.h = null;
            }
        }
    }

    public synchronized void a(Context context) {
        if (context instanceof Activity) {
            if (jw.a().c()) {
                kg.a(3, b, "bootstrap for context:" + context);
                e(context);
            }
        }
    }

    public synchronized void b(Context context) {
        if (!(jw.a().c() && (context instanceof Activity))) {
            kg.a(3, b, "Manual onStartSession for context:" + context);
            e(context);
        }
    }

    public synchronized void c(Context context) {
        if (!(jw.a().c() && (context instanceof Activity))) {
            kg.a(3, b, "Manual onEndSession for context:" + context);
            d(context);
        }
    }

    public synchronized boolean f() {
        boolean z;
        if (e() == null) {
            kg.a(2, b, "Session not found. No active session");
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    public synchronized void g() {
        for (Entry entry : this.c.entrySet()) {
            lg lgVar = new lg();
            lgVar.a = new WeakReference(entry.getKey());
            lgVar.b = (lf) entry.getValue();
            lgVar.c = lg.a.END;
            lgVar.d = je.a().d();
            lgVar.b();
        }
        this.c.clear();
        js.a().b(new ly(this) {
            final /* synthetic */ lh a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.i();
            }
        });
    }

    private synchronized void e(Context context) {
        if (((lf) this.c.get(context)) == null) {
            lg lgVar;
            this.d.a();
            lf e = e();
            if (e == null) {
                e = new lf();
                kg.e(b, "Flurry session started for context:" + context);
                lgVar = new lg();
                lgVar.a = new WeakReference(context);
                lgVar.b = e;
                lgVar.c = lg.a.CREATE;
                lgVar.b();
            }
            this.c.put(context, e);
            a(e);
            kg.e(b, "Flurry session resumed for context:" + context);
            lgVar = new lg();
            lgVar.a = new WeakReference(context);
            lgVar.b = e;
            lgVar.c = lg.a.START;
            lgVar.b();
            this.f = 0;
        } else if (jw.a().c()) {
            kg.a(3, b, "Session already started with context:" + context);
        } else {
            kg.e(b, "Session already started with context:" + context);
        }
    }

    synchronized void d(Context context) {
        lf lfVar = (lf) this.c.remove(context);
        if (lfVar != null) {
            kg.e(b, "Flurry session paused for context:" + context);
            lg lgVar = new lg();
            lgVar.a = new WeakReference(context);
            lgVar.b = lfVar;
            lgVar.d = je.a().d();
            lgVar.c = lg.a.END;
            lgVar.b();
            if (d() == 0) {
                this.d.a(this.g);
                this.f = System.currentTimeMillis();
            } else {
                this.f = 0;
            }
        } else if (jw.a().c()) {
            kg.a(3, b, "Session cannot be ended, session not found for context:" + context);
        } else {
            kg.e(b, "Session cannot be ended, session not found for context:" + context);
        }
    }

    private synchronized void i() {
        int d = d();
        if (d > 0) {
            kg.a(5, b, "Session cannot be finalized, sessionContextCount:" + d);
        } else {
            final lf e = e();
            if (e == null) {
                kg.a(5, b, "Session cannot be finalized, current session not found");
            } else {
                kg.e(b, "Flurry session ended");
                lg lgVar = new lg();
                lgVar.b = e;
                lgVar.c = lg.a.FINALIZE;
                lgVar.d = je.a().d();
                lgVar.b();
                js.a().b(new ly(this) {
                    final /* synthetic */ lh b;

                    public void a() {
                        this.b.b(e);
                    }
                });
            }
        }
    }

    public void a(String str, Object obj) {
        if (str.equals("ContinueSessionMillis")) {
            this.g = ((Long) obj).longValue();
            kg.a(4, b, "onSettingUpdate, ContinueSessionMillis = " + this.g);
            return;
        }
        kg.a(6, b, "onSettingUpdate internal error!");
    }
}
