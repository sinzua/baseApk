package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzk;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Map;
import java.util.WeakHashMap;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.json.JSONObject;

@zzhb
public final class zzdo implements zzdf {
    private final Map<zzjp, Integer> zzzI = new WeakHashMap();

    private static int zza(Context context, Map<String, String> map, String str, int i) {
        String str2 = (String) map.get(str);
        if (str2 != null) {
            try {
                i = zzn.zzcS().zzb(context, Integer.parseInt(str2));
            } catch (NumberFormatException e) {
                zzb.zzaK("Could not parse " + str + " in a video GMSG: " + str2);
            }
        }
        return i;
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str;
        String str2 = (String) map.get(ParametersKeys.ACTION);
        if (str2 == null) {
            zzb.zzaK("Action missing from video GMSG.");
            return;
        }
        if (zzb.zzQ(3)) {
            JSONObject jSONObject = new JSONObject(map);
            jSONObject.remove("google.afma.Notify_dt");
            zzb.zzaI("Video GMSG: " + str2 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + jSONObject.toString());
        }
        if ("background".equals(str2)) {
            str = (String) map.get(ParametersKeys.COLOR);
            if (TextUtils.isEmpty(str)) {
                zzb.zzaK("Color parameter missing from color video GMSG.");
                return;
            }
            try {
                int parseColor = Color.parseColor(str);
                zzjo zzia = com_google_android_gms_internal_zzjp.zzia();
                if (zzia != null) {
                    zzk zzhM = zzia.zzhM();
                    if (zzhM != null) {
                        zzhM.setBackgroundColor(parseColor);
                        return;
                    }
                }
                this.zzzI.put(com_google_android_gms_internal_zzjp, Integer.valueOf(parseColor));
                return;
            } catch (IllegalArgumentException e) {
                zzb.zzaK("Invalid color parameter in video GMSG.");
                return;
            }
        }
        zzjo zzia2 = com_google_android_gms_internal_zzjp.zzia();
        if (zzia2 == null) {
            zzb.zzaK("Could not get underlay container for a video GMSG.");
            return;
        }
        boolean equals = "new".equals(str2);
        boolean equals2 = ParametersKeys.POSITION.equals(str2);
        int zza;
        int zza2;
        if (equals || equals2) {
            int parseInt;
            Context context = com_google_android_gms_internal_zzjp.getContext();
            int zza3 = zza(context, map, "x", 0);
            zza = zza(context, map, "y", 0);
            zza2 = zza(context, map, "w", -1);
            int zza4 = zza(context, map, "h", -1);
            try {
                parseInt = Integer.parseInt((String) map.get("player"));
            } catch (NumberFormatException e2) {
                parseInt = 0;
            }
            if (equals && zzia2.zzhM() == null) {
                zzia2.zza(zza3, zza, zza2, zza4, parseInt);
                if (this.zzzI.containsKey(com_google_android_gms_internal_zzjp)) {
                    zza3 = ((Integer) this.zzzI.get(com_google_android_gms_internal_zzjp)).intValue();
                    zzk zzhM2 = zzia2.zzhM();
                    zzhM2.setBackgroundColor(zza3);
                    zzhM2.zzfE();
                    return;
                }
                return;
            }
            zzia2.zze(zza3, zza, zza2, zza4);
            return;
        }
        zzk zzhM3 = zzia2.zzhM();
        if (zzhM3 == null) {
            zzk.zzg(com_google_android_gms_internal_zzjp);
        } else if ("click".equals(str2)) {
            Context context2 = com_google_android_gms_internal_zzjp.getContext();
            zza = zza(context2, map, "x", 0);
            zza2 = zza(context2, map, "y", 0);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, (float) zza, (float) zza2, 0);
            zzhM3.zzd(obtain);
            obtain.recycle();
        } else if ("currentTime".equals(str2)) {
            str = (String) map.get("time");
            if (str == null) {
                zzb.zzaK("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                zzhM3.seekTo((int) (Float.parseFloat(str) * 1000.0f));
            } catch (NumberFormatException e3) {
                zzb.zzaK("Could not parse time parameter from currentTime video GMSG: " + str);
            }
        } else if ("hide".equals(str2)) {
            zzhM3.setVisibility(4);
        } else if ("load".equals(str2)) {
            zzhM3.zzfD();
        } else if ("mimetype".equals(str2)) {
            zzhM3.setMimeType((String) map.get("mimetype"));
        } else if ("muted".equals(str2)) {
            if (Boolean.parseBoolean((String) map.get("muted"))) {
                zzhM3.zzff();
            } else {
                zzhM3.zzfg();
            }
        } else if ("pause".equals(str2)) {
            zzhM3.pause();
        } else if ("play".equals(str2)) {
            zzhM3.play();
        } else if ("show".equals(str2)) {
            zzhM3.setVisibility(0);
        } else if ("src".equals(str2)) {
            zzhM3.zzap((String) map.get("src"));
        } else if ("volume".equals(str2)) {
            str = (String) map.get("volume");
            if (str == null) {
                zzb.zzaK("Level parameter missing from volume video GMSG.");
                return;
            }
            try {
                zzhM3.zza(Float.parseFloat(str));
            } catch (NumberFormatException e4) {
                zzb.zzaK("Could not parse volume parameter from volume video GMSG: " + str);
            }
        } else if ("watermark".equals(str2)) {
            zzhM3.zzfE();
        } else {
            zzb.zzaK("Unknown video action: " + str2);
        }
    }
}
