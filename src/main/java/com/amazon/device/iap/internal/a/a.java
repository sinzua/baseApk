package com.amazon.device.iap.internal.a;

import android.util.Log;

/* compiled from: SandboxLogHandler */
public class a implements com.amazon.device.iap.internal.a {
    public boolean a() {
        return true;
    }

    public boolean b() {
        return true;
    }

    public void a(String str, String str2) {
        Log.d(str, a(str2));
    }

    public void b(String str, String str2) {
        Log.e(str, a(str2));
    }

    private static String a(String str) {
        return "In App Purchasing SDK - Sandbox Mode: " + str;
    }
}
