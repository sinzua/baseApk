package com.amazon.device.iap.internal.c;

import android.content.SharedPreferences.Editor;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.internal.util.e;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

/* compiled from: EntitlementTracker */
public class c {
    private static c a = new c();
    private static final String b = c.class.getSimpleName();
    private static final String c = (c.class.getName() + "_PREFS_");

    public static c a() {
        return a;
    }

    public void a(String str, String str2, String str3) {
        e.a(b, "enter saveEntitlementRecord for v1 Entitlement [" + str2 + "/" + str3 + "], user [" + str + RequestParameters.RIGHT_BRACKETS);
        try {
            d.a(str, "userId");
            d.a(str2, "receiptId");
            d.a(str3, "sku");
            Object b = com.amazon.device.iap.internal.d.d().b();
            d.a(b, "context");
            Editor edit = b.getSharedPreferences(c + str, 0).edit();
            edit.putString(str3, str2);
            edit.commit();
        } catch (Throwable th) {
            e.a(b, "error in saving v1 Entitlement:" + str2 + "/" + str3 + ":" + th.getMessage());
        }
        e.a(b, "leaving saveEntitlementRecord for v1 Entitlement [" + str2 + "/" + str3 + "], user [" + str + RequestParameters.RIGHT_BRACKETS);
    }

    public String a(String str, String str2) {
        String str3 = null;
        e.a(b, "enter getReceiptIdFromSku for sku [" + str2 + "], user [" + str + RequestParameters.RIGHT_BRACKETS);
        try {
            d.a(str, "userId");
            d.a(str2, "sku");
            Object b = com.amazon.device.iap.internal.d.d().b();
            d.a(b, "context");
            str3 = b.getSharedPreferences(c + str, 0).getString(str2, null);
        } catch (Throwable th) {
            e.a(b, "error in saving v1 Entitlement:" + str2 + ":" + th.getMessage());
        }
        e.a(b, "leaving saveEntitlementRecord for sku [" + str2 + "], user [" + str + RequestParameters.RIGHT_BRACKETS);
        return str3;
    }
}
