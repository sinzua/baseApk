package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@zzhb
public final class zzhg {
    private int mOrientation = -1;
    private final AdRequestInfoParcel zzCu;
    private List<String> zzGK;
    private boolean zzJA = true;
    private int zzJB = 0;
    private String zzJC = "";
    private boolean zzJD = false;
    private RewardItemParcel zzJE;
    private List<String> zzJF;
    private List<String> zzJG;
    private boolean zzJH = false;
    private String zzJn;
    private String zzJo;
    private List<String> zzJp;
    private String zzJq;
    private String zzJr;
    private List<String> zzJs;
    private long zzJt = -1;
    private boolean zzJu = false;
    private final long zzJv = -1;
    private long zzJw = -1;
    private boolean zzJx = false;
    private boolean zzJy = false;
    private boolean zzJz = false;
    private boolean zzuS = false;
    private String zzxY;

    public zzhg(AdRequestInfoParcel adRequestInfoParcel) {
        this.zzCu = adRequestInfoParcel;
    }

    private void zzA(Map<String, List<String>> map) {
        String zzd = zzd(map, "X-Afma-Fluid");
        if (zzd != null && zzd.equals("height")) {
            this.zzJD = true;
        }
    }

    private void zzB(Map<String, List<String>> map) {
        this.zzuS = "native_express".equals(zzd(map, "X-Afma-Ad-Format"));
    }

    private void zzC(Map<String, List<String>> map) {
        this.zzJE = RewardItemParcel.zzay(zzd(map, "X-Afma-Rewards"));
    }

    private void zzD(Map<String, List<String>> map) {
        if (this.zzJF == null) {
            this.zzJF = zzf(map, "X-Afma-Reward-Video-Start-Urls");
        }
    }

    private void zzE(Map<String, List<String>> map) {
        if (this.zzJG == null) {
            this.zzJG = zzf(map, "X-Afma-Reward-Video-Complete-Urls");
        }
    }

    private void zzF(Map<String, List<String>> map) {
        this.zzJH |= zzg(map, "X-Afma-Use-Displayed-Impression");
    }

    static String zzd(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    static long zze(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                zzb.zzaK("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1;
    }

    static List<String> zzf(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            if (str2 != null) {
                return Arrays.asList(str2.trim().split("\\s+"));
            }
        }
        return null;
    }

    private boolean zzg(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty() || !Boolean.valueOf((String) list.get(0)).booleanValue()) ? false : true;
    }

    private void zzk(Map<String, List<String>> map) {
        this.zzJn = zzd(map, "X-Afma-Ad-Size");
    }

    private void zzl(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Click-Tracking-Urls");
        if (zzf != null) {
            this.zzJp = zzf;
        }
    }

    private void zzm(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Debug-Dialog");
        if (list != null && !list.isEmpty()) {
            this.zzJq = (String) list.get(0);
        }
    }

    private void zzn(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Tracking-Urls");
        if (zzf != null) {
            this.zzJs = zzf;
        }
    }

    private void zzo(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Interstitial-Timeout");
        if (zze != -1) {
            this.zzJt = zze;
        }
    }

    private void zzp(Map<String, List<String>> map) {
        this.zzJr = zzd(map, "X-Afma-ActiveView");
    }

    private void zzq(Map<String, List<String>> map) {
        this.zzJy = "native".equals(zzd(map, "X-Afma-Ad-Format"));
    }

    private void zzr(Map<String, List<String>> map) {
        this.zzJx |= zzg(map, "X-Afma-Custom-Rendering-Allowed");
    }

    private void zzs(Map<String, List<String>> map) {
        this.zzJu |= zzg(map, "X-Afma-Mediation");
    }

    private void zzt(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Manual-Tracking-Urls");
        if (zzf != null) {
            this.zzGK = zzf;
        }
    }

    private void zzu(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Refresh-Rate");
        if (zze != -1) {
            this.zzJw = zze;
        }
    }

    private void zzv(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Orientation");
        if (list != null && !list.isEmpty()) {
            String str = (String) list.get(0);
            if (ParametersKeys.ORIENTATION_PORTRAIT.equalsIgnoreCase(str)) {
                this.mOrientation = zzr.zzbE().zzhw();
            } else if (ParametersKeys.ORIENTATION_LANDSCAPE.equalsIgnoreCase(str)) {
                this.mOrientation = zzr.zzbE().zzhv();
            }
        }
    }

    private void zzw(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Use-HTTPS");
        if (list != null && !list.isEmpty()) {
            this.zzJz = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzx(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Content-Url-Opted-Out");
        if (list != null && !list.isEmpty()) {
            this.zzJA = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzy(Map<String, List<String>> map) {
        List<String> zzf = zzf(map, "X-Afma-OAuth-Token-Status");
        this.zzJB = 0;
        if (zzf != null) {
            for (String str : zzf) {
                if ("Clear".equalsIgnoreCase(str)) {
                    this.zzJB = 1;
                    return;
                } else if ("No-Op".equalsIgnoreCase(str)) {
                    this.zzJB = 0;
                    return;
                }
            }
        }
    }

    private void zzz(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Gws-Query-Id");
        if (list != null && !list.isEmpty()) {
            this.zzJC = (String) list.get(0);
        }
    }

    public void zzb(String str, Map<String, List<String>> map, String str2) {
        this.zzJo = str;
        this.zzxY = str2;
        zzj((Map) map);
    }

    public AdResponseParcel zzj(long j) {
        return new AdResponseParcel(this.zzCu, this.zzJo, this.zzxY, this.zzJp, this.zzJs, this.zzJt, this.zzJu, -1, this.zzGK, this.zzJw, this.mOrientation, this.zzJn, j, this.zzJq, this.zzJr, this.zzJx, this.zzJy, this.zzJz, this.zzJA, false, this.zzJB, this.zzJC, this.zzJD, this.zzuS, this.zzJE, this.zzJF, this.zzJG, this.zzJH);
    }

    public void zzj(Map<String, List<String>> map) {
        zzk(map);
        zzl(map);
        zzm(map);
        zzn(map);
        zzo(map);
        zzs(map);
        zzt(map);
        zzu(map);
        zzv(map);
        zzp(map);
        zzw(map);
        zzr(map);
        zzq(map);
        zzx(map);
        zzy(map);
        zzz(map);
        zzA(map);
        zzB(map);
        zzC(map);
        zzD(map);
        zzE(map);
        zzF(map);
    }
}
