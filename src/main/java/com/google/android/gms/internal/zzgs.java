package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzjq.zza;
import com.supersonicads.sdk.precache.DownloadManager;

@zzhb
public class zzgs extends zzgn implements zza {
    zzgs(Context context, zzif.zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp, zzgr.zza com_google_android_gms_internal_zzgr_zza) {
        super(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza);
    }

    protected void zzgb() {
        if (this.zzGe.errorCode == -2) {
            this.zzpD.zzhU().zza((zza) this);
            zzgi();
            zzb.zzaI("Loading HTML in WebView.");
            this.zzpD.loadDataWithBaseURL(zzr.zzbC().zzaC(this.zzGe.zzEF), this.zzGe.body, "text/html", DownloadManager.UTF8_CHARSET, null);
        }
    }

    protected void zzgi() {
    }
}
