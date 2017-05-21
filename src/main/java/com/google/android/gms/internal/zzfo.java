package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.nativex.monetization.mraid.objects.ObjectNames.OrientationProperties;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Map;

@zzhb
public class zzfo {
    private final boolean zzDp;
    private final String zzDq;
    private final zzjp zzpD;

    public zzfo(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzDq = (String) map.get(OrientationProperties.FORCE_ORIENTATION);
        if (map.containsKey(OrientationProperties.ALLOW_ORIENTATION_CHANGE)) {
            this.zzDp = Boolean.parseBoolean((String) map.get(OrientationProperties.ALLOW_ORIENTATION_CHANGE));
        } else {
            this.zzDp = true;
        }
    }

    public void execute() {
        if (this.zzpD == null) {
            zzb.zzaK("AdWebView is null");
            return;
        }
        int zzhw = ParametersKeys.ORIENTATION_PORTRAIT.equalsIgnoreCase(this.zzDq) ? zzr.zzbE().zzhw() : ParametersKeys.ORIENTATION_LANDSCAPE.equalsIgnoreCase(this.zzDq) ? zzr.zzbE().zzhv() : this.zzDp ? -1 : zzr.zzbE().zzhx();
        this.zzpD.setRequestedOrientation(zzhw);
    }
}
