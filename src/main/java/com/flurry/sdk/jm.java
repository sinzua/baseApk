package com.flurry.sdk;

import android.telephony.TelephonyManager;

public class jm {
    private static jm a;
    private static final String b = jm.class.getSimpleName();

    public static synchronized jm a() {
        jm jmVar;
        synchronized (jm.class) {
            if (a == null) {
                a = new jm();
            }
            jmVar = a;
        }
        return jmVar;
    }

    public static void b() {
        a = null;
    }

    private jm() {
    }

    public String c() {
        TelephonyManager telephonyManager = (TelephonyManager) js.a().c().getSystemService("phone");
        if (telephonyManager == null) {
            return null;
        }
        return telephonyManager.getNetworkOperatorName();
    }

    public String d() {
        TelephonyManager telephonyManager = (TelephonyManager) js.a().c().getSystemService("phone");
        if (telephonyManager == null) {
            return null;
        }
        return telephonyManager.getNetworkOperator();
    }
}
