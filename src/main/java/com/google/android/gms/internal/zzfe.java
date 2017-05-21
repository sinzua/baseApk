package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzey.zza;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

@zzhb
public final class zzfe extends zza {
    private final MediationAdapter zzCI;
    private zzff zzCJ;

    public zzfe(MediationAdapter mediationAdapter) {
        this.zzCI = mediationAdapter;
    }

    private Bundle zza(String str, int i, String str2) throws RemoteException {
        zzb.zzaK("Server parameters: " + str);
        try {
            Bundle bundle = new Bundle();
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str);
                Bundle bundle2 = new Bundle();
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str3 = (String) keys.next();
                    bundle2.putString(str3, jSONObject.getString(str3));
                }
                bundle = bundle2;
            }
            if (this.zzCI instanceof AdMobAdapter) {
                bundle.putString("adJson", str2);
                bundle.putInt("tagForChildDirectedTreatment", i);
            }
            return bundle;
        } catch (Throwable th) {
            zzb.zzd("Could not get Server Parameters Bundle.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void destroy() throws RemoteException {
        try {
            this.zzCI.onDestroy();
        } catch (Throwable th) {
            zzb.zzd("Could not destroy adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public Bundle getInterstitialAdapterInfo() {
        if (this.zzCI instanceof zzka) {
            return ((zzka) this.zzCI).getInterstitialAdapterInfo();
        }
        zzb.zzaK("MediationAdapter is not a v2 MediationInterstitialAdapter: " + this.zzCI.getClass().getCanonicalName());
        return new Bundle();
    }

    public zzd getView() throws RemoteException {
        if (this.zzCI instanceof MediationBannerAdapter) {
            try {
                return zze.zzC(((MediationBannerAdapter) this.zzCI).getBannerView());
            } catch (Throwable th) {
                zzb.zzd("Could not get banner view from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationBannerAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public boolean isInitialized() throws RemoteException {
        if (this.zzCI instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaI("Check if adapter is initialized.");
            try {
                return ((MediationRewardedVideoAdAdapter) this.zzCI).isInitialized();
            } catch (Throwable th) {
                zzb.zzd("Could not check if adapter is initialized.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void pause() throws RemoteException {
        try {
            this.zzCI.onPause();
        } catch (Throwable th) {
            zzb.zzd("Could not pause adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void resume() throws RemoteException {
        try {
            this.zzCI.onResume();
        } catch (Throwable th) {
            zzb.zzd("Could not resume adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzCI instanceof MediationInterstitialAdapter) {
            zzb.zzaI("Showing interstitial from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzCI).showInterstitial();
            } catch (Throwable th) {
                zzb.zzd("Could not show interstitial from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void showVideo() throws RemoteException {
        if (this.zzCI instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaI("Show rewarded video ad from adapter.");
            try {
                ((MediationRewardedVideoAdAdapter) this.zzCI).showVideo();
            } catch (Throwable th) {
                zzb.zzd("Could not show rewarded video ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(AdRequestParcel adRequestParcel, String str, String str2) throws RemoteException {
        if (this.zzCI instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaI("Requesting rewarded video ad from adapter.");
            try {
                MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzCI;
                mediationRewardedVideoAdAdapter.loadAd(new zzfd(adRequestParcel.zztC == -1 ? null : new Date(adRequestParcel.zztC), adRequestParcel.zztD, adRequestParcel.zztE != null ? new HashSet(adRequestParcel.zztE) : null, adRequestParcel.zztK, adRequestParcel.zztF, adRequestParcel.zztG, adRequestParcel.zztR), zza(str, adRequestParcel.zztG, str2), adRequestParcel.zztM != null ? adRequestParcel.zztM.getBundle(mediationRewardedVideoAdAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not load rewarded video ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza com_google_android_gms_ads_internal_reward_mediation_client_zza, String str2) throws RemoteException {
        if (this.zzCI instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaI("Initialize rewarded video adapter.");
            try {
                MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzCI;
                mediationRewardedVideoAdAdapter.initialize((Context) zze.zzp(com_google_android_gms_dynamic_zzd), new zzfd(adRequestParcel.zztC == -1 ? null : new Date(adRequestParcel.zztC), adRequestParcel.zztD, adRequestParcel.zztE != null ? new HashSet(adRequestParcel.zztE) : null, adRequestParcel.zztK, adRequestParcel.zztF, adRequestParcel.zztG, adRequestParcel.zztR), str, new com.google.android.gms.ads.internal.reward.mediation.client.zzb(com_google_android_gms_ads_internal_reward_mediation_client_zza), zza(str2, adRequestParcel.zztG, null), adRequestParcel.zztM != null ? adRequestParcel.zztM.getBundle(mediationRewardedVideoAdAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not initialize rewarded video adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        zza(com_google_android_gms_dynamic_zzd, adRequestParcel, str, null, com_google_android_gms_internal_zzez);
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        if (this.zzCI instanceof MediationInterstitialAdapter) {
            zzb.zzaI("Requesting interstitial ad from adapter.");
            try {
                MediationInterstitialAdapter mediationInterstitialAdapter = (MediationInterstitialAdapter) this.zzCI;
                mediationInterstitialAdapter.requestInterstitialAd((Context) zze.zzp(com_google_android_gms_dynamic_zzd), new zzff(com_google_android_gms_internal_zzez), zza(str, adRequestParcel.zztG, str2), new zzfd(adRequestParcel.zztC == -1 ? null : new Date(adRequestParcel.zztC), adRequestParcel.zztD, adRequestParcel.zztE != null ? new HashSet(adRequestParcel.zztE) : null, adRequestParcel.zztK, adRequestParcel.zztF, adRequestParcel.zztG, adRequestParcel.zztR), adRequestParcel.zztM != null ? adRequestParcel.zztM.getBundle(mediationInterstitialAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) throws RemoteException {
        if (this.zzCI instanceof MediationNativeAdapter) {
            try {
                MediationNativeAdapter mediationNativeAdapter = (MediationNativeAdapter) this.zzCI;
                zzfi com_google_android_gms_internal_zzfi = new zzfi(adRequestParcel.zztC == -1 ? null : new Date(adRequestParcel.zztC), adRequestParcel.zztD, adRequestParcel.zztE != null ? new HashSet(adRequestParcel.zztE) : null, adRequestParcel.zztK, adRequestParcel.zztF, adRequestParcel.zztG, nativeAdOptionsParcel, list, adRequestParcel.zztR);
                Bundle bundle = adRequestParcel.zztM != null ? adRequestParcel.zztM.getBundle(mediationNativeAdapter.getClass().getName()) : null;
                this.zzCJ = new zzff(com_google_android_gms_internal_zzez);
                mediationNativeAdapter.requestNativeAd((Context) zze.zzp(com_google_android_gms_dynamic_zzd), this.zzCJ, zza(str, adRequestParcel.zztG, str2), com_google_android_gms_internal_zzfi, bundle);
            } catch (Throwable th) {
                zzb.zzd("Could not request native ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationNativeAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        zza(com_google_android_gms_dynamic_zzd, adSizeParcel, adRequestParcel, str, null, com_google_android_gms_internal_zzez);
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        if (this.zzCI instanceof MediationBannerAdapter) {
            zzb.zzaI("Requesting banner ad from adapter.");
            try {
                MediationBannerAdapter mediationBannerAdapter = (MediationBannerAdapter) this.zzCI;
                mediationBannerAdapter.requestBannerAd((Context) zze.zzp(com_google_android_gms_dynamic_zzd), new zzff(com_google_android_gms_internal_zzez), zza(str, adRequestParcel.zztG, str2), com.google.android.gms.ads.zza.zza(adSizeParcel.width, adSizeParcel.height, adSizeParcel.zzuh), new zzfd(adRequestParcel.zztC == -1 ? null : new Date(adRequestParcel.zztC), adRequestParcel.zztD, adRequestParcel.zztE != null ? new HashSet(adRequestParcel.zztE) : null, adRequestParcel.zztK, adRequestParcel.zztF, adRequestParcel.zztG, adRequestParcel.zztR), adRequestParcel.zztM != null ? adRequestParcel.zztM.getBundle(mediationBannerAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not request banner ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationBannerAdapter: " + this.zzCI.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zzb(AdRequestParcel adRequestParcel, String str) throws RemoteException {
        zza(adRequestParcel, str, null);
    }

    public zzfb zzeF() {
        NativeAdMapper zzeJ = this.zzCJ.zzeJ();
        return zzeJ instanceof NativeAppInstallAdMapper ? new zzfg((NativeAppInstallAdMapper) zzeJ) : null;
    }

    public zzfc zzeG() {
        NativeAdMapper zzeJ = this.zzCJ.zzeJ();
        return zzeJ instanceof NativeContentAdMapper ? new zzfh((NativeContentAdMapper) zzeJ) : null;
    }

    public Bundle zzeH() {
        if (this.zzCI instanceof zzjz) {
            return ((zzjz) this.zzCI).zzeH();
        }
        zzb.zzaK("MediationAdapter is not a v2 MediationBannerAdapter: " + this.zzCI.getClass().getCanonicalName());
        return new Bundle();
    }

    public Bundle zzeI() {
        return new Bundle();
    }
}
