package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.internal.zzcr.zza;

@zzhb
public class zzcw extends zza {
    private final OnAppInstallAdLoadedListener zzyS;

    public zzcw(OnAppInstallAdLoadedListener onAppInstallAdLoadedListener) {
        this.zzyS = onAppInstallAdLoadedListener;
    }

    public void zza(zzcl com_google_android_gms_internal_zzcl) {
        this.zzyS.onAppInstallAdLoaded(zzb(com_google_android_gms_internal_zzcl));
    }

    zzcm zzb(zzcl com_google_android_gms_internal_zzcl) {
        return new zzcm(com_google_android_gms_internal_zzcl);
    }
}
