package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzif.zza;

@zzhb
public class zzgp extends zzgn {
    private zzgo zzGs;

    zzgp(Context context, zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp, zzgr.zza com_google_android_gms_internal_zzgr_zza) {
        super(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza);
    }

    protected void zzgb() {
        int i;
        int i2;
        AdSizeParcel zzaN = this.zzpD.zzaN();
        if (zzaN.zzui) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            i = displayMetrics.widthPixels;
            i2 = displayMetrics.heightPixels;
        } else {
            i = zzaN.widthPixels;
            i2 = zzaN.heightPixels;
        }
        this.zzGs = new zzgo(this, this.zzpD, i, i2);
        this.zzpD.zzhU().zza((zzjq.zza) this);
        this.zzGs.zza(this.zzGe);
    }

    protected int zzgc() {
        if (!this.zzGs.zzgg()) {
            return !this.zzGs.zzgh() ? 2 : -2;
        } else {
            zzb.zzaI("Ad-Network indicated no fill with passback URL.");
            return 3;
        }
    }
}
