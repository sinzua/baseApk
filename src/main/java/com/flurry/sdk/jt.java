package com.flurry.sdk;

import java.util.Locale;

public class jt {
    private static final String a = jt.class.getSimpleName();

    public static int a() {
        int intValue = ((Integer) lk.a().a("AgentVersion")).intValue();
        kg.a(4, a, "getAgentVersion() = " + intValue);
        return intValue;
    }

    public static int b() {
        return ((Integer) lk.a().a("ReleaseMajorVersion")).intValue();
    }

    public static int c() {
        return ((Integer) lk.a().a("ReleaseMinorVersion")).intValue();
    }

    public static int d() {
        return ((Integer) lk.a().a("ReleasePatchVersion")).intValue();
    }

    public static String e() {
        return (String) lk.a().a("ReleaseBetaVersion");
    }

    public static String f() {
        String str;
        if (e().length() > 0) {
            str = ".";
        } else {
            str = "";
        }
        return String.format(Locale.getDefault(), "Flurry_Android_%d_%d.%d.%d%s%s", new Object[]{Integer.valueOf(a()), Integer.valueOf(b()), Integer.valueOf(c()), Integer.valueOf(d()), str, e()});
    }
}
