package com.google.android.gms.internal;

import android.content.MutableContextWrapper;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;
import java.util.LinkedList;

@zzhb
class zzea {
    private final LinkedList<zza> zzAB = new LinkedList();
    private final int zzAC;
    private final String zzpS;
    private AdRequestParcel zzqH;

    class zza {
        zzk zzAD;
        zzdw zzAE = new zzdw();
        long zzAF;
        boolean zzAG;
        boolean zzAH;
        final /* synthetic */ zzea zzAI;
        MutableContextWrapper zzAb;

        zza(zzea com_google_android_gms_internal_zzea, zzdv com_google_android_gms_internal_zzdv) {
            this.zzAI = com_google_android_gms_internal_zzea;
            zzdv zzec = com_google_android_gms_internal_zzdv.zzec();
            this.zzAb = com_google_android_gms_internal_zzdv.zzed();
            this.zzAD = zzec.zzX(com_google_android_gms_internal_zzea.zzpS);
            this.zzAE.zzc(this.zzAD);
        }

        private void zzek() {
            if (!this.zzAG && this.zzAI.zzqH != null) {
                this.zzAH = this.zzAD.zzb(this.zzAI.zzqH);
                this.zzAG = true;
                this.zzAF = zzr.zzbG().currentTimeMillis();
            }
        }

        void zzc(zzdv com_google_android_gms_internal_zzdv) {
            this.zzAb.setBaseContext(com_google_android_gms_internal_zzdv.zzed().getBaseContext());
        }

        void zzh(AdRequestParcel adRequestParcel) {
            if (adRequestParcel != null) {
                this.zzAI.zzqH = adRequestParcel;
            }
            zzek();
            Iterator it = this.zzAI.zzAB.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzek();
            }
        }
    }

    zzea(AdRequestParcel adRequestParcel, String str, int i) {
        zzx.zzz(adRequestParcel);
        zzx.zzz(str);
        this.zzqH = adRequestParcel;
        this.zzpS = str;
        this.zzAC = i;
    }

    String getAdUnitId() {
        return this.zzpS;
    }

    int getNetworkType() {
        return this.zzAC;
    }

    int size() {
        return this.zzAB.size();
    }

    void zzb(zzdv com_google_android_gms_internal_zzdv) {
        zza com_google_android_gms_internal_zzea_zza = new zza(this, com_google_android_gms_internal_zzdv);
        this.zzAB.add(com_google_android_gms_internal_zzea_zza);
        com_google_android_gms_internal_zzea_zza.zzh(this.zzqH);
    }

    AdRequestParcel zzei() {
        return this.zzqH;
    }

    zza zzej() {
        return (zza) this.zzAB.remove();
    }
}
