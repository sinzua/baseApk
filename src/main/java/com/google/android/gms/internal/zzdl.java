package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zze;
import java.util.Map;

@zzhb
public class zzdl implements zzdf {
    static final Map<String, Integer> zzzC = zzmr.zza("resize", Integer.valueOf(1), "playVideo", Integer.valueOf(2), "storePicture", Integer.valueOf(3), "createCalendarEvent", Integer.valueOf(4), "setOrientationProperties", Integer.valueOf(5), "closeResizedAd", Integer.valueOf(6));
    private final zze zzzA;
    private final zzfn zzzB;

    public zzdl(zze com_google_android_gms_ads_internal_zze, zzfn com_google_android_gms_internal_zzfn) {
        this.zzzA = com_google_android_gms_ads_internal_zze;
        this.zzzB = com_google_android_gms_internal_zzfn;
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        int intValue = ((Integer) zzzC.get((String) map.get("a"))).intValue();
        if (intValue == 5 || this.zzzA == null || this.zzzA.zzbh()) {
            switch (intValue) {
                case 1:
                    this.zzzB.zzi(map);
                    return;
                case 3:
                    new zzfp(com_google_android_gms_internal_zzjp, map).execute();
                    return;
                case 4:
                    new zzfm(com_google_android_gms_internal_zzjp, map).execute();
                    return;
                case 5:
                    new zzfo(com_google_android_gms_internal_zzjp, map).execute();
                    return;
                case 6:
                    this.zzzB.zzp(true);
                    return;
                default:
                    zzb.zzaJ("Unknown MRAID command called.");
                    return;
            }
        }
        this.zzzA.zzq(null);
    }
}
