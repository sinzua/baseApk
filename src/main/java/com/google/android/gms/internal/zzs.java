package com.google.android.gms.internal;

import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class zzs {
    public static boolean DEBUG = Log.isLoggable(TAG, 2);
    public static String TAG = "Volley";

    static class zza {
        public static final boolean zzak = zzs.DEBUG;
        private final List<zza> zzal = new ArrayList();
        private boolean zzam = false;

        private static class zza {
            public final String name;
            public final long time;
            public final long zzan;

            public zza(String str, long j, long j2) {
                this.name = str;
                this.zzan = j;
                this.time = j2;
            }
        }

        zza() {
        }

        private long zzx() {
            if (this.zzal.size() == 0) {
                return 0;
            }
            return ((zza) this.zzal.get(this.zzal.size() - 1)).time - ((zza) this.zzal.get(0)).time;
        }

        protected void finalize() throws Throwable {
            if (!this.zzam) {
                zzd("Request on the loose");
                zzs.zzc("Marker log finalized without finish() - uncaught exit point for request", new Object[0]);
            }
        }

        public synchronized void zza(String str, long j) {
            if (this.zzam) {
                throw new IllegalStateException("Marker added to finished log");
            }
            this.zzal.add(new zza(str, j, SystemClock.elapsedRealtime()));
        }

        public synchronized void zzd(String str) {
            this.zzam = true;
            if (zzx() > 0) {
                long j = ((zza) this.zzal.get(0)).time;
                zzs.zzb("(%-4d ms) %s", Long.valueOf(r2), str);
                long j2 = j;
                for (zza com_google_android_gms_internal_zzs_zza_zza : this.zzal) {
                    zzs.zzb("(+%-4d) [%2d] %s", Long.valueOf(com_google_android_gms_internal_zzs_zza_zza.time - j2), Long.valueOf(com_google_android_gms_internal_zzs_zza_zza.zzan), com_google_android_gms_internal_zzs_zza_zza.name);
                    j2 = com_google_android_gms_internal_zzs_zza_zza.time;
                }
            }
        }
    }

    public static void zza(String str, Object... objArr) {
        if (DEBUG) {
            Log.v(TAG, zzd(str, objArr));
        }
    }

    public static void zza(Throwable th, String str, Object... objArr) {
        Log.e(TAG, zzd(str, objArr), th);
    }

    public static void zzb(String str, Object... objArr) {
        Log.d(TAG, zzd(str, objArr));
    }

    public static void zzc(String str, Object... objArr) {
        Log.e(TAG, zzd(str, objArr));
    }

    private static String zzd(String str, Object... objArr) {
        String str2;
        if (objArr != null) {
            str = String.format(Locale.US, str, objArr);
        }
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        String str3 = "<unknown>";
        for (int i = 2; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClass().equals(zzs.class)) {
                str3 = stackTrace[i].getClassName();
                str3 = str3.substring(str3.lastIndexOf(46) + 1);
                str2 = str3.substring(str3.lastIndexOf(36) + 1) + "." + stackTrace[i].getMethodName();
                break;
            }
        }
        str2 = str3;
        return String.format(Locale.US, "[%d] %s: %s", new Object[]{Long.valueOf(Thread.currentThread().getId()), str2, str});
    }
}
