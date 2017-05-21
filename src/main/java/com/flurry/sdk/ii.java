package com.flurry.sdk;

import com.flurry.sdk.ij.a;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.List;
import java.util.Map;

public class ii extends ks<ij> {
    public static long a;
    private static final String b = ii.class.getSimpleName();

    public ii() {
        a(30000);
    }

    protected jz<List<ij>> a() {
        return new jz(js.a().c().getFileStreamPath(".yflurryanpulsecallbackreporter"), ".yflurryanpulsecallbackreporter", 2, new le<List<ij>>(this) {
            final /* synthetic */ ii a;

            {
                this.a = r1;
            }

            public lb<List<ij>> a(int i) {
                return new la(new a());
            }
        });
    }

    protected void a(final ij ijVar) {
        kg.a(3, b, "Sending next pulse report to " + ijVar.i() + " at: " + ijVar.t());
        long d = je.a().d();
        if (d == 0) {
            d = a;
        }
        long g = je.a().g();
        if (g == 0) {
            g = System.currentTimeMillis() - d;
        }
        final ik ikVar = new ik(ijVar, d, g, ijVar.r());
        lz knVar = new kn();
        knVar.a(ijVar.t());
        knVar.d(100000);
        if (ijVar.f().equals(iq.POST)) {
            knVar.a(new kx());
            if (ijVar.l() != null) {
                knVar.a(ijVar.l().getBytes());
            }
            knVar.a(kp.a.kPost);
        } else {
            knVar.a(kp.a.kGet);
        }
        knVar.a(ijVar.j() * ControllerParameters.SECOND);
        knVar.b(ijVar.k() * ControllerParameters.SECOND);
        knVar.c(true);
        knVar.a(true);
        knVar.c((ijVar.j() + ijVar.k()) * ControllerParameters.SECOND);
        Map h = ijVar.h();
        if (h != null) {
            for (String str : ijVar.h().keySet()) {
                knVar.a(str, (String) h.get(str));
            }
        }
        knVar.b(false);
        knVar.a(new kn.a<byte[], String>(this) {
            final /* synthetic */ ii c;

            public void a(kn<byte[], String> knVar, String str) {
                kg.a(3, ii.b, "Pulse report to " + ijVar.i() + " for " + ijVar.m() + ", HTTP status code is: " + knVar.h());
                int h = knVar.h();
                ikVar.a((int) knVar.c());
                ikVar.e = h;
                if (!knVar.f()) {
                    Exception j = knVar.j();
                    if (knVar.d()) {
                        if (knVar.i()) {
                            kg.a(3, ii.b, "Timeout occured when trying to connect to: " + ijVar.i() + ". Exception: " + knVar.j().getMessage());
                        } else {
                            kg.a(3, ii.b, "Manually managed http request timeout occured for: " + ijVar.i());
                        }
                        this.c.b(ikVar, ijVar);
                        return;
                    }
                    kg.a(3, ii.b, "Error occured when trying to connect to: " + ijVar.i() + ". Exception: " + j.getMessage());
                    this.c.a(ikVar, ijVar, str);
                } else if (h >= 200 && h < 300) {
                    this.c.a(ikVar, ijVar);
                } else if (h < 300 || h >= 400) {
                    kg.a(3, ii.b, ijVar.m() + " report failed sending to : " + ijVar.i());
                    this.c.a(ikVar, ijVar, str);
                } else {
                    this.c.a(ikVar, ijVar, (kn) knVar);
                }
            }
        });
        jq.a().a((Object) this, knVar);
    }

    private void a(ik ikVar, ij ijVar) {
        kg.a(3, b, ijVar.m() + " report sent successfully to : " + ijVar.i());
        im.a().a(ikVar);
        c((kr) ijVar);
    }

    private void a(ik ikVar, ij ijVar, kn<?, ?> knVar) {
        String str = null;
        List b = knVar.b("Location");
        if (b != null && b.size() > 0) {
            str = ma.b((String) b.get(0), ijVar.s());
        }
        boolean a = im.a().a(ikVar, str);
        if (a) {
            kg.a(3, b, "Received redirect url. Retrying: " + str);
        } else {
            kg.a(3, b, "Received redirect url. Retrying: false");
        }
        if (a) {
            ijVar.c(str);
            knVar.a(str);
            knVar.c("Location");
            jq.a().a((Object) this, (lz) knVar);
            return;
        }
        c((kr) ijVar);
    }

    private void a(ik ikVar, ij ijVar, String str) {
        boolean b = im.a().b(ikVar, str);
        kg.a(3, b, "Failed report retrying: " + b);
        if (b) {
            d(ijVar);
        } else {
            c((kr) ijVar);
        }
    }

    private void b(ik ikVar, ij ijVar) {
        im.a().b(ikVar);
        c((kr) ijVar);
    }

    protected synchronized void a(List<ij> list) {
        im.a().d();
    }

    protected synchronized void b(List<ij> list) {
        List<in> e = im.a().e();
        if (e != null) {
            if (e.size() != 0) {
                kg.a(3, b, "Restoring " + e.size() + " from report queue.");
                for (in b : e) {
                    im.a().b(b);
                }
                for (in d : im.a().c()) {
                    for (ij ijVar : d.d()) {
                        if (!ijVar.n()) {
                            kg.a(3, b, "Callback for " + ijVar.m() + " to " + ijVar.i() + " not completed.  Adding to reporter queue.");
                            list.add(ijVar);
                        }
                    }
                }
            }
        }
    }
}
