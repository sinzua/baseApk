package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Map;

@zzhb
public class zzdi implements zzdf {
    private final zzdj zzzy;

    public zzdi(zzdj com_google_android_gms_internal_zzdj) {
        this.zzzy = com_google_android_gms_internal_zzdj;
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        float parseFloat;
        boolean equals = "1".equals(map.get("transparentBackground"));
        boolean equals2 = "1".equals(map.get("blur"));
        try {
            if (map.get("blurRadius") != null) {
                parseFloat = Float.parseFloat((String) map.get("blurRadius"));
                this.zzzy.zzd(equals);
                this.zzzy.zza(equals2, parseFloat);
            }
        } catch (Throwable e) {
            zzb.zzb("Fail to parse float", e);
        }
        parseFloat = 0.0f;
        this.zzzy.zzd(equals);
        this.zzzy.zza(equals2, parseFloat);
    }
}
