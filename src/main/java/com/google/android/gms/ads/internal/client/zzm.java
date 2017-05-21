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

public interface zzm {
    zzs createAdLoaderBuilder(Context context, String str, zzew com_google_android_gms_internal_zzew, VersionInfoParcel versionInfoParcel);

    @Nullable
    zzfv createAdOverlay(Activity activity);

    zzu createBannerAdManager(Context context, AdSizeParcel adSizeParcel, String str, zzew com_google_android_gms_internal_zzew, VersionInfoParcel versionInfoParcel);

    @Nullable
    zzge createInAppPurchaseManager(Activity activity);

    zzu createInterstitialAdManager(Context context, AdSizeParcel adSizeParcel, String str, zzew com_google_android_gms_internal_zzew, VersionInfoParcel versionInfoParcel);

    zzcj createNativeAdViewDelegate(FrameLayout frameLayout, FrameLayout frameLayout2);

    zzb createRewardedVideoAd(Context context, zzew com_google_android_gms_internal_zzew, VersionInfoParcel versionInfoParcel);

    zzy getMobileAdsSettingsManager(Context context);
}
