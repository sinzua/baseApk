package com.google.android.gms.ads.internal.client;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.reward.client.zzb;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcj;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfv;
import com.google.android.gms.internal.zzge;

public class zzai implements zzm {
    public zzs createAdLoaderBuilder(Context context, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzag();
    }

    @Nullable
    public zzfv createAdOverlay(Activity activity) {
        return null;
    }

    public zzu createBannerAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzah();
    }

    @Nullable
    public zzge createInAppPurchaseManager(Activity activity) {
        return null;
    }

    public zzu createInterstitialAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzah();
    }

    public zzcj createNativeAdViewDelegate(FrameLayout nativeAdView, FrameLayout overlayFrame) {
        return new zzak();
    }

    public zzb createRewardedVideoAd(Context context, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzal();
    }

    public zzy getMobileAdsSettingsManager(Context context) {
        return new zzaj();
    }
}
