package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.util.Map;

@zzhb
public class zzds implements zzdf {
    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        zzdq zzbR = zzr.zzbR();
        if (!map.containsKey("abort")) {
            String str = (String) map.get("src");
            if (str == null) {
                zzb.zzaK("Precache video action is missing the src parameter.");
                return;
            }
            int parseInt;
            try {
                parseInt = Integer.parseInt((String) map.get("player"));
            } catch (NumberFormatException e) {
                parseInt = 0;
            }
            String str2 = map.containsKey("mimetype") ? (String) map.get("mimetype") : "";
            if (zzbR.zze(com_google_android_gms_internal_zzjp)) {
                zzb.zzaK("Precache task already running.");
                return;
            }
            com.google.android.gms.common.internal.zzb.zzv(com_google_android_gms_internal_zzjp.zzhR());
            new zzdp(com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzjp.zzhR().zzpw.zza(com_google_android_gms_internal_zzjp, parseInt, str2), str).zzhn();
        } else if (!zzbR.zzd(com_google_android_gms_internal_zzjp)) {
            zzb.zzaK("Precache abort but no preload task running.");
        }
    }
}
