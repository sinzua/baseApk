package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zzb;

public final class zzmh implements zzmg {

    private static class zza extends zzme {
        private final zzb<Status> zzamC;

        public zza(zzb<Status> com_google_android_gms_common_api_internal_zza_zzb_com_google_android_gms_common_api_Status) {
            this.zzamC = com_google_android_gms_common_api_internal_zza_zzb_com_google_android_gms_common_api_Status;
        }

        public void zzcb(int i) throws RemoteException {
            this.zzamC.zzs(new Status(i));
        }
    }

    public PendingResult<Status> zzf(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            final /* synthetic */ zzmh zzamB;

            protected void zza(zzmj com_google_android_gms_internal_zzmj) throws RemoteException {
                ((zzml) com_google_android_gms_internal_zzmj.zzqJ()).zza(new zza(this));
            }
        });
    }
}
