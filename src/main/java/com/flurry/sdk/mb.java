package com.flurry.sdk;

import android.content.Context;
import com.flurry.sdk.ll.a;
import java.lang.Thread.UncaughtExceptionHandler;

public class mb implements kj, a, UncaughtExceptionHandler {
    private static final String a = mb.class.getSimpleName();
    private boolean b;

    public void a(Context context) {
        ll a = lk.a();
        this.b = ((Boolean) a.a("CaptureUncaughtExceptions")).booleanValue();
        a.a("CaptureUncaughtExceptions", (a) this);
        kg.a(4, a, "initSettings, CrashReportingEnabled = " + this.b);
        mc.a().a(this);
    }

    public void b() {
        mc.b();
        lk.a().b("CaptureUncaughtExceptions", (a) this);
    }

    public void a(String str, Object obj) {
        if (str.equals("CaptureUncaughtExceptions")) {
            this.b = ((Boolean) obj).booleanValue();
            kg.a(4, a, "onSettingUpdate, CrashReportingEnabled = " + this.b);
            return;
        }
        kg.a(6, a, "onSettingUpdate internal error!");
    }

    public void uncaughtException(Thread thread, Throwable th) {
        th.printStackTrace();
        if (this.b) {
            String str = "";
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                if (th.getMessage() != null) {
                    stringBuilder.append(" (" + th.getMessage() + ")\n");
                }
                str = stringBuilder.toString();
            } else if (th.getMessage() != null) {
                str = th.getMessage();
            }
            hl.a().a("uncaught", str, th);
        }
        lh.a().g();
        jj.a().d();
    }
}
