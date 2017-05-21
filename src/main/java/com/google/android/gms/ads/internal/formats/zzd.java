package com.google.android.gms.ads.internal.formats;

import android.os.Bundle;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzcl.zza;
import com.google.android.gms.internal.zzhb;
import java.util.List;

@zzhb
public class zzd extends zza implements zzh.zza {
    private Bundle mExtras;
    private Object zzpV = new Object();
    private String zzxW;
    private List<zzc> zzxX;
    private String zzxY;
    private zzch zzxZ;
    private String zzya;
    private double zzyb;
    private String zzyc;
    private String zzyd;
    private zza zzye;
    private zzh zzyf;

    public zzd(String str, List list, String str2, zzch com_google_android_gms_internal_zzch, String str3, double d, String str4, String str5, zza com_google_android_gms_ads_internal_formats_zza, Bundle bundle) {
        this.zzxW = str;
        this.zzxX = list;
        this.zzxY = str2;
        this.zzxZ = com_google_android_gms_internal_zzch;
        this.zzya = str3;
        this.zzyb = d;
        this.zzyc = str4;
        this.zzyd = str5;
        this.zzye = com_google_android_gms_ads_internal_formats_zza;
        this.mExtras = bundle;
    }

    public void destroy() {
        this.zzxW = null;
        this.zzxX = null;
        this.zzxY = null;
        this.zzxZ = null;
        this.zzya = null;
        this.zzyb = 0.0d;
        this.zzyc = null;
        this.zzyd = null;
        this.zzye = null;
        this.mExtras = null;
        this.zzpV = null;
        this.zzyf = null;
    }

    public String getBody() {
        return this.zzxY;
    }

    public String getCallToAction() {
        return this.zzya;
    }

    public String getCustomTemplateId() {
        return "";
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String getHeadline() {
        return this.zzxW;
    }

    public List getImages() {
        return this.zzxX;
    }

    public String getPrice() {
        return this.zzyd;
    }

    public double getStarRating() {
        return this.zzyb;
    }

    public String getStore() {
        return this.zzyc;
    }

    public void zzb(zzh com_google_android_gms_ads_internal_formats_zzh) {
        synchronized (this.zzpV) {
            this.zzyf = com_google_android_gms_ads_internal_formats_zzh;
        }
    }

    public zzch zzdK() {
        return this.zzxZ;
    }

    public com.google.android.gms.dynamic.zzd zzdL() {
        return zze.zzC(this.zzyf);
    }

    public String zzdM() {
        return "2";
    }

    public zza zzdN() {
        return this.zzye;
    }
}
