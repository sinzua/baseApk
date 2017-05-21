package com.google.android.gms.ads.mediation.customevent;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.common.annotation.KeepName;

@KeepName
public final class CustomEventAdapter implements MediationBannerAdapter, MediationInterstitialAdapter, MediationNativeAdapter {
    CustomEventBanner zzOq;
    CustomEventInterstitial zzOr;
    CustomEventNative zzOs;
    private View zzbk;

    static final class zza implements CustomEventBannerListener {
        private final CustomEventAdapter zzOt;
        private final MediationBannerListener zzbc;

        public zza(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.zzOt = customEventAdapter;
            this.zzbc = mediationBannerListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClicked.");
            this.zzbc.onAdClicked(this.zzOt);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClosed.");
            this.zzbc.onAdClosed(this.zzOt);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdFailedToLoad.");
            this.zzbc.onAdFailedToLoad(this.zzOt, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdLeftApplication.");
            this.zzbc.onAdLeftApplication(this.zzOt);
        }

        public void onAdLoaded(View view) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdLoaded.");
            this.zzOt.zza(view);
            this.zzbc.onAdLoaded(this.zzOt);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdOpened.");
            this.zzbc.onAdOpened(this.zzOt);
        }
    }

    class zzb implements CustomEventInterstitialListener {
        private final CustomEventAdapter zzOt;
        final /* synthetic */ CustomEventAdapter zzOu;
        private final MediationInterstitialListener zzbd;

        public zzb(CustomEventAdapter customEventAdapter, CustomEventAdapter customEventAdapter2, MediationInterstitialListener mediationInterstitialListener) {
            this.zzOu = customEventAdapter;
            this.zzOt = customEventAdapter2;
            this.zzbd = mediationInterstitialListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClicked.");
            this.zzbd.onAdClicked(this.zzOt);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClosed.");
            this.zzbd.onAdClosed(this.zzOt);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbd.onAdFailedToLoad(this.zzOt, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdLeftApplication.");
            this.zzbd.onAdLeftApplication(this.zzOt);
        }

        public void onAdLoaded() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onReceivedAd.");
            this.zzbd.onAdLoaded(this.zzOu);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdOpened.");
            this.zzbd.onAdOpened(this.zzOt);
        }
    }

    static class zzc implements CustomEventNativeListener {
        private final CustomEventAdapter zzOt;
        private final MediationNativeListener zzbe;

        public zzc(CustomEventAdapter customEventAdapter, MediationNativeListener mediationNativeListener) {
            this.zzOt = customEventAdapter;
            this.zzbe = mediationNativeListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClicked.");
            this.zzbe.onAdClicked(this.zzOt);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdClosed.");
            this.zzbe.onAdClosed(this.zzOt);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdFailedToLoad.");
            this.zzbe.onAdFailedToLoad(this.zzOt, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdLeftApplication.");
            this.zzbe.onAdLeftApplication(this.zzOt);
        }

        public void onAdLoaded(NativeAdMapper ad) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdLoaded.");
            this.zzbe.onAdLoaded(this.zzOt, ad);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onAdOpened.");
            this.zzbe.onAdOpened(this.zzOt);
        }
    }

    private void zza(View view) {
        this.zzbk = view;
    }

    private static <T> T zzj(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Could not instantiate custom event adapter: " + str + ". " + th.getMessage());
            return null;
        }
    }

    public View getBannerView() {
        return this.zzbk;
    }

    public void onDestroy() {
        if (this.zzOq != null) {
            this.zzOq.onDestroy();
        }
        if (this.zzOr != null) {
            this.zzOr.onDestroy();
        }
        if (this.zzOs != null) {
            this.zzOs.onDestroy();
        }
    }

    public void onPause() {
        if (this.zzOq != null) {
            this.zzOq.onPause();
        }
        if (this.zzOr != null) {
            this.zzOr.onPause();
        }
        if (this.zzOs != null) {
            this.zzOs.onPause();
        }
    }

    public void onResume() {
        if (this.zzOq != null) {
            this.zzOq.onResume();
        }
        if (this.zzOr != null) {
            this.zzOr.onResume();
        }
        if (this.zzOs != null) {
            this.zzOs.onResume();
        }
    }

    public void requestBannerAd(Context context, MediationBannerListener listener, Bundle serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzOq = (CustomEventBanner) zzj(serverParameters.getString("class_name"));
        if (this.zzOq == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzOq.requestBannerAd(context, new zza(this, listener), serverParameters.getString("parameter"), adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestInterstitialAd(Context context, MediationInterstitialListener listener, Bundle serverParameters, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzOr = (CustomEventInterstitial) zzj(serverParameters.getString("class_name"));
        if (this.zzOr == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzOr.requestInterstitialAd(context, zza(listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestNativeAd(Context context, MediationNativeListener listener, Bundle serverParameters, NativeMediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzOs = (CustomEventNative) zzj(serverParameters.getString("class_name"));
        if (this.zzOs == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzOs.requestNativeAd(context, new zzc(this, listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void showInterstitial() {
        this.zzOr.showInterstitial();
    }

    zzb zza(MediationInterstitialListener mediationInterstitialListener) {
        return new zzb(this, this, mediationInterstitialListener);
    }
}
