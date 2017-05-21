package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzeo {
    public final List<zzen> zzBO;
    public final long zzBP;
    public final List<String> zzBQ;
    public final List<String> zzBR;
    public final List<String> zzBS;
    public final String zzBT;
    public final long zzBU;
    public final String zzBV;
    public final int zzBW;
    public final int zzBX;
    public final long zzBY;
    public int zzBZ;
    public int zzCa;

    public zzeo(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (zzb.zzQ(2)) {
            zzin.v("Mediation Response JSON: " + jSONObject.toString(2));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        List arrayList = new ArrayList(jSONArray.length());
        int i = -1;
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            zzen com_google_android_gms_internal_zzen = new zzen(jSONArray.getJSONObject(i2));
            arrayList.add(com_google_android_gms_internal_zzen);
            if (i < 0 && zza(com_google_android_gms_internal_zzen)) {
                i = i2;
            }
        }
        this.zzBZ = i;
        this.zzCa = jSONArray.length();
        this.zzBO = Collections.unmodifiableList(arrayList);
        this.zzBT = jSONObject.getString("qdata");
        this.zzBX = jSONObject.optInt("fs_model_type", -1);
        this.zzBY = jSONObject.optLong("timeout_ms", -1);
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject != null) {
            this.zzBP = optJSONObject.optLong("ad_network_timeout_millis", -1);
            this.zzBQ = zzr.zzbP().zza(optJSONObject, "click_urls");
            this.zzBR = zzr.zzbP().zza(optJSONObject, "imp_urls");
            this.zzBS = zzr.zzbP().zza(optJSONObject, "nofill_urls");
            long optLong = optJSONObject.optLong("refresh", -1);
            this.zzBU = optLong > 0 ? optLong * 1000 : -1;
            RewardItemParcel zza = RewardItemParcel.zza(optJSONObject.optJSONArray("rewards"));
            if (zza == null) {
                this.zzBV = null;
                this.zzBW = 0;
                return;
            }
            this.zzBV = zza.type;
            this.zzBW = zza.zzKS;
            return;
        }
        this.zzBP = -1;
        this.zzBQ = null;
        this.zzBR = null;
        this.zzBS = null;
        this.zzBU = -1;
        this.zzBV = null;
        this.zzBW = 0;
    }

    public zzeo(List<zzen> list, long j, List<String> list2, List<String> list3, List<String> list4, String str, long j2, int i, int i2, String str2, int i3, int i4, long j3) {
        this.zzBO = list;
        this.zzBP = j;
        this.zzBQ = list2;
        this.zzBR = list3;
        this.zzBS = list4;
        this.zzBT = str;
        this.zzBU = j2;
        this.zzBZ = i;
        this.zzCa = i2;
        this.zzBV = str2;
        this.zzBW = i3;
        this.zzBX = i4;
        this.zzBY = j3;
    }

    private boolean zza(zzen com_google_android_gms_internal_zzen) {
        for (String equals : com_google_android_gms_internal_zzen.zzBB) {
            if (equals.equals("com.google.ads.mediation.admob.AdMobAdapter")) {
                return true;
            }
        }
        return false;
    }
}
