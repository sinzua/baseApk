package com.flurry.sdk;

import java.util.Locale;
import java.util.TimeZone;

public class ji {
    private static ji a;

    public static synchronized ji a() {
        ji jiVar;
        synchronized (ji.class) {
            if (a == null) {
                a = new ji();
            }
            jiVar = a;
        }
        return jiVar;
    }

    public static void b() {
        a = null;
    }

    private ji() {
    }

    public String c() {
        return Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    }

    public String d() {
        return TimeZone.getDefault().getID();
    }
}
