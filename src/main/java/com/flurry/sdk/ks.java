package com.flurry.sdk;

import com.ty.followboom.helpers.VLTools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ks<ReportInfo extends kr> {
    private static final String a = ks.class.getSimpleName();
    private static long b = VLTools.DEFAULT_RATE_US_THREHOLD;
    private final int c = Integer.MAX_VALUE;
    private final jz<List<ReportInfo>> d;
    private final List<ReportInfo> e = new ArrayList();
    private boolean f;
    private int g;
    private long h;
    private final Runnable i = new ly(this) {
        final /* synthetic */ ks a;

        {
            this.a = r1;
        }

        public void a() {
            this.a.b();
        }
    };
    private final kb<jk> j = new kb<jk>(this) {
        final /* synthetic */ ks a;

        {
            this.a = r1;
        }

        public void a(jk jkVar) {
            if (jkVar.a) {
                this.a.b();
            }
        }
    };

    protected abstract jz<List<ReportInfo>> a();

    protected abstract void a(ReportInfo reportInfo);

    public ks() {
        kc.a().a("com.flurry.android.sdk.NetworkStateEvent", this.j);
        this.d = a();
        this.h = b;
        this.g = -1;
        js.a().b(new ly(this) {
            final /* synthetic */ ks a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.b(this.a.e);
                this.a.b();
            }
        });
    }

    public void a(long j) {
        b = j;
        this.h = b;
    }

    public void c() {
        js.a().c(this.i);
        i();
    }

    public void d() {
        this.f = true;
    }

    public void e() {
        this.f = false;
        js.a().b(new ly(this) {
            final /* synthetic */ ks a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.b();
            }
        });
    }

    public synchronized void b(ReportInfo reportInfo) {
        if (reportInfo != null) {
            this.e.add(reportInfo);
            js.a().b(new ly(this) {
                final /* synthetic */ ks a;

                {
                    this.a = r1;
                }

                public void a() {
                    this.a.b();
                }
            });
        }
    }

    protected synchronized void c(ReportInfo reportInfo) {
        reportInfo.b(true);
        js.a().b(new ly(this) {
            final /* synthetic */ ks a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.f();
            }
        });
    }

    protected synchronized void d(ReportInfo reportInfo) {
        reportInfo.a_();
        js.a().b(new ly(this) {
            final /* synthetic */ ks a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.f();
            }
        });
    }

    private synchronized void b() {
        if (!this.f) {
            if (this.g >= 0) {
                kg.a(3, a, "Transmit is in progress");
            } else {
                h();
                if (this.e.isEmpty()) {
                    this.h = b;
                    this.g = -1;
                } else {
                    this.g = 0;
                    js.a().b(new ly(this) {
                        final /* synthetic */ ks a;

                        {
                            this.a = r1;
                        }

                        public void a() {
                            this.a.f();
                        }
                    });
                }
            }
        }
    }

    private synchronized void f() {
        kr krVar;
        lt.b();
        if (jl.a().c()) {
            while (this.g < this.e.size()) {
                List list = this.e;
                int i = this.g;
                this.g = i + 1;
                krVar = (kr) list.get(i);
                if (!krVar.q()) {
                    break;
                }
            }
            krVar = null;
        } else {
            kg.a(3, a, "Network is not available, aborting transmission");
            krVar = null;
        }
        if (krVar == null) {
            g();
        } else {
            a(krVar);
        }
    }

    private synchronized void g() {
        h();
        a(this.e);
        if (this.f) {
            kg.a(3, a, "Reporter paused");
            this.h = b;
        } else if (this.e.isEmpty()) {
            kg.a(3, a, "All reports sent successfully");
            this.h = b;
        } else {
            this.h <<= 1;
            kg.a(3, a, "One or more reports failed to send, backing off: " + this.h + "ms");
            js.a().b(this.i, this.h);
        }
        this.g = -1;
    }

    protected synchronized void b(List<ReportInfo> list) {
        lt.b();
        List list2 = (List) this.d.a();
        if (list2 != null) {
            list.addAll(list2);
        }
    }

    protected synchronized void a(List<ReportInfo> list) {
        lt.b();
        this.d.a(new ArrayList(list));
    }

    private synchronized void h() {
        Iterator it = this.e.iterator();
        while (it.hasNext()) {
            kr krVar = (kr) it.next();
            if (krVar.q()) {
                kg.a(3, a, "Url transmitted - " + krVar.s() + " Attempts: " + krVar.r());
                it.remove();
            } else if (krVar.r() > krVar.d()) {
                kg.a(3, a, "Exceeded max no of attempts - " + krVar.s() + " Attempts: " + krVar.r());
                it.remove();
            } else if (System.currentTimeMillis() > krVar.p() && krVar.r() > 0) {
                kg.a(3, a, "Expired: Time expired - " + krVar.s() + " Attempts: " + krVar.r());
                it.remove();
            }
        }
    }

    private void i() {
        kc.a().b("com.flurry.android.sdk.NetworkStateEvent", this.j);
    }
}
