package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.internal.client.zzm;
import com.google.android.gms.ads.internal.client.zzs;
import com.google.android.gms.ads.internal.client.zzu;
import com.google.android.gms.ads.internal.client.zzy;
import com.google.android.gms.ads.internal.formats.zzk;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.purchase.zze;
import com.google.android.gms.ads.internal.reward.client.zzb;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzcj;
import com.google.android.gms.internal.zzeb;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfv;
import com.google.android.gms.internal.zzge;
import com.google.android.gms.internal.zzhs;

public class ClientApi implements zzm {
    public static void retainReference() {
        zzl.zzuq = ClientApi.class.getName();
    }

    public zzs createAdLoaderBuilder(Context context, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzj(context, adUnitId, adapterCreator, versionInfo, zzd.zzbg());
    }

    public zzfv createAdOverlay(Activity activity) {
        return new zzd(activity);
    }

    public zzu createBannerAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzf(context, adSize, adUnitId, adapterCreator, versionInfo, zzd.zzbg());
    }

    public zzge createInAppPurchaseManager(Activity activity) {
        return new zze(activity);
    }

    public zzu createInterstitialAdManager(Context context, AdSizeParcel adSize, String adUnitId, zzew adapterCreator, VersionInfoParcel versionInfo) {
        if (((Boolean) zzbt.zzwE.get()).booleanValue()) {
            return new zzeb(context, adUnitId, adapterCreator, versionInfo, zzd.zzbg());
        }
        return new zzk(context, adSize, adUnitId, adapterCreator, versionInfo, zzd.zzbg());
    }

    public zzcj createNativeAdViewDelegate(FrameLayout nativeAdView, FrameLayout overlayFrame) {
        return new zzk(nativeAdView, overlayFrame);
    }

    public zzb createRewardedVideoAd(Context context, zzew adapterCreator, VersionInfoParcel versionInfo) {
        return new zzhs(context, zzd.zzbg(), adapterCreator, versionInfo);
    }

    public zzy getMobileAdsSettingsManager(Context context) {
        return zzn.zzr(context);
    }
}
