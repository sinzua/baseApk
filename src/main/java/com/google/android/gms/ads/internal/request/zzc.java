package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;

@zzhb
public final class zzc {

    public interface zza {
        void zzb(AdResponseParcel adResponseParcel);
    }

    interface zzb {
        boolean zza(VersionInfoParcel versionInfoParcel);
    }

    public static zzit zza(final Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, zza com_google_android_gms_ads_internal_request_zzc_zza) {
        return zza(context, versionInfoParcel, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza, new zzb() {
            public boolean zza(VersionInfoParcel versionInfoParcel) {
                return versionInfoParcel.zzNb || (zze.zzap(context) && !((Boolean) zzbt.zzwb.get()).booleanValue());
            }
        });
    }

    static zzit zza(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, zza com_google_android_gms_ads_internal_request_zzc_zza, zzb com_google_android_gms_ads_internal_request_zzc_zzb) {
        return com_google_android_gms_ads_internal_request_zzc_zzb.zza(versionInfoParcel) ? zza(context, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza) : zzb(context, versionInfoParcel, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza);
    }

    private static zzit zza(Context context, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, zza com_google_android_gms_ads_internal_request_zzc_zza) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Fetching ad response from local ad request service.");
        zzit com_google_android_gms_ads_internal_request_zzd_zza = new com.google.android.gms.ads.internal.request.zzd.zza(context, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza);
        com_google_android_gms_ads_internal_request_zzd_zza.zzga();
        return com_google_android_gms_ads_internal_request_zzd_zza;
    }

    private static zzit zzb(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, zza com_google_android_gms_ads_internal_request_zzc_zza) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Fetching ad response from remote ad request service.");
        if (zzn.zzcS().zzU(context)) {
            return new com.google.android.gms.ads.internal.request.zzd.zzb(context, versionInfoParcel, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK("Failed to connect to remote ad request service.");
        return null;
    }
}
