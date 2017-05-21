package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.stats.zzc.zzb;
import com.google.android.gms.internal.zzmp;
import com.google.android.gms.internal.zzmv;
import java.util.List;

public class zzi {
    private static String TAG = "WakeLockTracker";
    private static zzi zzanY = new zzi();
    private static Integer zzanv;

    private static int getLogLevel() {
        try {
            return zzmp.zzkr() ? ((Integer) zzb.zzanz.get()).intValue() : zzd.LOG_LEVEL_OFF;
        } catch (SecurityException e) {
            return zzd.LOG_LEVEL_OFF;
        }
    }

    private static boolean zzav(Context context) {
        if (zzanv == null) {
            zzanv = Integer.valueOf(getLogLevel());
        }
        return zzanv.intValue() != zzd.LOG_LEVEL_OFF;
    }

    public static zzi zzrZ() {
        return zzanY;
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list) {
        zza(context, str, i, str2, str3, i2, list, 0);
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list, long j) {
        if (!zzav(context)) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "missing wakeLock key. " + str);
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (7 == i || 8 == i || 10 == i || 11 == i) {
            try {
                context.startService(new Intent().setComponent(zzd.zzanF).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", new WakeLockEvent(currentTimeMillis, i, str2, i2, list, str, SystemClock.elapsedRealtime(), zzmv.zzax(context), str3, context.getPackageName(), zzmv.zzay(context), j)));
            } catch (Throwable e) {
                Log.wtf(TAG, e);
            }
        }
    }
}
