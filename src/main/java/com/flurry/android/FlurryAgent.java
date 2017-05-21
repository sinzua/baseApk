package com.flurry.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.anjlab.android.iab.v3.Constants;
import com.flurry.sdk.hl;
import com.flurry.sdk.je;
import com.flurry.sdk.js;
import com.flurry.sdk.jt;
import com.flurry.sdk.ju;
import com.flurry.sdk.kb;
import com.flurry.sdk.kc;
import com.flurry.sdk.kg;
import com.flurry.sdk.lg;
import com.flurry.sdk.lh;
import com.flurry.sdk.lk;
import com.flurry.sdk.lt;
import com.flurry.sdk.md;
import java.util.Date;
import java.util.Map;

public final class FlurryAgent {
    private static final String a = FlurryAgent.class.getSimpleName();
    private static FlurryAgentListener b;
    private static final kb<lg> c = new kb<lg>() {
        public void a(final lg lgVar) {
            js.a().a(new Runnable(this) {
                final /* synthetic */ AnonymousClass1 b;

                public void run() {
                    switch (lgVar.c) {
                        case SESSION_ID_CREATED:
                            if (FlurryAgent.b != null) {
                                FlurryAgent.b.onSessionStarted();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            });
        }
    };

    private FlurryAgent() {
    }

    public static int getAgentVersion() {
        return jt.a();
    }

    public static String getReleaseVersion() {
        return jt.f();
    }

    public static void setFlurryAgentListener(FlurryAgentListener flurryAgentListener) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (flurryAgentListener == null) {
            kg.b(a, "Listener cannot be null");
            kc.a().b("com.flurry.android.sdk.FlurrySessionEvent", c);
        } else {
            b = flurryAgentListener;
            kc.a().a("com.flurry.android.sdk.FlurrySessionEvent", c);
        }
    }

    public static void setLogEnabled(boolean z) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (z) {
            kg.b();
        } else {
            kg.a();
        }
    }

    public static void setLogLevel(int i) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            kg.a(i);
        }
    }

    public static void setVersionName(String str) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String versionName passed to setVersionName was null.");
        } else {
            lk.a().a("VersionName", (Object) str);
        }
    }

    public static void setReportLocation(boolean z) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            lk.a().a("ReportLocation", (Object) Boolean.valueOf(z));
        }
    }

    public static void setLocation(float f, float f2) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
            return;
        }
        Location location = new Location("Explicit");
        location.setLatitude((double) f);
        location.setLongitude((double) f2);
        lk.a().a("ExplicitLocation", (Object) location);
    }

    public static void clearLocation() {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            lk.a().a("ExplicitLocation", null);
        }
    }

    public static void setContinueSessionMillis(long j) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (j < 5000) {
            kg.b(a, "Invalid time set for session resumption: " + j);
        } else {
            lk.a().a("ContinueSessionMillis", (Object) Long.valueOf(j));
        }
    }

    public static void setLogEvents(boolean z) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            lk.a().a("LogEvents", (Object) Boolean.valueOf(z));
        }
    }

    public static void setCaptureUncaughtExceptions(boolean z) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            lk.a().a("CaptureUncaughtExceptions", (Object) Boolean.valueOf(z));
        }
    }

    public static void addOrigin(String str, String str2) {
        addOrigin(str, str2, null);
    }

    public static void addOrigin(String str, String str2, Map<String, String> map) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("originName not specified");
        } else if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("originVersion not specified");
        } else {
            try {
                ju.a().a(str, str2, map);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static void setPulseEnabled(boolean z) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
            return;
        }
        lk.a().a("ProtonEnabled", (Object) Boolean.valueOf(z));
        if (!z) {
            lk.a().a("analyticsEnabled", (Object) Boolean.valueOf(true));
        }
    }

    public static synchronized void init(Context context, String str) {
        synchronized (FlurryAgent.class) {
            if (VERSION.SDK_INT < 10) {
                kg.b(a, "Device SDK Version older than 10");
            } else if (context == null) {
                throw new NullPointerException("Null context");
            } else {
                if (str != null) {
                    if (str.length() != 0) {
                        try {
                            md.a();
                            js.a(context, str);
                        } catch (Throwable th) {
                            kg.a(a, "", th);
                        }
                    }
                }
                throw new IllegalArgumentException("apiKey not specified");
            }
        }
    }

    @Deprecated
    public static void onStartSession(Context context, String str) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (context == null) {
            throw new NullPointerException("Null context");
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Api key not specified");
        } else if (js.a() == null) {
            throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
        } else {
            try {
                lh.a().b(context);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static void onStartSession(Context context) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (context == null) {
            throw new NullPointerException("Null context");
        } else if (js.a() == null) {
            throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
        } else {
            try {
                lh.a().b(context);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static void onEndSession(Context context) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (context == null) {
            throw new NullPointerException("Null context");
        } else if (js.a() == null) {
            throw new IllegalStateException("Flurry SDK must be initialized before ending a session");
        } else {
            try {
                lh.a().c(context);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static boolean isSessionActive() {
        boolean z = false;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            try {
                z = lh.a().f();
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
        return z;
    }

    public static String getSessionId() {
        String str = null;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else {
            try {
                str = je.a().c();
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
        return str;
    }

    public static FlurryEventRecordStatus logEvent(String str) {
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to logEvent was null.");
        } else {
            try {
                flurryEventRecordStatus = hl.a().a(str);
            } catch (Throwable th) {
                kg.a(a, "Failed to log event: " + str, th);
            }
        }
        return flurryEventRecordStatus;
    }

    public static FlurryEventRecordStatus logEvent(String str, Map<String, String> map) {
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to logEvent was null.");
        } else if (map == null) {
            kg.b(a, "String parameters passed to logEvent was null.");
        } else {
            try {
                flurryEventRecordStatus = hl.a().a(str, (Map) map);
            } catch (Throwable th) {
                kg.a(a, "Failed to log event: " + str, th);
            }
        }
        return flurryEventRecordStatus;
    }

    public static FlurryEventRecordStatus logEvent(FlurrySyndicationEventName flurrySyndicationEventName, String str, Map<String, String> map) {
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (flurrySyndicationEventName == null) {
            kg.b(a, "String eventName passed to logEvent was null.");
        } else if (TextUtils.isEmpty(str)) {
            kg.b(a, "String syndicationId passed to logEvent was null or empty.");
        } else if (map == null) {
            kg.b(a, "String parameters passed to logEvent was null.");
        } else {
            try {
                flurryEventRecordStatus = hl.a().a(flurrySyndicationEventName.toString(), str, (Map) map);
            } catch (Throwable th) {
                kg.a(a, "Failed to log event: " + flurrySyndicationEventName.toString(), th);
            }
        }
        return flurryEventRecordStatus;
    }

    public static FlurryEventRecordStatus logEvent(String str, boolean z) {
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to logEvent was null.");
        } else {
            try {
                flurryEventRecordStatus = hl.a().a(str, z);
            } catch (Throwable th) {
                kg.a(a, "Failed to log event: " + str, th);
            }
        }
        return flurryEventRecordStatus;
    }

    public static FlurryEventRecordStatus logEvent(String str, Map<String, String> map, boolean z) {
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to logEvent was null.");
        } else if (map == null) {
            kg.b(a, "String parameters passed to logEvent was null.");
        } else {
            try {
                flurryEventRecordStatus = hl.a().a(str, (Map) map, z);
            } catch (Throwable th) {
                kg.a(a, "Failed to log event: " + str, th);
            }
        }
        return flurryEventRecordStatus;
    }

    public static void endTimedEvent(String str) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to endTimedEvent was null.");
        } else {
            try {
                hl.a().b(str);
            } catch (Throwable th) {
                kg.a(a, "Failed to signify the end of event: " + str, th);
            }
        }
    }

    public static void endTimedEvent(String str, Map<String, String> map) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to endTimedEvent was null.");
        } else if (map == null) {
            kg.b(a, "String eventId passed to endTimedEvent was null.");
        } else {
            try {
                hl.a().b(str, map);
            } catch (Throwable th) {
                kg.a(a, "Failed to signify the end of event: " + str, th);
            }
        }
    }

    @Deprecated
    public static void onError(String str, String str2, String str3) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String errorId passed to onError was null.");
        } else if (str2 == null) {
            kg.b(a, "String message passed to onError was null.");
        } else if (str3 == null) {
            kg.b(a, "String errorClass passed to onError was null.");
        } else {
            try {
                hl.a().a(str, str2, str3);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static void onError(String str, String str2, Throwable th) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String errorId passed to onError was null.");
        } else if (str2 == null) {
            kg.b(a, "String message passed to onError was null.");
        } else if (th == null) {
            kg.b(a, "Throwable passed to onError was null.");
        } else {
            try {
                hl.a().a(str, str2, th);
            } catch (Throwable th2) {
                kg.a(a, "", th2);
            }
        }
    }

    @Deprecated
    public static void onEvent(String str) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to onEvent was null.");
        } else {
            try {
                hl.a().c(str);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    @Deprecated
    public static void onEvent(String str, Map<String, String> map) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String eventId passed to onEvent was null.");
        } else if (map == null) {
            kg.b(a, "Parameters Map passed to onEvent was null.");
        } else {
            try {
                hl.a().c(str, map);
            } catch (Throwable th) {
                kg.a(a, "", th);
            }
        }
    }

    public static void onPageView() {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
            return;
        }
        try {
            hl.a().g();
        } catch (Throwable th) {
            kg.a(a, "", th);
        }
    }

    @Deprecated
    public static void setLocationCriteria(Criteria criteria) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        }
    }

    public static void setAge(int i) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (i > 0 && i < Constants.BILLING_ERROR_OTHER_ERROR) {
            lk.a().a("Age", (Object) Long.valueOf(new Date(new Date(System.currentTimeMillis() - (((long) i) * 31449600000L)).getYear(), 1, 1).getTime()));
        }
    }

    public static void setGender(byte b) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
            return;
        }
        switch (b) {
            case (byte) 0:
            case (byte) 1:
                lk.a().a("Gender", (Object) Byte.valueOf(b));
                return;
            default:
                lk.a().a("Gender", (Object) Byte.valueOf((byte) -1));
                return;
        }
    }

    public static void setUserId(String str) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            kg.b(a, "String userId passed to setUserId was null.");
        } else {
            lk.a().a("UserId", (Object) lt.b(str));
        }
    }

    public static void setSessionOrigin(String str, String str2) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (TextUtils.isEmpty(str)) {
            kg.b(a, "String originName passed to setSessionOrigin was null or empty.");
        } else {
            je.a().a(str);
            je.a().b(str2);
        }
    }

    public static void addSessionProperty(String str, String str2) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            kg.b(a, "String name or value passed to addSessionProperty was null or empty.");
        } else {
            je.a().a(str, str2);
        }
    }
}
