package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.client.zzb.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzd;

@zzhb
public class zzhs extends zza {
    private final Context mContext;
    private final zzht zzKu;
    private final VersionInfoParcel zzpT;
    private final Object zzpV = new Object();

    public zzhs(Context context, zzd com_google_android_gms_ads_internal_zzd, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzpT = versionInfoParcel;
        this.zzKu = new zzht(context, com_google_android_gms_ads_internal_zzd, AdSizeParcel.zzcP(), com_google_android_gms_internal_zzex, versionInfoParcel);
    }

    public void destroy() {
        synchronized (this.zzpV) {
            this.zzKu.destroy();
        }
    }

    public boolean isLoaded() {
        boolean isLoaded;
        synchronized (this.zzpV) {
            isLoaded = this.zzKu.isLoaded();
        }
        return isLoaded;
    }

    public void pause() {
        synchronized (this.zzpV) {
            this.zzKu.pause();
        }
    }

    public void resume() {
        synchronized (this.zzpV) {
            this.zzKu.resume();
        }
    }

    public void setUserId(String userId) {
        synchronized (this.zzpV) {
            this.zzKu.setUserId(userId);
        }
    }

    public void show() {
        synchronized (this.zzpV) {
            this.zzKu.zzgL();
        }
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) {
        synchronized (this.zzpV) {
            this.zzKu.zza(rewardedVideoAdRequestParcel);
        }
    }

    public void zza(com.google.android.gms.ads.internal.reward.client.zzd com_google_android_gms_ads_internal_reward_client_zzd) {
        synchronized (this.zzpV) {
            this.zzKu.zza(com_google_android_gms_ads_internal_reward_client_zzd);
        }
    }
}
