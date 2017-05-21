package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzeb extends zza {
    private zzk zzAD;
    private zzdx zzAJ;
    private zzgh zzAK;
    private String zzAL;
    private zzdv zzAz;
    private String zzpS;

    public zzeb(Context context, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        this(str, new zzdv(context.getApplicationContext(), com_google_android_gms_internal_zzex, versionInfoParcel, com_google_android_gms_ads_internal_zzd));
    }

    public zzeb(String str, zzdv com_google_android_gms_internal_zzdv) {
        this.zzpS = str;
        this.zzAz = com_google_android_gms_internal_zzdv;
        this.zzAJ = new zzdx();
        zzr.zzbN().zza(com_google_android_gms_internal_zzdv);
    }

    private void zzel() {
        if (this.zzAD != null && this.zzAK != null) {
            this.zzAD.zza(this.zzAK, this.zzAL);
        }
    }

    void abort() {
        if (this.zzAD == null) {
            this.zzAD = this.zzAz.zzX(this.zzpS);
            this.zzAJ.zzc(this.zzAD);
            zzel();
        }
    }

    public void destroy() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.destroy();
        }
    }

    public String getMediationAdapterClassName() throws RemoteException {
        return this.zzAD != null ? this.zzAD.getMediationAdapterClassName() : null;
    }

    public boolean isLoading() throws RemoteException {
        return this.zzAD != null && this.zzAD.isLoading();
    }

    public boolean isReady() throws RemoteException {
        return this.zzAD != null && this.zzAD.isReady();
    }

    public void pause() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.pause();
        }
    }

    public void resume() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.resume();
        }
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) throws RemoteException {
        abort();
        if (this.zzAD != null) {
            this.zzAD.setManualImpressionsEnabled(manualImpressionsEnabled);
        }
    }

    public void setUserId(String useId) {
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.showInterstitial();
        } else {
            zzb.zzaK("Interstitial ad must be loaded before showInterstitial().");
        }
    }

    public void stopLoading() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.stopLoading();
        }
    }

    public void zza(AdSizeParcel adSizeParcel) throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.zza(adSizeParcel);
        }
    }

    public void zza(zzp com_google_android_gms_ads_internal_client_zzp) throws RemoteException {
        this.zzAJ.zzAt = com_google_android_gms_ads_internal_client_zzp;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzq com_google_android_gms_ads_internal_client_zzq) throws RemoteException {
        this.zzAJ.zzpK = com_google_android_gms_ads_internal_client_zzq;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzw com_google_android_gms_ads_internal_client_zzw) throws RemoteException {
        this.zzAJ.zzAq = com_google_android_gms_ads_internal_client_zzw;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzx com_google_android_gms_ads_internal_client_zzx) throws RemoteException {
        abort();
        if (this.zzAD != null) {
            this.zzAD.zza(com_google_android_gms_ads_internal_client_zzx);
        }
    }

    public void zza(com.google.android.gms.ads.internal.reward.client.zzd com_google_android_gms_ads_internal_reward_client_zzd) {
        this.zzAJ.zzAu = com_google_android_gms_ads_internal_reward_client_zzd;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzcf com_google_android_gms_internal_zzcf) throws RemoteException {
        this.zzAJ.zzAs = com_google_android_gms_internal_zzcf;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzgd com_google_android_gms_internal_zzgd) throws RemoteException {
        this.zzAJ.zzAr = com_google_android_gms_internal_zzgd;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzgh com_google_android_gms_internal_zzgh, String str) throws RemoteException {
        this.zzAK = com_google_android_gms_internal_zzgh;
        this.zzAL = str;
        zzel();
    }

    public com.google.android.gms.dynamic.zzd zzaM() throws RemoteException {
        return this.zzAD != null ? this.zzAD.zzaM() : null;
    }

    public AdSizeParcel zzaN() throws RemoteException {
        return this.zzAD != null ? this.zzAD.zzaN() : null;
    }

    public void zzaP() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.zzaP();
        } else {
            zzb.zzaK("Interstitial ad must be loaded before pingManualTrackingUrl().");
        }
    }

    public boolean zzb(AdRequestParcel adRequestParcel) throws RemoteException {
        if (zzi(adRequestParcel)) {
            abort();
        }
        if (adRequestParcel.zztJ != null) {
            abort();
        }
        if (this.zzAD != null) {
            return this.zzAD.zzb(adRequestParcel);
        }
        zza zza = zzr.zzbN().zza(adRequestParcel, this.zzpS);
        if (zza != null) {
            if (!zza.zzAG) {
                zza.zzh(adRequestParcel);
            }
            this.zzAD = zza.zzAD;
            zza.zzc(this.zzAz);
            zza.zzAE.zza(this.zzAJ);
            this.zzAJ.zzc(this.zzAD);
            zzel();
            return zza.zzAH;
        }
        this.zzAD = this.zzAz.zzX(this.zzpS);
        this.zzAJ.zzc(this.zzAD);
        zzel();
        return this.zzAD.zzb(adRequestParcel);
    }

    boolean zzi(AdRequestParcel adRequestParcel) {
        Bundle bundle = adRequestParcel.zztM;
        if (bundle == null) {
            return false;
        }
        bundle = bundle.getBundle(AdMobAdapter.class.getCanonicalName());
        if (bundle == null) {
            return false;
        }
        return bundle.keySet().contains("gw");
    }
}
