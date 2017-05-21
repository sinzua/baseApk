package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zze;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.internal.zzgw.zza;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzgy implements zza<zze> {
    private final boolean zzHc;
    private final boolean zzHd;

    public zzgy(boolean z, boolean z2) {
        this.zzHc = z;
        this.zzHd = z2;
    }

    public /* synthetic */ zzh.zza zza(zzgw com_google_android_gms_internal_zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        return zzc(com_google_android_gms_internal_zzgw, jSONObject);
    }

    public zze zzc(zzgw com_google_android_gms_internal_zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        List<zzjg> zza = com_google_android_gms_internal_zzgw.zza(jSONObject, "images", true, this.zzHc, this.zzHd);
        Future zza2 = com_google_android_gms_internal_zzgw.zza(jSONObject, "secondary_image", false, this.zzHc);
        Future zzf = com_google_android_gms_internal_zzgw.zzf(jSONObject);
        List arrayList = new ArrayList();
        for (zzjg com_google_android_gms_internal_zzjg : zza) {
            arrayList.add(com_google_android_gms_internal_zzjg.get());
        }
        return new zze(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), (zzch) zza2.get(), jSONObject.getString("call_to_action"), jSONObject.getString("advertiser"), (com.google.android.gms.ads.internal.formats.zza) zzf.get(), new Bundle());
    }
}
