package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zza;

public class zzg implements zzk {
    private final zzl zzahj;
    private boolean zzahk = false;

    public zzg(zzl com_google_android_gms_common_api_internal_zzl) {
        this.zzahj = com_google_android_gms_common_api_internal_zzl;
    }

    private <A extends zzb> void zza(zze<A> com_google_android_gms_common_api_internal_zzj_zze_A) throws DeadObjectException {
        this.zzahj.zzagW.zzb((zze) com_google_android_gms_common_api_internal_zzj_zze_A);
        zzb zza = this.zzahj.zzagW.zza(com_google_android_gms_common_api_internal_zzj_zze_A.zzoR());
        if (zza.isConnected() || !this.zzahj.zzaio.containsKey(com_google_android_gms_common_api_internal_zzj_zze_A.zzoR())) {
            com_google_android_gms_common_api_internal_zzj_zze_A.zzb(zza);
        } else {
            com_google_android_gms_common_api_internal_zzj_zze_A.zzw(new Status(17));
        }
    }

    public void begin() {
    }

    public void connect() {
        if (this.zzahk) {
            this.zzahk = false;
            this.zzahj.zza(new zza(this, this) {
                final /* synthetic */ zzg zzahl;

                public void zzpt() {
                    this.zzahl.zzahj.zzais.zzi(null);
                }
            });
        }
    }

    public boolean disconnect() {
        if (this.zzahk) {
            return false;
        }
        if (this.zzahj.zzagW.zzpG()) {
            this.zzahk = true;
            for (zzx zzpU : this.zzahj.zzagW.zzaia) {
                zzpU.zzpU();
            }
            return false;
        }
        this.zzahj.zzh(null);
        return true;
    }

    public void onConnected(Bundle connectionHint) {
    }

    public void onConnectionSuspended(int cause) {
        this.zzahj.zzh(null);
        this.zzahj.zzais.zzc(cause, this.zzahk);
    }

    public <A extends zzb, R extends Result, T extends zza<R, A>> T zza(T t) {
        return zzb(t);
    }

    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    public <A extends zzb, T extends zza<? extends Result, A>> T zzb(T t) {
        try {
            zza((zze) t);
        } catch (DeadObjectException e) {
            this.zzahj.zza(new zza(this, this) {
                final /* synthetic */ zzg zzahl;

                public void zzpt() {
                    this.zzahl.onConnectionSuspended(1);
                }
            });
        }
        return t;
    }

    void zzps() {
        if (this.zzahk) {
            this.zzahk = false;
            this.zzahj.zzagW.zzaa(false);
            disconnect();
        }
    }
}
