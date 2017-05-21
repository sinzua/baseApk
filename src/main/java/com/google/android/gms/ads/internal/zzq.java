package com.google.android.gms.ads.internal;

import android.os.Handler;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzir;
import java.lang.ref.WeakReference;

@zzhb
public class zzq {
    private final zza zzqG;
    private AdRequestParcel zzqH;
    private boolean zzqI;
    private boolean zzqJ;
    private long zzqK;
    private final Runnable zzx;

    public static class zza {
        private final Handler mHandler;

        public zza(Handler handler) {
            this.mHandler = handler;
        }

        public boolean postDelayed(Runnable runnable, long timeFromNowInMillis) {
            return this.mHandler.postDelayed(runnable, timeFromNowInMillis);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public zzq(zza com_google_android_gms_ads_internal_zza) {
        this(com_google_android_gms_ads_internal_zza, new zza(zzir.zzMc));
    }

    zzq(zza com_google_android_gms_ads_internal_zza, zza com_google_android_gms_ads_internal_zzq_zza) {
        this.zzqI = false;
        this.zzqJ = false;
        this.zzqK = 0;
        this.zzqG = com_google_android_gms_ads_internal_zzq_zza;
        final WeakReference weakReference = new WeakReference(com_google_android_gms_ads_internal_zza);
        this.zzx = new Runnable(this) {
            final /* synthetic */ zzq zzqM;

            public void run() {
                this.zzqM.zzqI = false;
                zza com_google_android_gms_ads_internal_zza = (zza) weakReference.get();
                if (com_google_android_gms_ads_internal_zza != null) {
                    com_google_android_gms_ads_internal_zza.zzd(this.zzqM.zzqH);
                }
            }
        };
    }

    public void cancel() {
        this.zzqI = false;
        this.zzqG.removeCallbacks(this.zzx);
    }

    public void pause() {
        this.zzqJ = true;
        if (this.zzqI) {
            this.zzqG.removeCallbacks(this.zzx);
        }
    }

    public void resume() {
        this.zzqJ = false;
        if (this.zzqI) {
            this.zzqI = false;
            zza(this.zzqH, this.zzqK);
        }
    }

    public void zza(AdRequestParcel adRequestParcel, long j) {
        if (this.zzqI) {
            zzb.zzaK("An ad refresh is already scheduled.");
            return;
        }
        this.zzqH = adRequestParcel;
        this.zzqI = true;
        this.zzqK = j;
        if (!this.zzqJ) {
            zzb.zzaJ("Scheduling ad refresh " + j + " milliseconds from now.");
            this.zzqG.postDelayed(this.zzx, j);
        }
    }

    public boolean zzbw() {
        return this.zzqI;
    }

    public void zzg(AdRequestParcel adRequestParcel) {
        zza(adRequestParcel, 60000);
    }
}
