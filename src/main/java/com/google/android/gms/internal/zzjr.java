package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzjr {
    public zzjp zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan com_google_android_gms_internal_zzan, VersionInfoParcel versionInfoParcel) {
        return zza(context, adSizeParcel, z, z2, com_google_android_gms_internal_zzan, versionInfoParcel, null, null);
    }

    public zzjp zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan com_google_android_gms_internal_zzan, VersionInfoParcel versionInfoParcel, zzcb com_google_android_gms_internal_zzcb, zzd com_google_android_gms_ads_internal_zzd) {
        zzjp com_google_android_gms_internal_zzjs = new zzjs(zzjt.zzb(context, adSizeParcel, z, z2, com_google_android_gms_internal_zzan, versionInfoParcel, com_google_android_gms_internal_zzcb, com_google_android_gms_ads_internal_zzd));
        com_google_android_gms_internal_zzjs.setWebViewClient(zzr.zzbE().zzb(com_google_android_gms_internal_zzjs, z2));
        com_google_android_gms_internal_zzjs.setWebChromeClient(zzr.zzbE().zzk(com_google_android_gms_internal_zzjs));
        return com_google_android_gms_internal_zzjs;
    }
}
