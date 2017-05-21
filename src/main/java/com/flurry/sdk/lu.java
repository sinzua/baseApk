package com.flurry.sdk;

import android.content.Context;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public class lu {
    private static final String a = lu.class.getSimpleName();
    private static String b = "com.google.android.gms.common.GooglePlayServicesUtil";
    private static String c = "com.google.android.gms.ads.identifier.AdvertisingIdClient";

    public static boolean a(Context context) {
        try {
            Object a = lw.a(null, "isGooglePlayServicesAvailable").a(Class.forName(b)).a(Context.class, context).a();
            return a != null && ((Integer) a).intValue() == 0;
        } catch (Exception e) {
            kg.b(a, "GOOGLE PLAY SERVICES EXCEPTION: " + e.getMessage());
            kg.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return false;
        }
    }

    public static jp b(Context context) {
        if (context == null) {
            return null;
        }
        try {
            Object a = lw.a(null, "getAdvertisingIdInfo").a(Class.forName(c)).a(Context.class, context).a();
            return new jp(a(a, null), a(a, false));
        } catch (Exception e) {
            kg.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            kg.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return null;
        }
    }

    static String a(Object obj, String str) {
        try {
            return (String) lw.a(obj, "getId").a();
        } catch (Exception e) {
            kg.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            kg.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return str;
        }
    }

    static boolean a(Object obj, boolean z) {
        try {
            Boolean bool = (Boolean) lw.a(obj, RequestParameters.isLAT).a();
            if (bool != null) {
                z = bool.booleanValue();
            }
        } catch (Exception e) {
            kg.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            kg.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
        }
        return z;
    }
}
