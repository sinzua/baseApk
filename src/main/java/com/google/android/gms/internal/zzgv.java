package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzgr.zza;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzhb
public class zzgv extends zzim {
    private final zzgw zzGC;
    private Future<zzif> zzGD;
    private final zza zzGc;
    private final zzif.zza zzGd;
    private final AdResponseParcel zzGe;
    private final Object zzpV;

    public zzgv(Context context, zzp com_google_android_gms_ads_internal_zzp, zzee com_google_android_gms_internal_zzee, zzif.zza com_google_android_gms_internal_zzif_zza, zzan com_google_android_gms_internal_zzan, zza com_google_android_gms_internal_zzgr_zza) {
        this(com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzgr_zza, new zzgw(context, com_google_android_gms_ads_internal_zzp, com_google_android_gms_internal_zzee, new zziw(context), com_google_android_gms_internal_zzan, com_google_android_gms_internal_zzif_zza));
    }

    zzgv(zzif.zza com_google_android_gms_internal_zzif_zza, zza com_google_android_gms_internal_zzgr_zza, zzgw com_google_android_gms_internal_zzgw) {
        this.zzpV = new Object();
        this.zzGd = com_google_android_gms_internal_zzif_zza;
        this.zzGe = com_google_android_gms_internal_zzif_zza.zzLe;
        this.zzGc = com_google_android_gms_internal_zzgr_zza;
        this.zzGC = com_google_android_gms_internal_zzgw;
    }

    private zzif zzE(int i) {
        return new zzif(this.zzGd.zzLd.zzHt, null, null, i, null, null, this.zzGe.orientation, this.zzGe.zzBU, this.zzGd.zzLd.zzHw, false, null, null, null, null, null, this.zzGe.zzHU, this.zzGd.zzrp, this.zzGe.zzHS, this.zzGd.zzKY, this.zzGe.zzHX, this.zzGe.zzHY, this.zzGd.zzKT, null, null, null, null, this.zzGd.zzLe.zzIm);
    }

    public void onStop() {
        synchronized (this.zzpV) {
            if (this.zzGD != null) {
                this.zzGD.cancel(true);
            }
        }
    }

    public void zzbr() {
        zzif com_google_android_gms_internal_zzif;
        int i;
        try {
            synchronized (this.zzpV) {
                this.zzGD = zziq.zza(this.zzGC);
            }
            com_google_android_gms_internal_zzif = (zzif) this.zzGD.get(60000, TimeUnit.MILLISECONDS);
            i = -2;
        } catch (TimeoutException e) {
            zzb.zzaK("Timed out waiting for native ad.");
            this.zzGD.cancel(true);
            i = 2;
            com_google_android_gms_internal_zzif = null;
        } catch (ExecutionException e2) {
            i = 0;
            com_google_android_gms_internal_zzif = null;
        } catch (InterruptedException e3) {
            com_google_android_gms_internal_zzif = null;
            i = -1;
        } catch (CancellationException e4) {
            com_google_android_gms_internal_zzif = null;
            i = -1;
        }
        if (com_google_android_gms_internal_zzif == null) {
            com_google_android_gms_internal_zzif = zzE(i);
        }
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzgv zzGE;

            public void run() {
                this.zzGE.zzGc.zzb(com_google_android_gms_internal_zzif);
            }
        });
    }
}
