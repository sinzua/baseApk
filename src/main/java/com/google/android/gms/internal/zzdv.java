package com.google.android.gms.internal;

import android.content.Context;
import android.content.MutableContextWrapper;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzk;

@zzhb
public class zzdv {
    private MutableContextWrapper zzAb;
    private final VersionInfoParcel zzpT;
    private final zzd zzpm;
    private final zzex zzpn;

    zzdv(Context context, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        this.zzAb = new MutableContextWrapper(context.getApplicationContext());
        this.zzpn = com_google_android_gms_internal_zzex;
        this.zzpT = versionInfoParcel;
        this.zzpm = com_google_android_gms_ads_internal_zzd;
    }

    public zzk zzX(String str) {
        return new zzk(this.zzAb, new AdSizeParcel(), str, this.zzpn, this.zzpT, this.zzpm);
    }

    public zzdv zzec() {
        return new zzdv(this.zzAb.getBaseContext(), this.zzpn, this.zzpT, this.zzpm);
    }

    public MutableContextWrapper zzed() {
        return this.zzAb;
    }
}
