package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzeo;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zziq;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;
import com.google.android.gms.internal.zzjj;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzb extends zzim implements com.google.android.gms.ads.internal.request.zzc.zza {
    private final Context mContext;
    zzeo zzCf;
    private AdRequestInfoParcel zzCu;
    AdResponseParcel zzGe;
    private Runnable zzGf;
    private final Object zzGg = new Object();
    private final com.google.android.gms.ads.internal.request.zza.zza zzHg;
    private final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzHh;
    zzit zzHi;
    private final zzan zzyt;

    @zzhb
    static final class zza extends Exception {
        private final int zzGu;

        public zza(String str, int i) {
            super(str);
            this.zzGu = i;
        }

        public int getErrorCode() {
            return this.zzGu;
        }
    }

    public zzb(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza com_google_android_gms_ads_internal_request_AdRequestInfoParcel_zza, zzan com_google_android_gms_internal_zzan, com.google.android.gms.ads.internal.request.zza.zza com_google_android_gms_ads_internal_request_zza_zza) {
        this.zzHg = com_google_android_gms_ads_internal_request_zza_zza;
        this.mContext = context;
        this.zzHh = com_google_android_gms_ads_internal_request_AdRequestInfoParcel_zza;
        this.zzyt = com_google_android_gms_internal_zzan;
    }

    private void zzc(int i, String str) {
        if (i == 3 || i == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaJ(str);
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(str);
        }
        if (this.zzGe == null) {
            this.zzGe = new AdResponseParcel(i);
        } else {
            this.zzGe = new AdResponseParcel(i, this.zzGe.zzBU);
        }
        this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu != null ? this.zzCu : new AdRequestInfoParcel(this.zzHh, null, -1), this.zzGe, this.zzCf, null, i, -1, this.zzGe.zzHX, null));
    }

    public void onStop() {
        synchronized (this.zzGg) {
            if (this.zzHi != null) {
                this.zzHi.cancel();
            }
        }
    }

    zzit zza(VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel) {
        return zzc.zza(this.mContext, versionInfoParcel, com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, this);
    }

    protected AdSizeParcel zzb(AdRequestInfoParcel adRequestInfoParcel) throws zza {
        if (this.zzGe.zzHW == null) {
            throw new zza("The ad response must specify one of the supported ad sizes.", 0);
        }
        String[] split = this.zzGe.zzHW.split("x");
        if (split.length != 2) {
            throw new zza("Invalid ad size format from the ad response: " + this.zzGe.zzHW, 0);
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzrp.zzuj) {
                float f = this.mContext.getResources().getDisplayMetrics().density;
                int i = adSizeParcel.width == -1 ? (int) (((float) adSizeParcel.widthPixels) / f) : adSizeParcel.width;
                int i2 = adSizeParcel.height == -2 ? (int) (((float) adSizeParcel.heightPixels) / f) : adSizeParcel.height;
                if (parseInt == i && parseInt2 == i2) {
                    return new AdSizeParcel(adSizeParcel, adRequestInfoParcel.zzrp.zzuj);
                }
            }
            throw new zza("The ad size from the ad response was not one of the requested sizes: " + this.zzGe.zzHW, 0);
        } catch (NumberFormatException e) {
            throw new zza("Invalid ad size number from the ad response: " + this.zzGe.zzHW, 0);
        }
    }

    public void zzb(@NonNull AdResponseParcel adResponseParcel) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Received ad response.");
        this.zzGe = adResponseParcel;
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        synchronized (this.zzGg) {
            this.zzHi = null;
        }
        try {
            if (this.zzGe.errorCode == -2 || this.zzGe.errorCode == -3) {
                JSONObject jSONObject;
                zzgq();
                AdSizeParcel zzb = this.zzCu.zzrp.zzuj != null ? zzb(this.zzCu) : null;
                zzr.zzbF().zzB(this.zzGe.zzId);
                if (!TextUtils.isEmpty(this.zzGe.zzIb)) {
                    try {
                        jSONObject = new JSONObject(this.zzGe.zzIb);
                    } catch (Throwable e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzb("Error parsing the JSON for Active View.", e);
                    }
                    this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu, this.zzGe, this.zzCf, zzb, -2, elapsedRealtime, this.zzGe.zzHX, jSONObject));
                    zzir.zzMc.removeCallbacks(this.zzGf);
                    return;
                }
                jSONObject = null;
                this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu, this.zzGe, this.zzCf, zzb, -2, elapsedRealtime, this.zzGe.zzHX, jSONObject));
                zzir.zzMc.removeCallbacks(this.zzGf);
                return;
            }
            throw new zza("There was a problem getting an ad response. ErrorCode: " + this.zzGe.errorCode, this.zzGe.errorCode);
        } catch (zza e2) {
            zzc(e2.getErrorCode(), e2.getMessage());
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    public void zzbr() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("AdLoaderBackgroundTask started.");
        this.zzGf = new Runnable(this) {
            final /* synthetic */ zzb zzHj;

            {
                this.zzHj = r1;
            }

            public void run() {
                synchronized (this.zzHj.zzGg) {
                    if (this.zzHj.zzHi == null) {
                        return;
                    }
                    this.zzHj.onStop();
                    this.zzHj.zzc(2, "Timed out waiting for ad response.");
                }
            }
        };
        zzir.zzMc.postDelayed(this.zzGf, ((Long) zzbt.zzwX.get()).longValue());
        final zzji com_google_android_gms_internal_zzjj = new zzjj();
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        zziq.zza(new Runnable(this) {
            final /* synthetic */ zzb zzHj;

            public void run() {
                synchronized (this.zzHj.zzGg) {
                    this.zzHj.zzHi = this.zzHj.zza(this.zzHj.zzHh.zzrl, com_google_android_gms_internal_zzjj);
                    if (this.zzHj.zzHi == null) {
                        this.zzHj.zzc(0, "Could not start the ad request service.");
                        zzir.zzMc.removeCallbacks(this.zzHj.zzGf);
                    }
                }
            }
        });
        this.zzCu = new AdRequestInfoParcel(this.zzHh, this.zzyt.zzab().zzb(this.mContext), elapsedRealtime);
        com_google_android_gms_internal_zzjj.zzh(this.zzCu);
    }

    protected void zzgq() throws zza {
        if (this.zzGe.errorCode != -3) {
            if (TextUtils.isEmpty(this.zzGe.body)) {
                throw new zza("No fill from ad server.", 3);
            }
            zzr.zzbF().zza(this.mContext, this.zzGe.zzHB);
            if (this.zzGe.zzHT) {
                try {
                    this.zzCf = new zzeo(this.zzGe.body);
                } catch (JSONException e) {
                    throw new zza("Could not parse mediation config: " + this.zzGe.body, 0);
                }
            }
        }
    }
}
