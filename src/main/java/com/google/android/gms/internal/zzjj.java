package com.google.android.gms.internal;

import com.google.android.gms.internal.zzji.zzc;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@zzhb
public class zzjj<T> implements zzji<T> {
    protected int zzBc = 0;
    protected final BlockingQueue<zza> zzNq = new LinkedBlockingQueue();
    protected T zzNr;
    private final Object zzpV = new Object();

    class zza {
        public final zzc<T> zzNs;
        public final com.google.android.gms.internal.zzji.zza zzNt;
        final /* synthetic */ zzjj zzNu;

        public zza(zzjj com_google_android_gms_internal_zzjj, zzc<T> com_google_android_gms_internal_zzji_zzc_T, com.google.android.gms.internal.zzji.zza com_google_android_gms_internal_zzji_zza) {
            this.zzNu = com_google_android_gms_internal_zzjj;
            this.zzNs = com_google_android_gms_internal_zzji_zzc_T;
            this.zzNt = com_google_android_gms_internal_zzji_zza;
        }
    }

    public int getStatus() {
        return this.zzBc;
    }

    public void reject() {
        synchronized (this.zzpV) {
            if (this.zzBc != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzBc = -1;
            for (zza com_google_android_gms_internal_zzjj_zza : this.zzNq) {
                com_google_android_gms_internal_zzjj_zza.zzNt.run();
            }
            this.zzNq.clear();
        }
    }

    public void zza(zzc<T> com_google_android_gms_internal_zzji_zzc_T, com.google.android.gms.internal.zzji.zza com_google_android_gms_internal_zzji_zza) {
        synchronized (this.zzpV) {
            if (this.zzBc == 1) {
                com_google_android_gms_internal_zzji_zzc_T.zze(this.zzNr);
            } else if (this.zzBc == -1) {
                com_google_android_gms_internal_zzji_zza.run();
            } else if (this.zzBc == 0) {
                this.zzNq.add(new zza(this, com_google_android_gms_internal_zzji_zzc_T, com_google_android_gms_internal_zzji_zza));
            }
        }
    }

    public void zzh(T t) {
        synchronized (this.zzpV) {
            if (this.zzBc != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzNr = t;
            this.zzBc = 1;
            for (zza com_google_android_gms_internal_zzjj_zza : this.zzNq) {
                com_google_android_gms_internal_zzjj_zza.zzNs.zze(t);
            }
            this.zzNq.clear();
        }
    }
}
