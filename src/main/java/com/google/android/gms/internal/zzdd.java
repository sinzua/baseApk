package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Map;

@zzhb
public final class zzdd implements zzdf {
    private void zzb(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get("label");
        String str2 = (String) map.get("start_label");
        String str3 = (String) map.get("timestamp");
        if (TextUtils.isEmpty(str)) {
            zzb.zzaK("No label given for CSI tick.");
        } else if (TextUtils.isEmpty(str3)) {
            zzb.zzaK("No timestamp given for CSI tick.");
        } else {
            try {
                long zzc = zzc(Long.parseLong(str3));
                if (TextUtils.isEmpty(str2)) {
                    str2 = "native:view_load";
                }
                com_google_android_gms_internal_zzjp.zzic().zza(str, str2, zzc);
            } catch (Throwable e) {
                zzb.zzd("Malformed timestamp for CSI tick.", e);
            }
        }
    }

    private long zzc(long j) {
        return (j - zzr.zzbG().currentTimeMillis()) + zzr.zzbG().elapsedRealtime();
    }

    private void zzc(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get(ParametersKeys.VALUE);
        if (TextUtils.isEmpty(str)) {
            zzb.zzaK("No value given for CSI experiment.");
            return;
        }
        zzcb zzdA = com_google_android_gms_internal_zzjp.zzic().zzdA();
        if (zzdA == null) {
            zzb.zzaK("No ticker for WebView, dropping experiment ID.");
        } else {
            zzdA.zzc("e", str);
        }
    }

    private void zzd(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get("name");
        String str2 = (String) map.get(ParametersKeys.VALUE);
        if (TextUtils.isEmpty(str2)) {
            zzb.zzaK("No value given for CSI extra.");
        } else if (TextUtils.isEmpty(str)) {
            zzb.zzaK("No name given for CSI extra.");
        } else {
            zzcb zzdA = com_google_android_gms_internal_zzjp.zzic().zzdA();
            if (zzdA == null) {
                zzb.zzaK("No ticker for WebView, dropping extra parameter.");
            } else {
                zzdA.zzc(str, str2);
            }
        }
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get(ParametersKeys.ACTION);
        if ("tick".equals(str)) {
            zzb(com_google_android_gms_internal_zzjp, map);
        } else if ("experiment".equals(str)) {
            zzc(com_google_android_gms_internal_zzjp, map);
        } else if ("extra".equals(str)) {
            zzd(com_google_android_gms_internal_zzjp, map);
        }
    }
}
