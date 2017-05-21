package com.flurry.sdk;

import android.content.Context;
import com.flurry.sdk.jl.a;
import java.util.Map;

public class je implements kj {
    private static final String a = je.class.getSimpleName();

    public static synchronized je a() {
        je jeVar;
        synchronized (je.class) {
            jeVar = (je) js.a().a(je.class);
        }
        return jeVar;
    }

    public void a(Context context) {
        lf.a(jr.class);
        kc.a();
        lo.a();
        lk.a();
        ju.a();
        jl.a();
        jf.a();
        jm.a();
        jj.a();
        jf.a();
        jo.a();
        ji.a();
        jq.a();
    }

    public void b() {
        jq.b();
        ji.b();
        jo.b();
        jf.b();
        jj.b();
        jm.b();
        jf.b();
        jl.b();
        ju.b();
        lk.b();
        lo.b();
        kc.b();
        lf.b(jr.class);
    }

    public String c() {
        jr m = m();
        if (m != null) {
            return m.b();
        }
        return null;
    }

    public long d() {
        jr m = m();
        if (m != null) {
            return m.c();
        }
        return 0;
    }

    public long e() {
        jr m = m();
        if (m != null) {
            return m.d();
        }
        return 0;
    }

    public long f() {
        jr m = m();
        if (m != null) {
            return m.e();
        }
        return -1;
    }

    public long g() {
        jr m = m();
        if (m != null) {
            return m.g();
        }
        return 0;
    }

    public long h() {
        jr m = m();
        if (m != null) {
            return m.f();
        }
        return 0;
    }

    public void a(String str) {
        jr m = m();
        if (m != null) {
            m.a(str);
        }
    }

    public String i() {
        jr m = m();
        if (m != null) {
            return m.h();
        }
        return null;
    }

    public void b(String str) {
        jr m = m();
        if (m != null) {
            m.b(str);
        }
    }

    public String j() {
        jr m = m();
        if (m != null) {
            return m.i();
        }
        return null;
    }

    public void a(String str, String str2) {
        jr m = m();
        if (m != null) {
            m.a(str, str2);
        }
    }

    public Map<String, String> k() {
        jr m = m();
        if (m != null) {
            return m.j();
        }
        return null;
    }

    public a l() {
        return jl.a().d();
    }

    private jr m() {
        return a(lh.a().e());
    }

    private jr a(lf lfVar) {
        if (lfVar == null) {
            return null;
        }
        return (jr) lfVar.c(jr.class);
    }
}
