package com.google.android.gms.internal;

import android.text.TextUtils;

@zzhb
public class zzbw {
    public zzbv zza(zzbu com_google_android_gms_internal_zzbu) {
        if (com_google_android_gms_internal_zzbu == null) {
            throw new IllegalArgumentException("CSI configuration can't be null. ");
        } else if (!com_google_android_gms_internal_zzbu.zzdu()) {
            zzin.v("CsiReporterFactory: CSI is not enabled. No CSI reporter created.");
            return null;
        } else if (com_google_android_gms_internal_zzbu.getContext() == null) {
            throw new IllegalArgumentException("Context can't be null. Please set up context in CsiConfiguration.");
        } else if (!TextUtils.isEmpty(com_google_android_gms_internal_zzbu.zzcs())) {
            return new zzbv(com_google_android_gms_internal_zzbu.getContext(), com_google_android_gms_internal_zzbu.zzcs(), com_google_android_gms_internal_zzbu.zzdv(), com_google_android_gms_internal_zzbu.zzdw());
        } else {
            throw new IllegalArgumentException("AfmaVersion can't be null or empty. Please set up afmaVersion in CsiConfiguration.");
        }
    }
}
