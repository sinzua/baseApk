package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.reward.client.zzf;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzn {
    private static final Object zzqy = new Object();
    private static zzn zzur;
    private final zza zzus = new zza();
    private final zze zzut = new zze();
    private final zzl zzuu = new zzl();
    private final zzaf zzuv = new zzaf();
    private final zzcv zzuw = new zzcv();
    private final zzf zzux = new zzf();

    static {
        zza(new zzn());
    }

    protected zzn() {
    }

    protected static void zza(zzn com_google_android_gms_ads_internal_client_zzn) {
        synchronized (zzqy) {
            zzur = com_google_android_gms_ads_internal_client_zzn;
        }
    }

    private static zzn zzcR() {
        zzn com_google_android_gms_ads_internal_client_zzn;
        synchronized (zzqy) {
            com_google_android_gms_ads_internal_client_zzn = zzur;
        }
        return com_google_android_gms_ads_internal_client_zzn;
    }

    public static zza zzcS() {
        return zzcR().zzus;
    }

    public static zze zzcT() {
        return zzcR().zzut;
    }

    public static zzl zzcU() {
        return zzcR().zzuu;
    }

    public static zzaf zzcV() {
        return zzcR().zzuv;
    }

    public static zzcv zzcW() {
        return zzcR().zzuw;
    }

    public static zzf zzcX() {
        return zzcR().zzux;
    }
}
