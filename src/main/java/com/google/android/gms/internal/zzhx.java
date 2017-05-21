package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.zza.zza;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;

@zzhb
public class zzhx extends zza {
    private zzhy zzKC;
    private zzhv zzKJ;
    private zzhw zzKK;

    public zzhx(zzhw com_google_android_gms_internal_zzhw) {
        this.zzKK = com_google_android_gms_internal_zzhw;
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, RewardItemParcel rewardItemParcel) {
        if (this.zzKK != null) {
            this.zzKK.zzc(rewardItemParcel);
        }
    }

    public void zza(zzhv com_google_android_gms_internal_zzhv) {
        this.zzKJ = com_google_android_gms_internal_zzhv;
    }

    public void zza(zzhy com_google_android_gms_internal_zzhy) {
        this.zzKC = com_google_android_gms_internal_zzhy;
    }

    public void zzb(zzd com_google_android_gms_dynamic_zzd, int i) {
        if (this.zzKJ != null) {
            this.zzKJ.zzN(i);
        }
    }

    public void zzc(zzd com_google_android_gms_dynamic_zzd, int i) {
        if (this.zzKC != null) {
            this.zzKC.zza(zze.zzp(com_google_android_gms_dynamic_zzd).getClass().getName(), i);
        }
    }

    public void zzg(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKJ != null) {
            this.zzKJ.zzgN();
        }
    }

    public void zzh(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKC != null) {
            this.zzKC.zzax(zze.zzp(com_google_android_gms_dynamic_zzd).getClass().getName());
        }
    }

    public void zzi(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdOpened();
        }
    }

    public void zzj(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoStarted();
        }
    }

    public void zzk(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdClosed();
        }
    }

    public void zzl(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKK != null) {
            this.zzKK.zzgM();
        }
    }

    public void zzm(zzd com_google_android_gms_dynamic_zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdLeftApplication();
        }
    }
}
