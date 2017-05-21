package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.zzr;
import java.util.WeakHashMap;

@zzhb
public final class zzhk {
    private WeakHashMap<Context, zza> zzKm = new WeakHashMap();

    private class zza {
        public final long zzKn = zzr.zzbG().currentTimeMillis();
        public final zzhj zzKo;
        final /* synthetic */ zzhk zzKp;

        public zza(zzhk com_google_android_gms_internal_zzhk, zzhj com_google_android_gms_internal_zzhj) {
            this.zzKp = com_google_android_gms_internal_zzhk;
            this.zzKo = com_google_android_gms_internal_zzhj;
        }

        public boolean hasExpired() {
            return ((Long) zzbt.zzwM.get()).longValue() + this.zzKn < zzr.zzbG().currentTimeMillis();
        }
    }

    public zzhj zzE(Context context) {
        zza com_google_android_gms_internal_zzhk_zza = (zza) this.zzKm.get(context);
        zzhj zzgI = (com_google_android_gms_internal_zzhk_zza == null || com_google_android_gms_internal_zzhk_zza.hasExpired() || !((Boolean) zzbt.zzwL.get()).booleanValue()) ? new com.google.android.gms.internal.zzhj.zza(context).zzgI() : new com.google.android.gms.internal.zzhj.zza(context, com_google_android_gms_internal_zzhk_zza.zzKo).zzgI();
        this.zzKm.put(context, new zza(this, zzgI));
        return zzgI;
    }
}
