package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.overlay.zzm;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzde {
    public static final zzdf zzyX = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        }
    };
    public static final zzdf zzyY = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            String str = (String) map.get("urls");
            if (TextUtils.isEmpty(str)) {
                zzb.zzaK("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] split = str.split(",");
            Map hashMap = new HashMap();
            PackageManager packageManager = com_google_android_gms_internal_zzjp.getContext().getPackageManager();
            for (String str2 : split) {
                String[] split2 = str2.split(";", 2);
                hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(split2[0].trim())), 65536) != null));
            }
            com_google_android_gms_internal_zzjp.zza("openableURLs", hashMap);
        }
    };
    public static final zzdf zzyZ = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            PackageManager packageManager = com_google_android_gms_internal_zzjp.getContext().getPackageManager();
            try {
                try {
                    JSONArray jSONArray = new JSONObject((String) map.get("data")).getJSONArray("intents");
                    JSONObject jSONObject = new JSONObject();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            String optString = jSONObject2.optString(CalendarEntryData.ID);
                            Object optString2 = jSONObject2.optString("u");
                            Object optString3 = jSONObject2.optString("i");
                            Object optString4 = jSONObject2.optString("m");
                            Object optString5 = jSONObject2.optString("p");
                            Object optString6 = jSONObject2.optString("c");
                            jSONObject2.optString("f");
                            jSONObject2.optString("e");
                            Intent intent = new Intent();
                            if (!TextUtils.isEmpty(optString2)) {
                                intent.setData(Uri.parse(optString2));
                            }
                            if (!TextUtils.isEmpty(optString3)) {
                                intent.setAction(optString3);
                            }
                            if (!TextUtils.isEmpty(optString4)) {
                                intent.setType(optString4);
                            }
                            if (!TextUtils.isEmpty(optString5)) {
                                intent.setPackage(optString5);
                            }
                            if (!TextUtils.isEmpty(optString6)) {
                                String[] split = optString6.split("/", 2);
                                if (split.length == 2) {
                                    intent.setComponent(new ComponentName(split[0], split[1]));
                                }
                            }
                            try {
                                jSONObject.put(optString, packageManager.resolveActivity(intent, 65536) != null);
                            } catch (Throwable e) {
                                zzb.zzb("Error constructing openable urls response.", e);
                            }
                        } catch (Throwable e2) {
                            zzb.zzb("Error parsing the intent data.", e2);
                        }
                    }
                    com_google_android_gms_internal_zzjp.zzb("openableIntents", jSONObject);
                } catch (JSONException e3) {
                    com_google_android_gms_internal_zzjp.zzb("openableIntents", new JSONObject());
                }
            } catch (JSONException e4) {
                com_google_android_gms_internal_zzjp.zzb("openableIntents", new JSONObject());
            }
        }
    };
    public static final zzdf zzza = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaK("URL missing from click GMSG.");
                return;
            }
            Uri zza;
            Uri parse = Uri.parse(str);
            try {
                zzan zzhW = com_google_android_gms_internal_zzjp.zzhW();
                if (zzhW != null && zzhW.zzb(parse)) {
                    zza = zzhW.zza(parse, com_google_android_gms_internal_zzjp.getContext());
                    new zziy(com_google_android_gms_internal_zzjp.getContext(), com_google_android_gms_internal_zzjp.zzhX().afmaVersion, zza.toString()).zzhn();
                }
            } catch (zzao e) {
                zzb.zzaK("Unable to append parameter to URL: " + str);
            }
            zza = parse;
            new zziy(com_google_android_gms_internal_zzjp.getContext(), com_google_android_gms_internal_zzjp.zzhX().afmaVersion, zza.toString()).zzhn();
        }
    };
    public static final zzdf zzzb = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            zzd zzhS = com_google_android_gms_internal_zzjp.zzhS();
            if (zzhS != null) {
                zzhS.close();
                return;
            }
            zzhS = com_google_android_gms_internal_zzjp.zzhT();
            if (zzhS != null) {
                zzhS.close();
            } else {
                zzb.zzaK("A GMSG tried to close something that wasn't an overlay.");
            }
        }
    };
    public static final zzdf zzzc = new zzdf() {
        private void zzc(zzjp com_google_android_gms_internal_zzjp) {
            zzb.zzaJ("Received support message, responding.");
            boolean z = false;
            com.google.android.gms.ads.internal.zzd zzhR = com_google_android_gms_internal_zzjp.zzhR();
            if (zzhR != null) {
                zzm com_google_android_gms_ads_internal_overlay_zzm = zzhR.zzpy;
                if (com_google_android_gms_ads_internal_overlay_zzm != null) {
                    z = com_google_android_gms_ads_internal_overlay_zzm.zzfM();
                }
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("event", "checkSupport");
                jSONObject.put("supports", z);
                com_google_android_gms_internal_zzjp.zzb("appStreaming", jSONObject);
            } catch (Throwable th) {
            }
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if ("checkSupport".equals(map.get(ParametersKeys.ACTION))) {
                zzc(com_google_android_gms_internal_zzjp);
                return;
            }
            zzd zzhS = com_google_android_gms_internal_zzjp.zzhS();
            if (zzhS != null) {
                zzhS.zzg(com_google_android_gms_internal_zzjp, map);
            }
        }
    };
    public static final zzdf zzzd = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            com_google_android_gms_internal_zzjp.zzE("1".equals(map.get("custom_close")));
        }
    };
    public static final zzdf zzze = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaK("URL missing from httpTrack GMSG.");
            } else {
                new zziy(com_google_android_gms_internal_zzjp.getContext(), com_google_android_gms_internal_zzjp.zzhX().afmaVersion, str).zzhn();
            }
        }
    };
    public static final zzdf zzzf = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            zzb.zzaJ("Received log message: " + ((String) map.get("string")));
        }
    };
    public static final zzdf zzzg = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            String str = (String) map.get("ty");
            String str2 = (String) map.get("td");
            try {
                int parseInt = Integer.parseInt((String) map.get("tx"));
                int parseInt2 = Integer.parseInt(str);
                int parseInt3 = Integer.parseInt(str2);
                zzan zzhW = com_google_android_gms_internal_zzjp.zzhW();
                if (zzhW != null) {
                    zzhW.zzab().zza(parseInt, parseInt2, parseInt3);
                }
            } catch (NumberFormatException e) {
                zzb.zzaK("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final zzdf zzzh = new zzdf() {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if (((Boolean) zzbt.zzwT.get()).booleanValue()) {
                com_google_android_gms_internal_zzjp.zzF(!Boolean.parseBoolean((String) map.get("disabled")));
            }
        }
    };
    public static final zzdf zzzi = new zzdo();
    public static final zzdf zzzj = new zzds();
    public static final zzdf zzzk = new zzdd();
}
