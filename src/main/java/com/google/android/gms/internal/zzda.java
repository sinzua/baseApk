package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Map;

@zzhb
public final class zzda implements zzdf {
    private final zzdb zzyW;

    public zzda(zzdb com_google_android_gms_internal_zzdb) {
        this.zzyW = com_google_android_gms_internal_zzdb;
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get("name");
        if (str == null) {
            zzb.zzaK("App event with no name parameter.");
        } else {
            this.zzyW.onAppEvent(str, (String) map.get("info"));
        }
    }
}
