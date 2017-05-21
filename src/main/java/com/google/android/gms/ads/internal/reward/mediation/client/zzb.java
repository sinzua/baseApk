package com.google.android.gms.ads.internal.reward.mediation.client;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzb implements MediationRewardedVideoAdListener {
    private final zza zzKR;

    public zzb(zza com_google_android_gms_ads_internal_reward_mediation_client_zza) {
        this.zzKR = com_google_android_gms_ads_internal_reward_mediation_client_zza;
    }

    public void onAdClicked(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onAdClicked must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdClicked.");
        try {
            this.zzKR.zzl(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClicked.", e);
        }
    }

    public void onAdClosed(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onAdClosed must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdClosed.");
        try {
            this.zzKR.zzk(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClosed.", e);
        }
    }

    public void onAdFailedToLoad(MediationRewardedVideoAdAdapter adapter, int errorCode) {
        zzx.zzcD("onAdFailedToLoad must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdFailedToLoad.");
        try {
            this.zzKR.zzc(zze.zzC(adapter), errorCode);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdFailedToLoad.", e);
        }
    }

    public void onAdLeftApplication(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onAdLeftApplication must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdLeftApplication.");
        try {
            this.zzKR.zzm(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLeftApplication.", e);
        }
    }

    public void onAdLoaded(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onAdLoaded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdLoaded.");
        try {
            this.zzKR.zzh(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLoaded.", e);
        }
    }

    public void onAdOpened(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onAdOpened must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onAdOpened.");
        try {
            this.zzKR.zzi(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdOpened.", e);
        }
    }

    public void onInitializationFailed(MediationRewardedVideoAdAdapter adapter, int errorCode) {
        zzx.zzcD("onInitializationFailed must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onInitializationFailed.");
        try {
            this.zzKR.zzb(zze.zzC(adapter), errorCode);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onInitializationFailed.", e);
        }
    }

    public void onInitializationSucceeded(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onInitializationSucceeded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onInitializationSucceeded.");
        try {
            this.zzKR.zzg(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onInitializationSucceeded.", e);
        }
    }

    public void onRewarded(MediationRewardedVideoAdAdapter adapter, RewardItem rewardItem) {
        zzx.zzcD("onRewarded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onRewarded.");
        if (rewardItem != null) {
            try {
                this.zzKR.zza(zze.zzC(adapter), new RewardItemParcel(rewardItem));
                return;
            } catch (Throwable e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onRewarded.", e);
                return;
            }
        }
        this.zzKR.zza(zze.zzC(adapter), new RewardItemParcel(adapter.getClass().getName(), 1));
    }

    public void onVideoStarted(MediationRewardedVideoAdAdapter adapter) {
        zzx.zzcD("onVideoStarted must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Adapter called onVideoStarted.");
        try {
            this.zzKR.zzj(zze.zzC(adapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onVideoStarted.", e);
        }
    }
}
