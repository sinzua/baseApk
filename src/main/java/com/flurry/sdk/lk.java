package com.flurry.sdk;

import android.location.Criteria;
import android.location.Location;
import com.ty.followboom.helpers.VLTools;

public class lk extends ll {
    public static final Integer a = Integer.valueOf(216);
    public static final Integer b = Integer.valueOf(6);
    public static final Integer c = Integer.valueOf(2);
    public static final Integer d = Integer.valueOf(0);
    public static final String e = null;
    public static final Boolean f = Boolean.valueOf(true);
    public static final Boolean g = Boolean.valueOf(true);
    public static final String h = null;
    public static final Boolean i = Boolean.valueOf(true);
    public static final Criteria j = null;
    public static final Location k = null;
    public static final Long l = Long.valueOf(VLTools.DEFAULT_RATE_US_THREHOLD);
    public static final Boolean m = Boolean.valueOf(true);
    public static final Long n = null;
    public static final Byte o = Byte.valueOf((byte) -1);
    public static final Boolean p = Boolean.valueOf(false);
    public static final String q = null;
    public static final Boolean r = Boolean.valueOf(true);
    private static lk s;

    public static synchronized lk a() {
        lk lkVar;
        synchronized (lk.class) {
            if (s == null) {
                s = new lk();
            }
            lkVar = s;
        }
        return lkVar;
    }

    public static synchronized void b() {
        synchronized (lk.class) {
            if (s != null) {
                s.d();
            }
            s = null;
        }
    }

    private lk() {
        c();
    }

    public void c() {
        a("AgentVersion", (Object) a);
        a("ReleaseMajorVersion", (Object) b);
        a("ReleaseMinorVersion", (Object) c);
        a("ReleasePatchVersion", (Object) d);
        a("ReleaseBetaVersion", (Object) "");
        a("VersionName", (Object) e);
        a("CaptureUncaughtExceptions", (Object) f);
        a("UseHttps", (Object) g);
        a("ReportUrl", (Object) h);
        a("ReportLocation", (Object) i);
        a("ExplicitLocation", (Object) k);
        a("ContinueSessionMillis", (Object) l);
        a("LogEvents", (Object) m);
        a("Age", (Object) n);
        a("Gender", (Object) o);
        a("UserId", (Object) "");
        a("ProtonEnabled", (Object) p);
        a("ProtonConfigUrl", (Object) q);
        a("analyticsEnabled", (Object) r);
    }
}
