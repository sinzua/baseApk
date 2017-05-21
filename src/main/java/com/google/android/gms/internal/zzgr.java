package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;

@zzhb
public class zzgr {

    public interface zza {
        void zzb(zzif com_google_android_gms_internal_zzif);
    }

    public zzit zza(Context context, com.google.android.gms.ads.internal.zza com_google_android_gms_ads_internal_zza, com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza, zzan com_google_android_gms_internal_zzan, zzjp com_google_android_gms_internal_zzjp, zzex com_google_android_gms_internal_zzex, zza com_google_android_gms_internal_zzgr_zza, zzcb com_google_android_gms_internal_zzcb) {
        zzit com_google_android_gms_internal_zzgu;
        AdResponseParcel adResponseParcel = com_google_android_gms_internal_zzif_zza.zzLe;
        if (adResponseParcel.zzHT) {
            com_google_android_gms_internal_zzgu = new zzgu(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzex, com_google_android_gms_internal_zzgr_zza, com_google_android_gms_internal_zzcb, com_google_android_gms_internal_zzjp);
        } else if (!adResponseParcel.zzuk) {
            com_google_android_gms_internal_zzgu = adResponseParcel.zzHZ ? new zzgp(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza) : (((Boolean) zzbt.zzwu.get()).booleanValue() && zzne.zzsk() && !zzne.isAtLeastL() && com_google_android_gms_internal_zzjp.zzaN().zzui) ? new zzgt(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza) : new zzgs(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza);
        } else if (com_google_android_gms_ads_internal_zza instanceof zzp) {
            com_google_android_gms_internal_zzgu = new zzgv(context, (zzp) com_google_android_gms_ads_internal_zza, new zzee(), com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzan, com_google_android_gms_internal_zzgr_zza);
        } else {
            throw new IllegalArgumentException("Invalid NativeAdManager type. Found: " + (com_google_android_gms_ads_internal_zza != null ? com_google_android_gms_ads_internal_zza.getClass().getName() : "null") + "; Required: NativeAdManager.");
        }
        zzb.zzaI("AdRenderer: " + com_google_android_gms_internal_zzgu.getClass().getName());
        com_google_android_gms_internal_zzgu.zzgd();
        return com_google_android_gms_internal_zzgu;
    }

    public zzit zza(Context context, String str, com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza, zzht com_google_android_gms_internal_zzht) {
        zzit com_google_android_gms_internal_zzhz = new zzhz(context, str, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzht);
        zzb.zzaI("AdRenderer: " + com_google_android_gms_internal_zzhz.getClass().getName());
        com_google_android_gms_internal_zzhz.zzgd();
        return com_google_android_gms_internal_zzhz;
    }
}
