package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

public final class zzmu {
    @TargetApi(20)
    public static boolean zzaw(Context context) {
        return zzne.zzsl() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    public static boolean zzb(Resources resources) {
        if (resources == null) {
            return false;
        }
        return (zzne.zzsd() && ((resources.getConfiguration().screenLayout & 15) > 3)) || zzc(resources);
    }

    @TargetApi(13)
    private static boolean zzc(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return zzne.zzsf() && (configuration.screenLayout & 15) <= 3 && configuration.smallestScreenWidthDp >= 600;
    }
}
