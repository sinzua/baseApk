package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;

@zzhb
public abstract class zzhl {
    public abstract void zza(Context context, zzhf com_google_android_gms_internal_zzhf, VersionInfoParcel versionInfoParcel);

    protected void zze(zzhf com_google_android_gms_internal_zzhf) {
        com_google_android_gms_internal_zzhf.zzgD();
        if (com_google_android_gms_internal_zzhf.zzgB() != null) {
            com_google_android_gms_internal_zzhf.zzgB().release();
        }
    }
}
