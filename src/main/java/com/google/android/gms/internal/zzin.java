package com.google.android.gms.internal;

import android.util.Log;
import com.google.ads.AdRequest;
import com.google.android.gms.ads.internal.util.client.zzb;

@zzhb
public final class zzin extends zzb {
    public static void v(String msg) {
        if (zzhp()) {
            Log.v(AdRequest.LOGTAG, msg);
        }
    }

    public static boolean zzho() {
        return ((Boolean) zzbt.zzwK.get()).booleanValue();
    }

    private static boolean zzhp() {
        return zzb.zzQ(2) && zzho();
    }
}
