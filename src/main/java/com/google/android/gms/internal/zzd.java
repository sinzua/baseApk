package com.google.android.gms.internal;

import com.nativex.network.volley.DefaultRetryPolicy;

public class zzd implements zzo {
    private int zzo;
    private int zzp;
    private final int zzq;
    private final float zzr;

    public zzd() {
        this(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public zzd(int i, int i2, float f) {
        this.zzo = i;
        this.zzq = i2;
        this.zzr = f;
    }

    public void zza(zzr com_google_android_gms_internal_zzr) throws zzr {
        this.zzp++;
        this.zzo = (int) (((float) this.zzo) + (((float) this.zzo) * this.zzr));
        if (!zzf()) {
            throw com_google_android_gms_internal_zzr;
        }
    }

    public int zzd() {
        return this.zzo;
    }

    public int zze() {
        return this.zzp;
    }

    protected boolean zzf() {
        return this.zzp <= this.zzq;
    }
}
