package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import java.util.concurrent.Callable;

@zzhb
public class zzbs {
    private final Object zzpV = new Object();
    private boolean zzqA = false;
    private SharedPreferences zzvx = null;

    public void initialize(Context context) {
        synchronized (this.zzpV) {
            if (this.zzqA) {
                return;
            }
            Context remoteContext = zze.getRemoteContext(context);
            if (remoteContext == null) {
                return;
            }
            this.zzvx = zzr.zzbJ().zzw(remoteContext);
            this.zzqA = true;
        }
    }

    public <T> T zzd(final zzbp<T> com_google_android_gms_internal_zzbp_T) {
        synchronized (this.zzpV) {
            if (this.zzqA) {
                return zzjb.zzb(new Callable<T>(this) {
                    final /* synthetic */ zzbs zzvz;

                    public T call() {
                        return com_google_android_gms_internal_zzbp_T.zza(this.zzvz.zzvx);
                    }
                });
            }
            T zzdq = com_google_android_gms_internal_zzbp_T.zzdq();
            return zzdq;
        }
    }
}
