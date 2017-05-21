package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zziz {
    private long zzMJ;
    private long zzMK = Long.MIN_VALUE;
    private Object zzpV = new Object();

    public zziz(long j) {
        this.zzMJ = j;
    }

    public boolean tryAcquire() {
        boolean z;
        synchronized (this.zzpV) {
            long elapsedRealtime = zzr.zzbG().elapsedRealtime();
            if (this.zzMK + this.zzMJ > elapsedRealtime) {
                z = false;
            } else {
                this.zzMK = elapsedRealtime;
                z = true;
            }
        }
        return z;
    }
}
