package com.google.android.gms.ads.internal.client;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.ClientApi;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcj;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfv;
import com.google.android.gms.internal.zzge;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzl {
    public static String zzuq = null;
    private zzm zzup;

    public zzl() {
        ClientApi.retainReference();
        if (zzuq != null) {
            try {
                this.zzup = (zzm) zzl.class.getClassLoader().loadClass(zzuq).newInstance();
                return;
            } catch (Throwable e) {
                zzb.zzd("Failed to instantiate ClientApi class.", e);
                this.zzup = new zzai();
                return;
            }
        }
        zzb.zzaK("No client jar implementation found.");
        this.zzup = new zzai();
    }

    public zzs createAdLoaderBuilder(Context context, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return this.zzup.createAdLoaderBuilder(context, adUnitId, adapterCreator, versionInfo);
    }

    @Nullable
    public zzfv createAdOverlay(Activity activity) {
        return this.zzup.createAdOverlay(activity);
    }

    public zzu createBannerAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return this.zzup.createBannerAdManager(context, adSize, adUnitId, adapterCreator, versionInfo);
    }

    @Nullable
    public zzge createInAppPurchaseManager(Activity activity) {
        return this.zzup.createInAppPurchaseManager(activity);
    }

    public zzu createInterstitialAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return this.zzup.createInterstitialAdManager(context, adSize, adUnitId, adapterCreator, versionInfo);
    }

    public zzcj createNativeAdViewDelegate(FrameLayout nativeAdView, FrameLayout overlayFrame) {
        return this.zzup.createNativeAdViewDelegate(nativeAdView, overlayFrame);
    }

    public com.google.android.gms.ads.internal.reward.client.zzb createRewardedVideoAd(Context context, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return this.zzup.createRewardedVideoAd(context, adapterCreator, versionInfo);
    }

    public zzy getMobileAdsSettingsManager(Context context) {
        return this.zzup.getMobileAdsSettingsManager(context);
    }
}
