package com.flurry.sdk;

import android.content.Context;
import com.flurry.android.FlurryEventRecordStatus;
import java.util.Map;

public class hl implements kj {
    private static final String a = hl.class.getSimpleName();
    private ig b;
    private iy c;
    private ii d;

    public static synchronized hl a() {
        hl hlVar;
        synchronized (hl.class) {
            hlVar = (hl) js.a().a(hl.class);
        }
        return hlVar;
    }

    public void a(Context context) {
        lf.a(jb.class);
        this.c = new iy();
        this.b = new ig();
        this.d = new ii();
        b(context);
    }

    private void b(Context context) {
        if (!lt.a(context, "android.permission.INTERNET")) {
            kg.b(a, "Application must declare permission: android.permission.INTERNET");
        }
        if (!lt.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
            kg.e(a, "It is highly recommended that the application declare permission: android.permission.ACCESS_NETWORK_STATE");
        }
    }

    public void b() {
        if (this.d != null) {
            this.d.c();
            this.d = null;
        }
        if (this.c != null) {
            this.c.a();
            this.c = null;
        }
        if (this.b != null) {
            this.b.a();
            this.b = null;
        }
        lf.b(jb.class);
    }

    public ig c() {
        return this.b;
    }

    public iy d() {
        return this.c;
    }

    public ii e() {
        return this.d;
    }

    public void f() {
        jb h = h();
        if (h != null) {
            h.a();
        }
    }

    public FlurryEventRecordStatus a(String str) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, null, false);
        }
        return flurryEventRecordStatus;
    }

    public FlurryEventRecordStatus a(String str, Map<String, String> map) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, (Map) map, false);
        }
        return flurryEventRecordStatus;
    }

    public FlurryEventRecordStatus a(String str, String str2, Map<String, String> map) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, str2, (Map) map, false);
        }
        return flurryEventRecordStatus;
    }

    public FlurryEventRecordStatus b(String str, String str2, Map<String, String> map) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, jd.a(str2), (Map) map, false);
        }
        return flurryEventRecordStatus;
    }

    public FlurryEventRecordStatus a(String str, boolean z) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, null, z);
        }
        return flurryEventRecordStatus;
    }

    public FlurryEventRecordStatus a(String str, Map<String, String> map, boolean z) {
        jb h = h();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (h != null) {
            return h.a(str, (Map) map, z);
        }
        return flurryEventRecordStatus;
    }

    public void b(String str) {
        jb h = h();
        if (h != null) {
            h.a(str, null);
        }
    }

    public void b(String str, Map<String, String> map) {
        jb h = h();
        if (h != null) {
            h.a(str, (Map) map);
        }
    }

    @Deprecated
    public void a(String str, String str2, String str3) {
        StackTraceElement[] stackTraceElementArr;
        Object stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length <= 2) {
            Object obj = stackTrace;
        } else {
            stackTraceElementArr = new StackTraceElement[(stackTrace.length - 2)];
            System.arraycopy(stackTrace, 2, stackTraceElementArr, 0, stackTraceElementArr.length);
        }
        Throwable th = new Throwable(str2);
        th.setStackTrace(stackTraceElementArr);
        jb h = h();
        if (h != null) {
            h.a(str, str2, str3, th);
        }
    }

    public void a(String str, String str2, Throwable th) {
        jb h = h();
        if (h != null) {
            h.a(str, str2, th.getClass().getName(), th);
        }
    }

    public void c(String str) {
        jb h = h();
        if (h != null) {
            h.a(str, null, false);
        }
    }

    public void c(String str, Map<String, String> map) {
        jb h = h();
        if (h != null) {
            h.a(str, (Map) map, false);
        }
    }

    public void g() {
        jb h = h();
        if (h != null) {
            h.b();
        }
    }

    private jb h() {
        return a(lh.a().e());
    }

    private jb a(lf lfVar) {
        if (lfVar == null) {
            return null;
        }
        return (jb) lfVar.c(jb.class);
    }
}
