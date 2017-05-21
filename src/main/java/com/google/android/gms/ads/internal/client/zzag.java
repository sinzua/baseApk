package com.google.android.gms.ads.internal.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;

public class zzag extends com.google.android.gms.ads.internal.client.zzs.zza {
    private zzq zzpK;

    private class zza extends com.google.android.gms.ads.internal.client.zzr.zza {
        final /* synthetic */ zzag zzuY;

        private zza(zzag com_google_android_gms_ads_internal_client_zzag) {
            this.zzuY = com_google_android_gms_ads_internal_client_zzag;
        }

        public String getMediationAdapterClassName() throws RemoteException {
            return null;
        }

        public boolean isLoading() throws RemoteException {
            return false;
        }

        public void zzf(AdRequestParcel adRequestParcel) throws RemoteException {
            zzb.e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
            com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
                final /* synthetic */ zza zzuZ;

                {
                    this.zzuZ = r1;
                }

                public void run() {
                    if (this.zzuZ.zzuY.zzpK != null) {
                        try {
                            this.zzuZ.zzuY.zzpK.onAdFailedToLoad(1);
                        } catch (Throwable e) {
                            zzb.zzd("Could not notify onAdFailedToLoad event.", e);
                        }
                    }
                }
            });
        }
    }

    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) throws RemoteException {
    }

    public void zza(zzcr com_google_android_gms_internal_zzcr) throws RemoteException {
    }

    public void zza(zzcs com_google_android_gms_internal_zzcs) throws RemoteException {
    }

    public void zza(String str, zzcu com_google_android_gms_internal_zzcu, zzct com_google_android_gms_internal_zzct) throws RemoteException {
    }

    public void zzb(zzq com_google_android_gms_ads_internal_client_zzq) throws RemoteException {
        this.zzpK = com_google_android_gms_ads_internal_client_zzq;
    }

    public void zzb(zzx com_google_android_gms_ads_internal_client_zzx) throws RemoteException {
    }

    public zzr zzbn() throws RemoteException {
        return new zza();
    }
}
