package com.flurry.sdk;

import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.Toast;
import com.flurry.sdk.in.a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class im {
    public static int a;
    public static int b;
    public static AtomicInteger c;
    static jz<List<in>> d;
    private static im f;
    private static Map<Integer, in> g;
    private String e = im.class.getSimpleName();
    private final AtomicInteger h;
    private long i;
    private kb<jk> j = new kb<jk>(this) {
        final /* synthetic */ im a;

        {
            this.a = r1;
        }

        public void a(jk jkVar) {
            kg.a(4, this.a.e, "onNetworkStateChanged : isNetworkEnable = " + jkVar.a);
            if (jkVar.a) {
                js.a().b(new Runnable(this) {
                    final /* synthetic */ AnonymousClass1 a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        io.a().c();
                    }
                });
            }
        }
    };

    private im() {
        g = new HashMap();
        this.h = new AtomicInteger(0);
        c = new AtomicInteger(0);
        if (b == 0) {
            b = 600000;
        }
        if (a == 0) {
            a = 15;
        }
        n();
        if (d == null) {
            q();
        }
        kc.a().a("com.flurry.android.sdk.NetworkStateEvent", this.j);
    }

    public static synchronized im a() {
        im imVar;
        synchronized (im.class) {
            if (f == null) {
                f = new im();
            }
            imVar = f;
        }
        return imVar;
    }

    public static void b() {
        if (f != null) {
            kc.a().b("com.flurry.android.sdk.NetworkStateEvent", f.j);
            g.clear();
            g = null;
            f = null;
        }
    }

    public static void a(int i) {
        a = i;
    }

    public static void b(int i) {
        b = i;
    }

    public synchronized void a(in inVar) {
        if (inVar == null) {
            kg.a(3, this.e, "Must add valid PulseCallbackAsyncReportInfo");
        } else {
            kg.a(3, this.e, "Adding and sending " + inVar.c() + " report to PulseCallbackManager.");
            if (inVar.d().size() != 0) {
                if (this.i == 0) {
                    this.i = System.currentTimeMillis() + ((long) b);
                    js.a().b(new Runnable(this) {
                        final /* synthetic */ im a;

                        {
                            this.a = r1;
                        }

                        public void run() {
                            this.a.o();
                        }
                    });
                }
                int p = p();
                inVar.a(p);
                g.put(Integer.valueOf(p), inVar);
                for (kr b : inVar.d()) {
                    hl.a().e().b(b);
                }
            }
        }
    }

    public synchronized void b(in inVar) {
        if (inVar == null) {
            kg.a(3, this.e, "Must add valid PulseCallbackAsyncReportInfo");
        } else {
            if (this.i == 0) {
                this.i = System.currentTimeMillis() + ((long) b);
                js.a().b(new Runnable(this) {
                    final /* synthetic */ im a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        this.a.o();
                    }
                });
            }
            int p = p();
            inVar.a(p);
            g.put(Integer.valueOf(p), inVar);
            for (ij ijVar : inVar.d()) {
                Iterator it = ijVar.a.iterator();
                while (it.hasNext()) {
                    ik ikVar = (ik) it.next();
                    c.incrementAndGet();
                    h();
                }
            }
            i();
            kg.a(3, this.e, "Restoring " + inVar.c() + " report to PulseCallbackManager. " + "Number of stored completed callbacks: " + c.get());
        }
    }

    public synchronized void c(int i) {
        kg.a(3, this.e, "Removing report " + i + " from PulseCallbackManager");
        g.remove(Integer.valueOf(i));
    }

    public List<in> c() {
        return new ArrayList(g.values());
    }

    public synchronized void a(final ik ikVar) {
        kg.a(3, this.e, ikVar.d() + " report sent successfully to " + ikVar.e());
        ikVar.f = il.COMPLETE;
        ikVar.g = "";
        c(ikVar);
        if (kg.c() <= 3 && kg.d()) {
            js.a().a(new Runnable(this) {
                final /* synthetic */ im b;

                public void run() {
                    Toast.makeText(js.a().c(), "PulseCallbackReportInfo HTTP Response Code: " + ikVar.e + " for url: " + ikVar.f(), 1).show();
                }
            });
        }
    }

    public synchronized boolean a(ik ikVar, String str) {
        boolean z;
        ikVar.h++;
        ikVar.i = System.currentTimeMillis();
        if (ikVar.c() || TextUtils.isEmpty(str)) {
            kg.a(3, this.e, "Maximum number of redirects attempted. Aborting: " + ikVar.d() + " report to " + ikVar.e());
            z = false;
            ikVar.f = il.INVALID_RESPONSE;
            ikVar.g = "";
            c(ikVar);
        } else {
            kg.a(3, this.e, "Report to " + ikVar.e() + " redirecting to url: " + str);
            z = true;
            ikVar.a(str);
            d();
        }
        return z;
    }

    public synchronized void b(ik ikVar) {
        kg.a(3, this.e, "Maximum number of attempts reached. Aborting: " + ikVar.d());
        ikVar.f = il.TIMEOUT;
        ikVar.i = System.currentTimeMillis();
        ikVar.g = "";
        c(ikVar);
    }

    public synchronized boolean b(ik ikVar, String str) {
        boolean z = false;
        synchronized (this) {
            ikVar.f = il.INVALID_RESPONSE;
            ikVar.i = System.currentTimeMillis();
            if (str == null) {
                str = "";
            }
            ikVar.g = str;
            if (ikVar.b()) {
                kg.a(3, this.e, "Maximum number of attempts reached. Aborting: " + ikVar.d() + " report to " + ikVar.e());
                c(ikVar);
            } else if (ma.h(ikVar.f())) {
                kg.a(3, this.e, "Retrying callback to " + ikVar.d() + " in: " + (ikVar.l.g() / 1000) + " seconds.");
                z = true;
                ikVar.a();
                c.incrementAndGet();
                d();
                g();
            } else {
                kg.a(3, this.e, "Url: " + ikVar.f() + " is invalid.");
                c(ikVar);
            }
        }
        return z;
    }

    public void d() {
        js.a().b(new Runnable(this) {
            final /* synthetic */ im a;

            {
                this.a = r1;
            }

            public void run() {
                List c = im.a().c();
                if (im.d == null) {
                    im.q();
                }
                im.d.a(c);
            }
        });
    }

    public List<in> e() {
        if (d == null) {
            q();
        }
        return (List) d.a();
    }

    private void c(ik ikVar) {
        ikVar.d = true;
        ikVar.a();
        c.incrementAndGet();
        ikVar.g();
        kg.a(3, this.e, ikVar.d() + " report to " + ikVar.e() + " finalized.");
        d();
        g();
    }

    private void g() {
        if (j() || k()) {
            kg.a(3, this.e, "Threshold reached. Sending callback logging reports");
            l();
        }
    }

    private void h() {
        if (j()) {
            kg.a(3, this.e, "Max Callback Attempts threshold reached. Sending callback logging reports");
            l();
        }
    }

    private void i() {
        if (k()) {
            kg.a(3, this.e, "Time threshold reached. Sending callback logging reports");
            l();
        }
    }

    private boolean j() {
        return c.intValue() >= a;
    }

    private boolean k() {
        return System.currentTimeMillis() > this.i;
    }

    private void l() {
        for (in inVar : c()) {
            int i = 0;
            for (ij ijVar : inVar.d()) {
                Iterator it = ijVar.a.iterator();
                while (it.hasNext()) {
                    ik ikVar = (ik) it.next();
                    if (ikVar.j) {
                        it.remove();
                    } else if (!ikVar.f.equals(il.PENDING_COMPLETION)) {
                        ikVar.j = true;
                        i = true;
                    }
                }
            }
            if (i != 0) {
                io.a().a(inVar);
            }
        }
        io.a().c();
        this.i = System.currentTimeMillis() + ((long) b);
        o();
        m();
        c = new AtomicInteger(0);
        d();
    }

    private void m() {
        List c = c();
        for (int i = 0; i < c.size(); i++) {
            in inVar = (in) c.get(i);
            if (inVar.f()) {
                c(inVar.a());
            } else {
                List d = inVar.d();
                for (int i2 = 0; i2 < d.size(); i2++) {
                    ij ijVar = (ij) d.get(i2);
                    if (ijVar.n()) {
                        inVar.e().remove(Long.valueOf(ijVar.e()));
                    } else {
                        Iterator it = ijVar.a.iterator();
                        while (it.hasNext()) {
                            if (((ik) it.next()).j) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private void n() {
        this.i = js.a().c().getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).getLong("timeToSendNextPulseReport", 0);
    }

    private void o() {
        Editor edit = js.a().c().getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).edit();
        edit.putLong("timeToSendNextPulseReport", this.i);
        edit.commit();
    }

    private synchronized int p() {
        return this.h.incrementAndGet();
    }

    private static void q() {
        d = new jz(js.a().c().getFileStreamPath(".yflurryanongoingpulsecallbackreporter"), ".yflurryanongoingpulsecallbackreporter", 2, new le<List<in>>() {
            public lb<List<in>> a(int i) {
                return new la(new a());
            }
        });
    }
}
