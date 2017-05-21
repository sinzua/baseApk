package com.google.android.gms.internal;

import android.os.SystemClock;

public final class zzmt implements zzmq {
    private static zzmt zzaoa;

    public static synchronized zzmq zzsc() {
        zzmq com_google_android_gms_internal_zzmq;
        synchronized (zzmt.class) {
            if (zzaoa == null) {
                zzaoa = new zzmt();
            }
            com_google_android_gms_internal_zzmq = zzaoa;
        }
        return com_google_android_gms_internal_zzmq;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public long nanoTime() {
        return System.nanoTime();
    }
}
