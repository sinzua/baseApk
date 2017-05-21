package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.internal.zzcs.zza;

@zzhb
public class zzcx extends zza {
    private final OnContentAdLoadedListener zzyT;

    public zzcx(OnContentAdLoadedListener onContentAdLoadedListener) {
        this.zzyT = onContentAdLoadedListener;
    }

    public void zza(zzcn com_google_android_gms_internal_zzcn) {
        this.zzyT.onContentAdLoaded(zzb(com_google_android_gms_internal_zzcn));
    }

    zzco zzb(zzcn com_google_android_gms_internal_zzcn) {
        return new zzco(com_google_android_gms_internal_zzcn);
    }
}
