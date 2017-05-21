package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import org.json.JSONObject;

@zzhb
public class zzdk implements zzdf {
    final HashMap<String, zzjd<JSONObject>> zzzz = new HashMap();

    public Future<JSONObject> zzR(String str) {
        Future com_google_android_gms_internal_zzjd = new zzjd();
        this.zzzz.put(str, com_google_android_gms_internal_zzjd);
        return com_google_android_gms_internal_zzjd;
    }

    public void zzS(String str) {
        zzjd com_google_android_gms_internal_zzjd = (zzjd) this.zzzz.get(str);
        if (com_google_android_gms_internal_zzjd == null) {
            zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        if (!com_google_android_gms_internal_zzjd.isDone()) {
            com_google_android_gms_internal_zzjd.cancel(true);
        }
        this.zzzz.remove(str);
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        zzd((String) map.get("request_id"), (String) map.get("fetched_ad"));
    }

    public void zzd(String str, String str2) {
        zzb.zzaI("Received ad from the cache.");
        zzjd com_google_android_gms_internal_zzjd = (zzjd) this.zzzz.get(str);
        if (com_google_android_gms_internal_zzjd == null) {
            zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        try {
            com_google_android_gms_internal_zzjd.zzg(new JSONObject(str2));
        } catch (Throwable e) {
            zzb.zzb("Failed constructing JSON object from value passed from javascript", e);
            com_google_android_gms_internal_zzjd.zzg(null);
        } finally {
            this.zzzz.remove(str);
        }
    }
}
