package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.SearchAdRequestParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhn.zza;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzhe {
    private static final SimpleDateFormat zzJg = new SimpleDateFormat("yyyyMMdd", Locale.US);

    private static String zzL(int i) {
        return String.format(Locale.US, "#%06x", new Object[]{Integer.valueOf(ViewCompat.MEASURED_SIZE_MASK & i)});
    }

    public static AdResponseParcel zza(Context context, AdRequestInfoParcel adRequestInfoParcel, String str) {
        try {
            String str2;
            long j;
            String optString;
            String str3;
            boolean optBoolean;
            JSONObject jSONObject = new JSONObject(str);
            String optString2 = jSONObject.optString("ad_base_url", null);
            Object optString3 = jSONObject.optString("ad_url", null);
            String optString4 = jSONObject.optString("ad_size", null);
            boolean z = (adRequestInfoParcel == null || adRequestInfoParcel.zzHz == 0) ? false : true;
            CharSequence optString5 = z ? jSONObject.optString("ad_json", null) : jSONObject.optString("ad_html", null);
            long j2 = -1;
            String optString6 = jSONObject.optString("debug_dialog", null);
            long j3 = jSONObject.has("interstitial_timeout") ? (long) (jSONObject.getDouble("interstitial_timeout") * 1000.0d) : -1;
            String optString7 = jSONObject.optString(ParametersKeys.ORIENTATION, null);
            int i = -1;
            if (ParametersKeys.ORIENTATION_PORTRAIT.equals(optString7)) {
                i = zzr.zzbE().zzhw();
            } else if (ParametersKeys.ORIENTATION_LANDSCAPE.equals(optString7)) {
                i = zzr.zzbE().zzhv();
            }
            AdResponseParcel adResponseParcel = null;
            if (TextUtils.isEmpty(optString5)) {
                if (TextUtils.isEmpty(optString3)) {
                    zzb.zzaK("Could not parse the mediation config: Missing required " + (z ? "ad_json" : "ad_html") + " or " + "ad_url" + " field.");
                    return new AdResponseParcel(0);
                }
                adResponseParcel = zzhd.zza(adRequestInfoParcel, context, adRequestInfoParcel.zzrl.afmaVersion, optString3, null, null, null, null, null);
                optString2 = adResponseParcel.zzEF;
                str2 = adResponseParcel.body;
                j2 = adResponseParcel.zzHX;
            } else if (TextUtils.isEmpty(optString2)) {
                zzb.zzaK("Could not parse the mediation config: Missing required ad_base_url field");
                return new AdResponseParcel(0);
            } else {
                CharSequence charSequence = optString5;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("click_urls");
            List list = adResponseParcel == null ? null : adResponseParcel.zzBQ;
            if (optJSONArray != null) {
                list = zza(optJSONArray, list);
            }
            optJSONArray = jSONObject.optJSONArray("impression_urls");
            List list2 = adResponseParcel == null ? null : adResponseParcel.zzBR;
            if (optJSONArray != null) {
                list2 = zza(optJSONArray, list2);
            }
            optJSONArray = jSONObject.optJSONArray("manual_impression_urls");
            List list3 = adResponseParcel == null ? null : adResponseParcel.zzHV;
            if (optJSONArray != null) {
                list3 = zza(optJSONArray, list3);
            }
            if (adResponseParcel != null) {
                if (adResponseParcel.orientation != -1) {
                    i = adResponseParcel.orientation;
                }
                if (adResponseParcel.zzHS > 0) {
                    j = adResponseParcel.zzHS;
                    optString = jSONObject.optString("active_view");
                    str3 = null;
                    optBoolean = jSONObject.optBoolean("ad_is_javascript", false);
                    if (optBoolean) {
                        str3 = jSONObject.optString("ad_passback_url", null);
                    }
                    return new AdResponseParcel(adRequestInfoParcel, optString2, str2, list, list2, j, jSONObject.optBoolean("mediation", false), jSONObject.optLong("mediation_config_cache_time_milliseconds", -1), list3, jSONObject.optLong("refresh_interval_milliseconds", -1), i, optString4, j2, optString6, optBoolean, str3, optString, jSONObject.optBoolean("custom_render_allowed", false), z, adRequestInfoParcel.zzHB, jSONObject.optBoolean("content_url_opted_out", true), jSONObject.optBoolean("prefetch", false), jSONObject.optInt("oauth2_token_status", 0), jSONObject.optString("gws_query_id", ""), "height".equals(jSONObject.optString("fluid", "")), jSONObject.optBoolean("native_express", false), RewardItemParcel.zza(jSONObject.optJSONArray("rewards")), zza(jSONObject.optJSONArray("video_start_urls"), null), zza(jSONObject.optJSONArray("video_complete_urls"), null), jSONObject.optBoolean("use_displayed_impression", false));
                }
            }
            j = j3;
            optString = jSONObject.optString("active_view");
            str3 = null;
            optBoolean = jSONObject.optBoolean("ad_is_javascript", false);
            if (optBoolean) {
                str3 = jSONObject.optString("ad_passback_url", null);
            }
            return new AdResponseParcel(adRequestInfoParcel, optString2, str2, list, list2, j, jSONObject.optBoolean("mediation", false), jSONObject.optLong("mediation_config_cache_time_milliseconds", -1), list3, jSONObject.optLong("refresh_interval_milliseconds", -1), i, optString4, j2, optString6, optBoolean, str3, optString, jSONObject.optBoolean("custom_render_allowed", false), z, adRequestInfoParcel.zzHB, jSONObject.optBoolean("content_url_opted_out", true), jSONObject.optBoolean("prefetch", false), jSONObject.optInt("oauth2_token_status", 0), jSONObject.optString("gws_query_id", ""), "height".equals(jSONObject.optString("fluid", "")), jSONObject.optBoolean("native_express", false), RewardItemParcel.zza(jSONObject.optJSONArray("rewards")), zza(jSONObject.optJSONArray("video_start_urls"), null), zza(jSONObject.optJSONArray("video_complete_urls"), null), jSONObject.optBoolean("use_displayed_impression", false));
        } catch (JSONException e) {
            zzb.zzaK("Could not parse the mediation config: " + e.getMessage());
            return new AdResponseParcel(0);
        }
    }

    @Nullable
    private static List<String> zza(@Nullable JSONArray jSONArray, @Nullable List<String> list) throws JSONException {
        if (jSONArray == null) {
            return null;
        }
        if (list == null) {
            list = new LinkedList();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            list.add(jSONArray.getString(i));
        }
        return list;
    }

    public static JSONObject zza(Context context, AdRequestInfoParcel adRequestInfoParcel, zzhj com_google_android_gms_internal_zzhj, zza com_google_android_gms_internal_zzhn_zza, Location location, zzbm com_google_android_gms_internal_zzbm, String str, String str2, List<String> list, Bundle bundle) {
        try {
            HashMap hashMap = new HashMap();
            if (list.size() > 0) {
                hashMap.put("eid", TextUtils.join(",", list));
            }
            if (adRequestInfoParcel.zzHs != null) {
                hashMap.put("ad_pos", adRequestInfoParcel.zzHs);
            }
            zza(hashMap, adRequestInfoParcel.zzHt);
            hashMap.put("format", adRequestInfoParcel.zzrp.zzuh);
            if (adRequestInfoParcel.zzrp.width == -1) {
                hashMap.put("smart_w", "full");
            }
            if (adRequestInfoParcel.zzrp.height == -2) {
                hashMap.put("smart_h", "auto");
            }
            if (adRequestInfoParcel.zzrp.zzul) {
                hashMap.put("fluid", "height");
            }
            if (adRequestInfoParcel.zzrp.zzuj != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzrp.zzuj) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("|");
                    }
                    stringBuilder.append(adSizeParcel.width == -1 ? (int) (((float) adSizeParcel.widthPixels) / com_google_android_gms_internal_zzhj.zzHF) : adSizeParcel.width);
                    stringBuilder.append("x");
                    stringBuilder.append(adSizeParcel.height == -2 ? (int) (((float) adSizeParcel.heightPixels) / com_google_android_gms_internal_zzhj.zzHF) : adSizeParcel.height);
                }
                hashMap.put("sz", stringBuilder);
            }
            if (adRequestInfoParcel.zzHz != 0) {
                hashMap.put("native_version", Integer.valueOf(adRequestInfoParcel.zzHz));
                if (!adRequestInfoParcel.zzrp.zzum) {
                    hashMap.put("native_templates", adRequestInfoParcel.zzrH);
                    hashMap.put("native_image_orientation", zzc(adRequestInfoParcel.zzrD));
                    if (!adRequestInfoParcel.zzHK.isEmpty()) {
                        hashMap.put("native_custom_templates", adRequestInfoParcel.zzHK);
                    }
                }
            }
            hashMap.put("slotname", adRequestInfoParcel.zzrj);
            hashMap.put("pn", adRequestInfoParcel.applicationInfo.packageName);
            if (adRequestInfoParcel.zzHu != null) {
                hashMap.put("vc", Integer.valueOf(adRequestInfoParcel.zzHu.versionCode));
            }
            hashMap.put("ms", str2);
            hashMap.put("seq_num", adRequestInfoParcel.zzHw);
            hashMap.put("session_id", adRequestInfoParcel.zzHx);
            hashMap.put("js", adRequestInfoParcel.zzrl.afmaVersion);
            zza(hashMap, com_google_android_gms_internal_zzhj, com_google_android_gms_internal_zzhn_zza);
            hashMap.put("platform", Build.MANUFACTURER);
            hashMap.put("submodel", Build.MODEL);
            if (adRequestInfoParcel.zzHt.versionCode >= 2 && adRequestInfoParcel.zzHt.zztK != null) {
                zza(hashMap, adRequestInfoParcel.zzHt.zztK);
            }
            if (adRequestInfoParcel.versionCode >= 2) {
                hashMap.put("quality_signals", adRequestInfoParcel.zzHy);
            }
            if (adRequestInfoParcel.versionCode >= 4 && adRequestInfoParcel.zzHB) {
                hashMap.put("forceHttps", Boolean.valueOf(adRequestInfoParcel.zzHB));
            }
            if (bundle != null) {
                hashMap.put("content_info", bundle);
            }
            if (adRequestInfoParcel.versionCode >= 5) {
                hashMap.put("u_sd", Float.valueOf(adRequestInfoParcel.zzHF));
                hashMap.put("sh", Integer.valueOf(adRequestInfoParcel.zzHE));
                hashMap.put("sw", Integer.valueOf(adRequestInfoParcel.zzHD));
            } else {
                hashMap.put("u_sd", Float.valueOf(com_google_android_gms_internal_zzhj.zzHF));
                hashMap.put("sh", Integer.valueOf(com_google_android_gms_internal_zzhj.zzHE));
                hashMap.put("sw", Integer.valueOf(com_google_android_gms_internal_zzhj.zzHD));
            }
            if (adRequestInfoParcel.versionCode >= 6) {
                if (!TextUtils.isEmpty(adRequestInfoParcel.zzHG)) {
                    try {
                        hashMap.put("view_hierarchy", new JSONObject(adRequestInfoParcel.zzHG));
                    } catch (Throwable e) {
                        zzb.zzd("Problem serializing view hierarchy to JSON", e);
                    }
                }
                hashMap.put("correlation_id", Long.valueOf(adRequestInfoParcel.zzHH));
            }
            if (adRequestInfoParcel.versionCode >= 7) {
                hashMap.put("request_id", adRequestInfoParcel.zzHI);
            }
            if (adRequestInfoParcel.versionCode >= 11 && adRequestInfoParcel.zzHM != null) {
                hashMap.put("capability", adRequestInfoParcel.zzHM.toBundle());
            }
            zza(hashMap, str);
            if (adRequestInfoParcel.versionCode >= 12 && !TextUtils.isEmpty(adRequestInfoParcel.zzHN)) {
                hashMap.put("anchor", adRequestInfoParcel.zzHN);
            }
            if (adRequestInfoParcel.versionCode >= 13) {
                hashMap.put("avol", Float.valueOf(adRequestInfoParcel.zzHO));
            }
            if (adRequestInfoParcel.versionCode >= 14 && adRequestInfoParcel.zzHP > 0) {
                hashMap.put("target_api", Integer.valueOf(adRequestInfoParcel.zzHP));
            }
            if (adRequestInfoParcel.versionCode >= 15) {
                hashMap.put("scroll_index", Integer.valueOf(adRequestInfoParcel.zzHQ == -1 ? -1 : adRequestInfoParcel.zzHQ));
            }
            if (zzb.zzQ(2)) {
                zzin.v("Ad Request JSON: " + zzr.zzbC().zzG(hashMap).toString(2));
            }
            return zzr.zzbC().zzG(hashMap);
        } catch (JSONException e2) {
            zzb.zzaK("Problem serializing ad request to JSON: " + e2.getMessage());
            return null;
        }
    }

    private static void zza(HashMap<String, Object> hashMap, Location location) {
        HashMap hashMap2 = new HashMap();
        Float valueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
        Long valueOf2 = Long.valueOf(location.getTime() * 1000);
        Long valueOf3 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
        Long valueOf4 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
        hashMap2.put("radius", valueOf);
        hashMap2.put("lat", valueOf3);
        hashMap2.put("long", valueOf4);
        hashMap2.put("time", valueOf2);
        hashMap.put("uule", hashMap2);
    }

    private static void zza(HashMap<String, Object> hashMap, AdRequestParcel adRequestParcel) {
        String zzhm = zzil.zzhm();
        if (zzhm != null) {
            hashMap.put("abf", zzhm);
        }
        if (adRequestParcel.zztC != -1) {
            hashMap.put("cust_age", zzJg.format(new Date(adRequestParcel.zztC)));
        }
        if (adRequestParcel.extras != null) {
            hashMap.put("extras", adRequestParcel.extras);
        }
        if (adRequestParcel.zztD != -1) {
            hashMap.put("cust_gender", Integer.valueOf(adRequestParcel.zztD));
        }
        if (adRequestParcel.zztE != null) {
            hashMap.put("kw", adRequestParcel.zztE);
        }
        if (adRequestParcel.zztG != -1) {
            hashMap.put("tag_for_child_directed_treatment", Integer.valueOf(adRequestParcel.zztG));
        }
        if (adRequestParcel.zztF) {
            hashMap.put("adtest", "on");
        }
        if (adRequestParcel.versionCode >= 2) {
            if (adRequestParcel.zztH) {
                hashMap.put("d_imp_hdr", Integer.valueOf(1));
            }
            if (!TextUtils.isEmpty(adRequestParcel.zztI)) {
                hashMap.put("ppid", adRequestParcel.zztI);
            }
            if (adRequestParcel.zztJ != null) {
                zza((HashMap) hashMap, adRequestParcel.zztJ);
            }
        }
        if (adRequestParcel.versionCode >= 3 && adRequestParcel.zztL != null) {
            hashMap.put(ParametersKeys.URL, adRequestParcel.zztL);
        }
        if (adRequestParcel.versionCode >= 5) {
            if (adRequestParcel.zztN != null) {
                hashMap.put("custom_targeting", adRequestParcel.zztN);
            }
            if (adRequestParcel.zztO != null) {
                hashMap.put("category_exclusions", adRequestParcel.zztO);
            }
            if (adRequestParcel.zztP != null) {
                hashMap.put("request_agent", adRequestParcel.zztP);
            }
        }
        if (adRequestParcel.versionCode >= 6 && adRequestParcel.zztQ != null) {
            hashMap.put("request_pkg", adRequestParcel.zztQ);
        }
        if (adRequestParcel.versionCode >= 7) {
            hashMap.put("is_designed_for_families", Boolean.valueOf(adRequestParcel.zztR));
        }
    }

    private static void zza(HashMap<String, Object> hashMap, SearchAdRequestParcel searchAdRequestParcel) {
        Object obj;
        Object obj2 = null;
        if (Color.alpha(searchAdRequestParcel.zzvd) != 0) {
            hashMap.put("acolor", zzL(searchAdRequestParcel.zzvd));
        }
        if (Color.alpha(searchAdRequestParcel.backgroundColor) != 0) {
            hashMap.put("bgcolor", zzL(searchAdRequestParcel.backgroundColor));
        }
        if (!(Color.alpha(searchAdRequestParcel.zzve) == 0 || Color.alpha(searchAdRequestParcel.zzvf) == 0)) {
            hashMap.put("gradientto", zzL(searchAdRequestParcel.zzve));
            hashMap.put("gradientfrom", zzL(searchAdRequestParcel.zzvf));
        }
        if (Color.alpha(searchAdRequestParcel.zzvg) != 0) {
            hashMap.put("bcolor", zzL(searchAdRequestParcel.zzvg));
        }
        hashMap.put("bthick", Integer.toString(searchAdRequestParcel.zzvh));
        switch (searchAdRequestParcel.zzvi) {
            case 0:
                obj = "none";
                break;
            case 1:
                obj = "dashed";
                break;
            case 2:
                obj = "dotted";
                break;
            case 3:
                obj = "solid";
                break;
            default:
                obj = null;
                break;
        }
        if (obj != null) {
            hashMap.put("btype", obj);
        }
        switch (searchAdRequestParcel.zzvj) {
            case 0:
                obj2 = "light";
                break;
            case 1:
                obj2 = "medium";
                break;
            case 2:
                obj2 = "dark";
                break;
        }
        if (obj2 != null) {
            hashMap.put("callbuttoncolor", obj2);
        }
        if (searchAdRequestParcel.zzvk != null) {
            hashMap.put("channel", searchAdRequestParcel.zzvk);
        }
        if (Color.alpha(searchAdRequestParcel.zzvl) != 0) {
            hashMap.put("dcolor", zzL(searchAdRequestParcel.zzvl));
        }
        if (searchAdRequestParcel.zzvm != null) {
            hashMap.put("font", searchAdRequestParcel.zzvm);
        }
        if (Color.alpha(searchAdRequestParcel.zzvn) != 0) {
            hashMap.put("hcolor", zzL(searchAdRequestParcel.zzvn));
        }
        hashMap.put("headersize", Integer.toString(searchAdRequestParcel.zzvo));
        if (searchAdRequestParcel.zzvp != null) {
            hashMap.put("q", searchAdRequestParcel.zzvp);
        }
    }

    private static void zza(HashMap<String, Object> hashMap, zzhj com_google_android_gms_internal_zzhj, zza com_google_android_gms_internal_zzhn_zza) {
        hashMap.put("am", Integer.valueOf(com_google_android_gms_internal_zzhj.zzJQ));
        hashMap.put("cog", zzy(com_google_android_gms_internal_zzhj.zzJR));
        hashMap.put("coh", zzy(com_google_android_gms_internal_zzhj.zzJS));
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzhj.zzJT)) {
            hashMap.put("carrier", com_google_android_gms_internal_zzhj.zzJT);
        }
        hashMap.put("gl", com_google_android_gms_internal_zzhj.zzJU);
        if (com_google_android_gms_internal_zzhj.zzJV) {
            hashMap.put("simulator", Integer.valueOf(1));
        }
        if (com_google_android_gms_internal_zzhj.zzJW) {
            hashMap.put("is_sidewinder", Integer.valueOf(1));
        }
        hashMap.put("ma", zzy(com_google_android_gms_internal_zzhj.zzJX));
        hashMap.put("sp", zzy(com_google_android_gms_internal_zzhj.zzJY));
        hashMap.put("hl", com_google_android_gms_internal_zzhj.zzJZ);
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzhj.zzKa)) {
            hashMap.put("mv", com_google_android_gms_internal_zzhj.zzKa);
        }
        hashMap.put("muv", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKb));
        if (com_google_android_gms_internal_zzhj.zzKc != -2) {
            hashMap.put("cnt", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKc));
        }
        hashMap.put("gnt", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKd));
        hashMap.put("pt", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKe));
        hashMap.put("rm", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKf));
        hashMap.put("riv", Integer.valueOf(com_google_android_gms_internal_zzhj.zzKg));
        Bundle bundle = new Bundle();
        bundle.putString("build", com_google_android_gms_internal_zzhj.zzKl);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("is_charging", com_google_android_gms_internal_zzhj.zzKi);
        bundle2.putDouble("battery_level", com_google_android_gms_internal_zzhj.zzKh);
        bundle.putBundle("battery", bundle2);
        bundle2 = new Bundle();
        bundle2.putInt("active_network_state", com_google_android_gms_internal_zzhj.zzKk);
        bundle2.putBoolean("active_network_metered", com_google_android_gms_internal_zzhj.zzKj);
        if (com_google_android_gms_internal_zzhn_zza != null) {
            Bundle bundle3 = new Bundle();
            bundle3.putInt("predicted_latency_micros", com_google_android_gms_internal_zzhn_zza.zzKq);
            bundle3.putLong("predicted_down_throughput_bps", com_google_android_gms_internal_zzhn_zza.zzKr);
            bundle3.putLong("predicted_up_throughput_bps", com_google_android_gms_internal_zzhn_zza.zzKs);
            bundle2.putBundle("predictions", bundle3);
        }
        bundle.putBundle("network", bundle2);
        hashMap.put(ParametersKeys.ORIENTATION_DEVICE, bundle);
    }

    private static void zza(HashMap<String, Object> hashMap, String str) {
        if (str != null) {
            Map hashMap2 = new HashMap();
            hashMap2.put("token", str);
            hashMap.put("pan", hashMap2);
        }
    }

    private static String zzc(NativeAdOptionsParcel nativeAdOptionsParcel) {
        switch (nativeAdOptionsParcel != null ? nativeAdOptionsParcel.zzyB : 0) {
            case 1:
                return ParametersKeys.ORIENTATION_PORTRAIT;
            case 2:
                return ParametersKeys.ORIENTATION_LANDSCAPE;
            default:
                return "any";
        }
    }

    public static JSONObject zzc(AdResponseParcel adResponseParcel) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (adResponseParcel.zzEF != null) {
            jSONObject.put("ad_base_url", adResponseParcel.zzEF);
        }
        if (adResponseParcel.zzHW != null) {
            jSONObject.put("ad_size", adResponseParcel.zzHW);
        }
        jSONObject.put("native", adResponseParcel.zzuk);
        if (adResponseParcel.zzuk) {
            jSONObject.put("ad_json", adResponseParcel.body);
        } else {
            jSONObject.put("ad_html", adResponseParcel.body);
        }
        if (adResponseParcel.zzHY != null) {
            jSONObject.put("debug_dialog", adResponseParcel.zzHY);
        }
        if (adResponseParcel.zzHS != -1) {
            jSONObject.put("interstitial_timeout", ((double) adResponseParcel.zzHS) / 1000.0d);
        }
        if (adResponseParcel.orientation == zzr.zzbE().zzhw()) {
            jSONObject.put(ParametersKeys.ORIENTATION, ParametersKeys.ORIENTATION_PORTRAIT);
        } else if (adResponseParcel.orientation == zzr.zzbE().zzhv()) {
            jSONObject.put(ParametersKeys.ORIENTATION, ParametersKeys.ORIENTATION_LANDSCAPE);
        }
        if (adResponseParcel.zzBQ != null) {
            jSONObject.put("click_urls", zzi(adResponseParcel.zzBQ));
        }
        if (adResponseParcel.zzBR != null) {
            jSONObject.put("impression_urls", zzi(adResponseParcel.zzBR));
        }
        if (adResponseParcel.zzHV != null) {
            jSONObject.put("manual_impression_urls", zzi(adResponseParcel.zzHV));
        }
        if (adResponseParcel.zzIb != null) {
            jSONObject.put("active_view", adResponseParcel.zzIb);
        }
        jSONObject.put("ad_is_javascript", adResponseParcel.zzHZ);
        if (adResponseParcel.zzIa != null) {
            jSONObject.put("ad_passback_url", adResponseParcel.zzIa);
        }
        jSONObject.put("mediation", adResponseParcel.zzHT);
        jSONObject.put("custom_render_allowed", adResponseParcel.zzIc);
        jSONObject.put("content_url_opted_out", adResponseParcel.zzId);
        jSONObject.put("prefetch", adResponseParcel.zzIe);
        jSONObject.put("oauth2_token_status", adResponseParcel.zzIf);
        if (adResponseParcel.zzBU != -1) {
            jSONObject.put("refresh_interval_milliseconds", adResponseParcel.zzBU);
        }
        if (adResponseParcel.zzHU != -1) {
            jSONObject.put("mediation_config_cache_time_milliseconds", adResponseParcel.zzHU);
        }
        if (!TextUtils.isEmpty(adResponseParcel.zzIi)) {
            jSONObject.put("gws_query_id", adResponseParcel.zzIi);
        }
        jSONObject.put("fluid", adResponseParcel.zzul ? "height" : "");
        jSONObject.put("native_express", adResponseParcel.zzum);
        if (adResponseParcel.zzIk != null) {
            jSONObject.put("video_start_urls", zzi(adResponseParcel.zzIk));
        }
        if (adResponseParcel.zzIl != null) {
            jSONObject.put("video_complete_urls", zzi(adResponseParcel.zzIl));
        }
        if (adResponseParcel.zzIj != null) {
            jSONObject.put("rewards", adResponseParcel.zzIj.zzgR());
        }
        jSONObject.put("use_displayed_impression", adResponseParcel.zzIm);
        return jSONObject;
    }

    @Nullable
    static JSONArray zzi(List<String> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (String put : list) {
            jSONArray.put(put);
        }
        return jSONArray;
    }

    private static Integer zzy(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}
