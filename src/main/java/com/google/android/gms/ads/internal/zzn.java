package com.google.android.gms.ads.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.zzy.zza;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzn extends zza {
    private static final Object zzqy = new Object();
    private static zzn zzqz;
    private final Context mContext;
    private final Object zzpV = new Object();
    private boolean zzqA;
    private float zzqB = -1.0f;

    zzn(Context context) {
        this.mContext = context;
        this.zzqA = false;
    }

    public static zzn zzbs() {
        zzn com_google_android_gms_ads_internal_zzn;
        synchronized (zzqy) {
            com_google_android_gms_ads_internal_zzn = zzqz;
        }
        return com_google_android_gms_ads_internal_zzn;
    }

    public static zzn zzr(Context context) {
        zzn com_google_android_gms_ads_internal_zzn;
        synchronized (zzqy) {
            if (zzqz == null) {
                zzqz = new zzn(context.getApplicationContext());
            }
            com_google_android_gms_ads_internal_zzn = zzqz;
        }
        return com_google_android_gms_ads_internal_zzn;
    }

    public void setAppVolume(float volume) {
        synchronized (this.zzpV) {
            this.zzqB = volume;
        }
    }

    public void zza() {
        synchronized (zzqy) {
            if (this.zzqA) {
                zzb.zzaK("Mobile ads is initialized already.");
                return;
            }
            this.zzqA = true;
        }
    }

    public float zzbt() {
        float f;
        synchronized (this.zzpV) {
            f = this.zzqB;
        }
        return f;
    }

    public boolean zzbu() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzqB >= 0.0f;
        }
        return z;
    }
}
