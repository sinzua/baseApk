package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzik {
    private int zzLJ;
    private int zzLK;
    private final String zzLh;
    private final Object zzpV;
    private final zzih zzqV;

    zzik(zzih com_google_android_gms_internal_zzih, String str) {
        this.zzpV = new Object();
        this.zzqV = com_google_android_gms_internal_zzih;
        this.zzLh = str;
    }

    public zzik(String str) {
        this(zzr.zzbF(), str);
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.zzpV) {
            bundle = new Bundle();
            bundle.putInt("pmnli", this.zzLJ);
            bundle.putInt("pmnll", this.zzLK);
        }
        return bundle;
    }

    public void zzg(int i, int i2) {
        synchronized (this.zzpV) {
            this.zzLJ = i;
            this.zzLK = i2;
            this.zzqV.zza(this.zzLh, this);
        }
    }
}
