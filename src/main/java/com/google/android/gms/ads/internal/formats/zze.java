package com.google.android.gms.ads.internal.formats;

import android.os.Bundle;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzcn.zza;
import com.google.android.gms.internal.zzhb;
import java.util.List;

@zzhb
public class zze extends zza implements zzh.zza {
    private Bundle mExtras;
    private Object zzpV = new Object();
    private String zzxW;
    private List<zzc> zzxX;
    private String zzxY;
    private String zzya;
    private zza zzye;
    private zzh zzyf;
    private zzch zzyg;
    private String zzyh;

    public zze(String str, List list, String str2, zzch com_google_android_gms_internal_zzch, String str3, String str4, zza com_google_android_gms_ads_internal_formats_zza, Bundle bundle) {
        this.zzxW = str;
        this.zzxX = list;
        this.zzxY = str2;
        this.zzyg = com_google_android_gms_internal_zzch;
        this.zzya = str3;
        this.zzyh = str4;
        this.zzye = com_google_android_gms_ads_internal_formats_zza;
        this.mExtras = bundle;
    }

    public void destroy() {
        this.zzxW = null;
        this.zzxX = null;
        this.zzxY = null;
        this.zzyg = null;
        this.zzya = null;
        this.zzyh = null;
        this.zzye = null;
        this.mExtras = null;
        this.zzpV = null;
        this.zzyf = null;
    }

    public String getAdvertiser() {
        return this.zzyh;
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

    public void zzb(zzh com_google_android_gms_ads_internal_formats_zzh) {
        synchronized (this.zzpV) {
            this.zzyf = com_google_android_gms_ads_internal_formats_zzh;
        }
    }

    public zzd zzdL() {
        return com.google.android.gms.dynamic.zze.zzC(this.zzyf);
    }

    public String zzdM() {
        return "1";
    }

    public zza zzdN() {
        return this.zzye;
    }

    public zzch zzdO() {
        return this.zzyg;
    }
}
