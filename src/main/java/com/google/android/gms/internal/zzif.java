package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

@zzhb
public class zzif {
    public final int errorCode;
    public final int orientation;
    public final List<String> zzBQ;
    public final List<String> zzBR;
    public final long zzBU;
    public final zzen zzCp;
    public final zzey zzCq;
    public final String zzCr;
    public final zzeq zzCs;
    public final zzjp zzED;
    public final long zzHS;
    public final boolean zzHT;
    public final long zzHU;
    public final List<String> zzHV;
    public final String zzHY;
    public final AdRequestParcel zzHt;
    public final String zzHw;
    @Nullable
    public final RewardItemParcel zzIj;
    @Nullable
    public final List<String> zzIl;
    public final boolean zzIm;
    public final JSONObject zzKT;
    public boolean zzKU;
    public final zzeo zzKV;
    public final AdSizeParcel zzKW;
    @Nullable
    public final List<String> zzKX;
    public final long zzKY;
    public final long zzKZ;
    public final com.google.android.gms.ads.internal.formats.zzh.zza zzLa;
    public boolean zzLb;
    public boolean zzLc;

    @zzhb
    public static final class zza {
        public final int errorCode;
        public final JSONObject zzKT;
        public final zzeo zzKV;
        public final long zzKY;
        public final long zzKZ;
        public final AdRequestInfoParcel zzLd;
        public final AdResponseParcel zzLe;
        public final AdSizeParcel zzrp;

        public zza(AdRequestInfoParcel adRequestInfoParcel, AdResponseParcel adResponseParcel, zzeo com_google_android_gms_internal_zzeo, AdSizeParcel adSizeParcel, int i, long j, long j2, JSONObject jSONObject) {
            this.zzLd = adRequestInfoParcel;
            this.zzLe = adResponseParcel;
            this.zzKV = com_google_android_gms_internal_zzeo;
            this.zzrp = adSizeParcel;
            this.errorCode = i;
            this.zzKY = j;
            this.zzKZ = j2;
            this.zzKT = jSONObject;
        }
    }

    public zzif(AdRequestParcel adRequestParcel, zzjp com_google_android_gms_internal_zzjp, List<String> list, int i, List<String> list2, List<String> list3, int i2, long j, String str, boolean z, zzen com_google_android_gms_internal_zzen, zzey com_google_android_gms_internal_zzey, String str2, zzeo com_google_android_gms_internal_zzeo, zzeq com_google_android_gms_internal_zzeq, long j2, AdSizeParcel adSizeParcel, long j3, long j4, long j5, String str3, JSONObject jSONObject, com.google.android.gms.ads.internal.formats.zzh.zza com_google_android_gms_ads_internal_formats_zzh_zza, RewardItemParcel rewardItemParcel, List<String> list4, List<String> list5, boolean z2) {
        this.zzLb = false;
        this.zzLc = false;
        this.zzHt = adRequestParcel;
        this.zzED = com_google_android_gms_internal_zzjp;
        this.zzBQ = zzj(list);
        this.errorCode = i;
        this.zzBR = zzj(list2);
        this.zzHV = zzj(list3);
        this.orientation = i2;
        this.zzBU = j;
        this.zzHw = str;
        this.zzHT = z;
        this.zzCp = com_google_android_gms_internal_zzen;
        this.zzCq = com_google_android_gms_internal_zzey;
        this.zzCr = str2;
        this.zzKV = com_google_android_gms_internal_zzeo;
        this.zzCs = com_google_android_gms_internal_zzeq;
        this.zzHU = j2;
        this.zzKW = adSizeParcel;
        this.zzHS = j3;
        this.zzKY = j4;
        this.zzKZ = j5;
        this.zzHY = str3;
        this.zzKT = jSONObject;
        this.zzLa = com_google_android_gms_ads_internal_formats_zzh_zza;
        this.zzIj = rewardItemParcel;
        this.zzKX = zzj(list4);
        this.zzIl = zzj(list5);
        this.zzIm = z2;
    }

    public zzif(zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp, zzen com_google_android_gms_internal_zzen, zzey com_google_android_gms_internal_zzey, String str, zzeq com_google_android_gms_internal_zzeq, com.google.android.gms.ads.internal.formats.zzh.zza com_google_android_gms_ads_internal_formats_zzh_zza) {
        this(com_google_android_gms_internal_zzif_zza.zzLd.zzHt, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzif_zza.zzLe.zzBQ, com_google_android_gms_internal_zzif_zza.errorCode, com_google_android_gms_internal_zzif_zza.zzLe.zzBR, com_google_android_gms_internal_zzif_zza.zzLe.zzHV, com_google_android_gms_internal_zzif_zza.zzLe.orientation, com_google_android_gms_internal_zzif_zza.zzLe.zzBU, com_google_android_gms_internal_zzif_zza.zzLd.zzHw, com_google_android_gms_internal_zzif_zza.zzLe.zzHT, com_google_android_gms_internal_zzen, com_google_android_gms_internal_zzey, str, com_google_android_gms_internal_zzif_zza.zzKV, com_google_android_gms_internal_zzeq, com_google_android_gms_internal_zzif_zza.zzLe.zzHU, com_google_android_gms_internal_zzif_zza.zzrp, com_google_android_gms_internal_zzif_zza.zzLe.zzHS, com_google_android_gms_internal_zzif_zza.zzKY, com_google_android_gms_internal_zzif_zza.zzKZ, com_google_android_gms_internal_zzif_zza.zzLe.zzHY, com_google_android_gms_internal_zzif_zza.zzKT, com_google_android_gms_ads_internal_formats_zzh_zza, com_google_android_gms_internal_zzif_zza.zzLe.zzIj, com_google_android_gms_internal_zzif_zza.zzLe.zzIk, com_google_android_gms_internal_zzif_zza.zzLe.zzIk, com_google_android_gms_internal_zzif_zza.zzLe.zzIm);
    }

    @Nullable
    private static <T> List<T> zzj(@Nullable List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public boolean zzcv() {
        return (this.zzED == null || this.zzED.zzhU() == null) ? false : this.zzED.zzhU().zzcv();
    }
}
