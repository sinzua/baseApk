package com.google.android.gms.ads.internal.util.client;

import android.util.Log;
import com.google.ads.AdRequest;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzb {
    public static void e(String msg) {
        if (zzQ(6)) {
            Log.e(AdRequest.LOGTAG, msg);
        }
    }

    public static boolean zzQ(int i) {
        return i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i);
    }

    public static void zza(String str, Throwable th) {
        if (zzQ(3)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzaI(String str) {
        if (zzQ(3)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaJ(String str) {
        if (zzQ(4)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaK(String str) {
        if (zzQ(5)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }

    public static void zzb(String str, Throwable th) {
        if (zzQ(6)) {
            Log.e(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzc(String str, Throwable th) {
        if (zzQ(4)) {
            Log.i(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzd(String str, Throwable th) {
        if (zzQ(5)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }
}
