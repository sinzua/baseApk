package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzen {
    public final String zzBA;
    public final List<String> zzBB;
    public final String zzBC;
    public final String zzBD;
    public final List<String> zzBE;
    public final List<String> zzBF;
    public final String zzBG;
    public final List<String> zzBH;
    public final List<String> zzBI;
    public final String zzBJ;
    public final String zzBK;
    public final String zzBL;
    public final List<String> zzBM;
    public final String zzBN;
    public final String zzBz;

    public zzen(String str, String str2, List<String> list, String str3, String str4, List<String> list2, List<String> list3, String str5, String str6, List<String> list4, List<String> list5, String str7, String str8, String str9, List<String> list6, String str10) {
        this.zzBz = str;
        this.zzBA = str2;
        this.zzBB = list;
        this.zzBC = str3;
        this.zzBD = str4;
        this.zzBE = list2;
        this.zzBF = list3;
        this.zzBG = str5;
        this.zzBH = list4;
        this.zzBI = list5;
        this.zzBJ = str7;
        this.zzBK = str8;
        this.zzBL = str9;
        this.zzBM = list6;
        this.zzBN = str10;
    }

    public zzen(JSONObject jSONObject) throws JSONException {
        String str = null;
        this.zzBA = jSONObject.getString(CalendarEntryData.ID);
        JSONArray jSONArray = jSONObject.getJSONArray("adapters");
        List arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(jSONArray.getString(i));
        }
        this.zzBB = Collections.unmodifiableList(arrayList);
        this.zzBC = jSONObject.optString("allocation_id", null);
        this.zzBE = zzr.zzbP().zza(jSONObject, "clickurl");
        this.zzBF = zzr.zzbP().zza(jSONObject, "imp_urls");
        this.zzBH = zzr.zzbP().zza(jSONObject, "video_start_urls");
        this.zzBI = zzr.zzbP().zza(jSONObject, "video_complete_urls");
        JSONObject optJSONObject = jSONObject.optJSONObject("ad");
        this.zzBz = optJSONObject != null ? optJSONObject.toString() : null;
        JSONObject optJSONObject2 = jSONObject.optJSONObject("data");
        this.zzBG = optJSONObject2 != null ? optJSONObject2.toString() : null;
        this.zzBD = optJSONObject2 != null ? optJSONObject2.optString("class_name") : null;
        this.zzBJ = jSONObject.optString("html_template", null);
        this.zzBK = jSONObject.optString("ad_base_url", null);
        optJSONObject = jSONObject.optJSONObject("assets");
        this.zzBL = optJSONObject != null ? optJSONObject.toString() : null;
        this.zzBM = zzr.zzbP().zza(jSONObject, "template_ids");
        optJSONObject = jSONObject.optJSONObject("ad_loader_options");
        if (optJSONObject != null) {
            str = optJSONObject.toString();
        }
        this.zzBN = str;
    }
}
