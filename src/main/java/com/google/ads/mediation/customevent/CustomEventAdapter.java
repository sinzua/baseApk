package com.google.ads.mediation.customevent;

import android.app.Activity;
import android.view.View;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventExtras;
import com.google.android.gms.common.annotation.KeepName;

@KeepName
public final class CustomEventAdapter implements MediationBannerAdapter<CustomEventExtras, CustomEventServerParameters>, MediationInterstitialAdapter<CustomEventExtras, CustomEventServerParameters> {
    private View zzbk;
    CustomEventBanner zzbl;
    CustomEventInterstitial zzbm;

    static final class zza implements CustomEventBannerListener {
        private final CustomEventAdapter zzbn;
        private final MediationBannerListener zzbo;

        public zza(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.zzbn = customEventAdapter;
            this.zzbo = mediationBannerListener;
        }

        public void onClick() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbo.onClick(this.zzbn);
        }

        public void onDismissScreen() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbo.onDismissScreen(this.zzbn);
        }

        public void onFailedToReceiveAd() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbo.onFailedToReceiveAd(this.zzbn, ErrorCode.NO_FILL);
        }

        public void onLeaveApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbo.onLeaveApplication(this.zzbn);
        }

        public void onPresentScreen() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbo.onPresentScreen(this.zzbn);
        }

        public void onReceivedAd(View view) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onReceivedAd.");
            this.zzbn.zza(view);
            this.zzbo.onReceivedAd(this.zzbn);
        }
    }

    class zzb implements CustomEventInterstitialListener {
        private final CustomEventAdapter zzbn;
        private final MediationInterstitialListener zzbp;
        final /* synthetic */ CustomEventAdapter zzbq;

        public zzb(CustomEventAdapter customEventAdapter, CustomEventAdapter customEventAdapter2, MediationInterstitialListener mediationInterstitialListener) {
            this.zzbq = customEventAdapter;
            this.zzbn = customEventAdapter2;
            this.zzbp = mediationInterstitialListener;
        }

        public void onDismissScreen() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onDismissScreen.");
            this.zzbp.onDismissScreen(this.zzbn);
        }

        public void onFailedToReceiveAd() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onFailedToReceiveAd.");
            this.zzbp.onFailedToReceiveAd(this.zzbn, ErrorCode.NO_FILL);
        }

        public void onLeaveApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onLeaveApplication.");
            this.zzbp.onLeaveApplication(this.zzbn);
        }

        public void onPresentScreen() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onPresentScreen.");
            this.zzbp.onPresentScreen(this.zzbn);
        }

        public void onReceivedAd() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Custom event adapter called onReceivedAd.");
            this.zzbp.onReceivedAd(this.zzbq);
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

    public void destroy() {
        if (this.zzbl != null) {
            this.zzbl.destroy();
        }
        if (this.zzbm != null) {
            this.zzbm.destroy();
        }
    }

    public Class<CustomEventExtras> getAdditionalParametersType() {
        return CustomEventExtras.class;
    }

    public View getBannerView() {
        return this.zzbk;
    }

    public Class<CustomEventServerParameters> getServerParametersType() {
        return CustomEventServerParameters.class;
    }

    public void requestBannerAd(MediationBannerListener listener, Activity activity, CustomEventServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.zzbl = (CustomEventBanner) zzj(serverParameters.className);
        if (this.zzbl == null) {
            listener.onFailedToReceiveAd(this, ErrorCode.INTERNAL_ERROR);
        } else {
            this.zzbl.requestBannerAd(new zza(this, listener), activity, serverParameters.label, serverParameters.parameter, adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    public void requestInterstitialAd(MediationInterstitialListener listener, Activity activity, CustomEventServerParameters serverParameters, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.zzbm = (CustomEventInterstitial) zzj(serverParameters.className);
        if (this.zzbm == null) {
            listener.onFailedToReceiveAd(this, ErrorCode.INTERNAL_ERROR);
        } else {
            this.zzbm.requestInterstitialAd(zza(listener), activity, serverParameters.label, serverParameters.parameter, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    public void showInterstitial() {
        this.zzbm.showInterstitial();
    }

    zzb zza(MediationInterstitialListener mediationInterstitialListener) {
        return new zzb(this, this, mediationInterstitialListener);
    }
}
