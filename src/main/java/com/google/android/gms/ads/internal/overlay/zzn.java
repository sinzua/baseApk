package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.support.annotation.Nullable;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;

@zzhb
public class zzn extends zzj {
    @Nullable
    public zzi zza(Context context, zzjp com_google_android_gms_internal_zzjp, int i, zzcb com_google_android_gms_internal_zzcb, zzbz com_google_android_gms_internal_zzbz) {
        if (!zzx(context)) {
            return null;
        }
        return new zzc(context, new zzt(context, com_google_android_gms_internal_zzjp.zzhX(), com_google_android_gms_internal_zzjp.getRequestId(), com_google_android_gms_internal_zzcb, com_google_android_gms_internal_zzbz));
    }
}
