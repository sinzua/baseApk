package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.zzr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzet {
    public List<String> zza(JSONObject jSONObject, String str) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        List arrayList = new ArrayList(optJSONArray.length());
        for (int i = 0; i < optJSONArray.length(); i++) {
            arrayList.add(optJSONArray.getString(i));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void zza(Context context, String str, zzif com_google_android_gms_internal_zzif, String str2, boolean z, List<String> list) {
        if (list != null && !list.isEmpty()) {
            String str3 = z ? "1" : "0";
            for (String replaceAll : list) {
                String replaceAll2 = replaceAll2.replaceAll("@gw_adlocid@", str2).replaceAll("@gw_adnetrefresh@", str3).replaceAll("@gw_qdata@", com_google_android_gms_internal_zzif.zzKV.zzBT).replaceAll("@gw_sdkver@", str).replaceAll("@gw_sessid@", zzr.zzbF().getSessionId()).replaceAll("@gw_seqnum@", com_google_android_gms_internal_zzif.zzHw);
                if (com_google_android_gms_internal_zzif.zzCp != null) {
                    replaceAll2 = replaceAll2.replaceAll("@gw_adnetid@", com_google_android_gms_internal_zzif.zzCp.zzBA).replaceAll("@gw_allocid@", com_google_android_gms_internal_zzif.zzCp.zzBC);
                }
                new zziy(context, str, replaceAll2).zzhn();
            }
        }
    }
}
