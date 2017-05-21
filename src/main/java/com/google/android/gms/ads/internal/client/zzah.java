package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzgh;

public class zzah extends zza {
    private zzq zzpK;

    public void destroy() {
    }

    public String getMediationAdapterClassName() {
        return null;
    }

    public boolean isLoading() {
        return false;
    }

    public boolean isReady() {
        return false;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setManualImpressionsEnabled(boolean enabled) {
    }

    public void setUserId(String userId) {
    }

    public void showInterstitial() {
    }

    public void stopLoading() {
    }

    public void zza(AdSizeParcel adSizeParcel) {
    }

    public void zza(zzp com_google_android_gms_ads_internal_client_zzp) {
    }

    public void zza(zzq com_google_android_gms_ads_internal_client_zzq) {
        this.zzpK = com_google_android_gms_ads_internal_client_zzq;
    }

    public void zza(zzw com_google_android_gms_ads_internal_client_zzw) {
    }

    public void zza(zzx com_google_android_gms_ads_internal_client_zzx) {
    }

    public void zza(zzd com_google_android_gms_ads_internal_reward_client_zzd) {
    }

    public void zza(zzcf com_google_android_gms_internal_zzcf) {
    }

    public void zza(zzgd com_google_android_gms_internal_zzgd) {
    }

    public void zza(zzgh com_google_android_gms_internal_zzgh, String str) {
    }

    public com.google.android.gms.dynamic.zzd zzaM() {
        return null;
    }

    public AdSizeParcel zzaN() {
        return null;
    }

    public void zzaP() {
    }

    public boolean zzb(AdRequestParcel adRequestParcel) {
        zzb.e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
            final /* synthetic */ zzah zzva;

            {
                this.zzva = r1;
            }

            public void run() {
                if (this.zzva.zzpK != null) {
                    try {
                        this.zzva.zzpK.onAdFailedToLoad(1);
                    } catch (Throwable e) {
                        zzb.zzd("Could not notify onAdFailedToLoad event.", e);
                    }
                }
            }
        });
        return false;
    }
}
