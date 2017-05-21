package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzr;
import com.google.android.gms.ads.internal.client.zzs.zza;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzj extends zza {
    private final Context mContext;
    private zzq zzpK;
    private NativeAdOptionsParcel zzpP;
    private zzx zzpR;
    private final String zzpS;
    private final VersionInfoParcel zzpT;
    private zzcr zzpY;
    private zzcs zzpZ;
    private final zzd zzpm;
    private final zzex zzpn;
    private SimpleArrayMap<String, zzct> zzqa = new SimpleArrayMap();
    private SimpleArrayMap<String, zzcu> zzqb = new SimpleArrayMap();

    public zzj(Context context, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        this.mContext = context;
        this.zzpS = str;
        this.zzpn = com_google_android_gms_internal_zzex;
        this.zzpT = versionInfoParcel;
        this.zzpm = com_google_android_gms_ads_internal_zzd;
    }

    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) {
        this.zzpP = nativeAdOptionsParcel;
    }

    public void zza(zzcr com_google_android_gms_internal_zzcr) {
        this.zzpY = com_google_android_gms_internal_zzcr;
    }

    public void zza(zzcs com_google_android_gms_internal_zzcs) {
        this.zzpZ = com_google_android_gms_internal_zzcs;
    }

    public void zza(String str, zzcu com_google_android_gms_internal_zzcu, zzct com_google_android_gms_internal_zzct) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Custom template ID for native custom template ad is empty. Please provide a valid template id.");
        }
        this.zzqb.put(str, com_google_android_gms_internal_zzcu);
        this.zzqa.put(str, com_google_android_gms_internal_zzct);
    }

    public void zzb(zzq com_google_android_gms_ads_internal_client_zzq) {
        this.zzpK = com_google_android_gms_ads_internal_client_zzq;
    }

    public void zzb(zzx com_google_android_gms_ads_internal_client_zzx) {
        this.zzpR = com_google_android_gms_ads_internal_client_zzx;
    }

    public zzr zzbn() {
        return new zzi(this.mContext, this.zzpS, this.zzpn, this.zzpT, this.zzpK, this.zzpY, this.zzpZ, this.zzqb, this.zzqa, this.zzpP, this.zzpR, this.zzpm);
    }
}
