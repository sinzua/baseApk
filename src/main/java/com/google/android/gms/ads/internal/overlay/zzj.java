package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.Nullable;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzjp;
import com.google.android.gms.internal.zzne;

public abstract class zzj {
    @Nullable
    public abstract zzi zza(Context context, zzjp com_google_android_gms_internal_zzjp, int i, zzcb com_google_android_gms_internal_zzcb, zzbz com_google_android_gms_internal_zzbz);

    protected boolean zzx(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return zzne.zzsg() && (applicationInfo == null || applicationInfo.targetSdkVersion >= 11);
    }
}
