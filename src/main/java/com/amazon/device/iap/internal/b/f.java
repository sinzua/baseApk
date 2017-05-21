package com.amazon.device.iap.internal.b;

import com.amazon.android.framework.util.KiwiLogger;
import com.amazon.device.iap.internal.a;

/* compiled from: KiwiLogHandler */
public class f implements a {
    private static KiwiLogger a = new KiwiLogger("In App Purchasing SDK - Production Mode");

    public boolean a() {
        return KiwiLogger.TRACE_ON;
    }

    public boolean b() {
        return KiwiLogger.ERROR_ON;
    }

    public void a(String str, String str2) {
        a.trace(c(str, str2));
    }

    public void b(String str, String str2) {
        a.error(c(str, str2));
    }

    private static String c(String str, String str2) {
        return str + ": " + str2;
    }
}
