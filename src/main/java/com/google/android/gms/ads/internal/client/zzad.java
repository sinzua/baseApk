package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.reward.client.zzi;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;
import com.nativex.network.volley.DefaultRetryPolicy;

@zzhb
public class zzad {
    private static final Object zzqy = new Object();
    private static zzad zzuV;
    private zzy zzuW;
    private RewardedVideoAd zzuX;

    private zzad() {
    }

    public static zzad zzdi() {
        zzad com_google_android_gms_ads_internal_client_zzad;
        synchronized (zzqy) {
            if (zzuV == null) {
                zzuV = new zzad();
            }
            com_google_android_gms_ads_internal_client_zzad = zzuV;
        }
        return com_google_android_gms_ads_internal_client_zzad;
    }

    public RewardedVideoAd getRewardedVideoAdInstance(Context context) {
        RewardedVideoAd rewardedVideoAd;
        synchronized (zzqy) {
            if (this.zzuX != null) {
                rewardedVideoAd = this.zzuX;
            } else {
                this.zzuX = new zzi(context, zzn.zzcX().zza(context, new zzew()));
                rewardedVideoAd = this.zzuX;
            }
        }
        return rewardedVideoAd;
    }

    public void initialize(Context context) {
        synchronized (zzqy) {
            if (this.zzuW != null) {
            } else if (context == null) {
                throw new IllegalArgumentException("Context cannot be null.");
            } else {
                try {
                    this.zzuW = zzn.zzcV().zzu(context);
                    this.zzuW.zza();
                } catch (RemoteException e) {
                    zzb.zzaK("Fail to initialize mobile ads setting manager");
                }
            }
        }
    }

    public void setAppVolume(float volume) {
        boolean z = true;
        boolean z2 = 0.0f <= volume && volume <= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        zzx.zzb(z2, (Object) "The app volume must be a value between 0 and 1 inclusive.");
        if (this.zzuW == null) {
            z = false;
        }
        zzx.zza(z, (Object) "MobileAds.initialize() must be called prior to setting the app volume.");
        try {
            this.zzuW.setAppVolume(volume);
        } catch (Throwable e) {
            zzb.zzb("Unable to set app volume.", e);
        }
    }

    public void zza(Context context, String str, zzae com_google_android_gms_ads_internal_client_zzae) {
        initialize(context);
    }
}
