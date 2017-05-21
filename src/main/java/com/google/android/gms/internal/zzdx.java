package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;

@zzhb
class zzdx {
    zzw zzAq;
    zzgd zzAr;
    zzcf zzAs;
    zzp zzAt;
    zzd zzAu;
    zzq zzpK;

    private class zza extends com.google.android.gms.ads.internal.client.zzq.zza {
        zzq zzAv;
        final /* synthetic */ zzdx zzAw;

        zza(zzdx com_google_android_gms_internal_zzdx, zzq com_google_android_gms_ads_internal_client_zzq) {
            this.zzAw = com_google_android_gms_internal_zzdx;
            this.zzAv = com_google_android_gms_ads_internal_client_zzq;
        }

        public void onAdClosed() throws RemoteException {
            this.zzAv.onAdClosed();
            zzr.zzbN().zzee();
        }

        public void onAdFailedToLoad(int errorCode) throws RemoteException {
            this.zzAv.onAdFailedToLoad(errorCode);
        }

        public void onAdLeftApplication() throws RemoteException {
            this.zzAv.onAdLeftApplication();
        }

        public void onAdLoaded() throws RemoteException {
            this.zzAv.onAdLoaded();
        }

        public void onAdOpened() throws RemoteException {
            this.zzAv.onAdOpened();
        }
    }

    zzdx() {
    }

    void zzc(zzk com_google_android_gms_ads_internal_zzk) {
        if (this.zzpK != null) {
            com_google_android_gms_ads_internal_zzk.zza(new zza(this, this.zzpK));
        }
        if (this.zzAq != null) {
            com_google_android_gms_ads_internal_zzk.zza(this.zzAq);
        }
        if (this.zzAr != null) {
            com_google_android_gms_ads_internal_zzk.zza(this.zzAr);
        }
        if (this.zzAs != null) {
            com_google_android_gms_ads_internal_zzk.zza(this.zzAs);
        }
        if (this.zzAt != null) {
            com_google_android_gms_ads_internal_zzk.zza(this.zzAt);
        }
        if (this.zzAu != null) {
            com_google_android_gms_ads_internal_zzk.zza(this.zzAu);
        }
    }
}
