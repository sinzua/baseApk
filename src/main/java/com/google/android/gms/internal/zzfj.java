package com.google.android.gms.internal;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzey.zza;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@zzhb
public final class zzfj<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends zza {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> zzCO;
    private final NETWORK_EXTRAS zzCP;

    public zzfj(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.zzCO = mediationAdapter;
        this.zzCP = network_extras;
    }

    private SERVER_PARAMETERS zzb(String str, int i, String str2) throws RemoteException {
        Map hashMap;
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                hashMap = new HashMap(jSONObject.length());
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str3 = (String) keys.next();
                    hashMap.put(str3, jSONObject.getString(str3));
                }
            } catch (Throwable th) {
                zzb.zzd("Could not get MediationServerParameters.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            hashMap = new HashMap(0);
        }
        Class serverParametersType = this.zzCO.getServerParametersType();
        if (serverParametersType == null) {
            return null;
        }
        MediationServerParameters mediationServerParameters = (MediationServerParameters) serverParametersType.newInstance();
        mediationServerParameters.load(hashMap);
        return mediationServerParameters;
    }

    public void destroy() throws RemoteException {
        try {
            this.zzCO.destroy();
        } catch (Throwable th) {
            zzb.zzd("Could not destroy adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public Bundle getInterstitialAdapterInfo() {
        return new Bundle();
    }

    public zzd getView() throws RemoteException {
        if (this.zzCO instanceof MediationBannerAdapter) {
            try {
                return zze.zzC(((MediationBannerAdapter) this.zzCO).getBannerView());
            } catch (Throwable th) {
                zzb.zzd("Could not get banner view from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationBannerAdapter: " + this.zzCO.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public boolean isInitialized() {
        return true;
    }

    public void pause() throws RemoteException {
        throw new RemoteException();
    }

    public void resume() throws RemoteException {
        throw new RemoteException();
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzCO instanceof MediationInterstitialAdapter) {
            zzb.zzaI("Showing interstitial from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzCO).showInterstitial();
            } catch (Throwable th) {
                zzb.zzd("Could not show interstitial from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzCO.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void showVideo() {
    }

    public void zza(AdRequestParcel adRequestParcel, String str, String str2) {
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza com_google_android_gms_ads_internal_reward_mediation_client_zza, String str2) throws RemoteException {
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        zza(com_google_android_gms_dynamic_zzd, adRequestParcel, str, null, com_google_android_gms_internal_zzez);
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        if (this.zzCO instanceof MediationInterstitialAdapter) {
            zzb.zzaI("Requesting interstitial ad from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzCO).requestInterstitialAd(new zzfk(com_google_android_gms_internal_zzez), (Activity) zze.zzp(com_google_android_gms_dynamic_zzd), zzb(str, adRequestParcel.zztG, str2), zzfl.zzj(adRequestParcel), this.zzCP);
            } catch (Throwable th) {
                zzb.zzd("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzCO.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        zza(com_google_android_gms_dynamic_zzd, adSizeParcel, adRequestParcel, str, null, com_google_android_gms_internal_zzez);
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzez com_google_android_gms_internal_zzez) throws RemoteException {
        if (this.zzCO instanceof MediationBannerAdapter) {
            zzb.zzaI("Requesting banner ad from adapter.");
            try {
                ((MediationBannerAdapter) this.zzCO).requestBannerAd(new zzfk(com_google_android_gms_internal_zzez), (Activity) zze.zzp(com_google_android_gms_dynamic_zzd), zzb(str, adRequestParcel.zztG, str2), zzfl.zzb(adSizeParcel), zzfl.zzj(adRequestParcel), this.zzCP);
            } catch (Throwable th) {
                zzb.zzd("Could not request banner ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaK("MediationAdapter is not a MediationBannerAdapter: " + this.zzCO.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zzb(AdRequestParcel adRequestParcel, String str) {
    }

    public zzfb zzeF() {
        return null;
    }

    public zzfc zzeG() {
        return null;
    }

    public Bundle zzeH() {
        return new Bundle();
    }

    public Bundle zzeI() {
        return new Bundle();
    }
}
