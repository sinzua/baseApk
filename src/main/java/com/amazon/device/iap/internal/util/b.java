package com.amazon.device.iap.internal.util;

import android.content.SharedPreferences.Editor;
import com.amazon.device.iap.internal.d;

/* compiled from: CursorUtil */
public class b {
    private static final String a = (b.class.getName() + "_PREFS");

    public static String a(String str) {
        d.a((Object) str, "userId");
        Object b = d.d().b();
        d.a(b, "context");
        return b.getSharedPreferences(a, 0).getString(str, null);
    }

    public static void a(String str, String str2) {
        d.a((Object) str, "userId");
        Object b = d.d().b();
        d.a(b, "context");
        Editor edit = b.getSharedPreferences(a, 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }
}
