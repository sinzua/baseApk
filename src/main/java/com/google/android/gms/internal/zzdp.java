package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzdp extends zzim {
    final zzjp zzpD;
    final zzdr zzzJ;
    private final String zzzK;

    zzdp(zzjp com_google_android_gms_internal_zzjp, zzdr com_google_android_gms_internal_zzdr, String str) {
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzzJ = com_google_android_gms_internal_zzdr;
        this.zzzK = str;
        zzr.zzbR().zza(this);
    }

    public void onStop() {
        this.zzzJ.abort();
    }

    public void zzbr() {
        try {
            this.zzzJ.zzU(this.zzzK);
        } finally {
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzdp zzzL;

                {
                    this.zzzL = r1;
                }

                public void run() {
                    zzr.zzbR().zzb(this.zzzL);
                }
            });
        }
    }
}
