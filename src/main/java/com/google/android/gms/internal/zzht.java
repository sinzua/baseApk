package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzb;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzif.zza;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

@zzhb
public class zzht extends zzb implements zzhw {
    private static final zzew zzKv = new zzew();
    private final Map<String, zzia> zzKw = new HashMap();
    private boolean zzKx;

    public zzht(Context context, zzd com_google_android_gms_ads_internal_zzd, AdSizeParcel adSizeParcel, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, null, com_google_android_gms_internal_zzex, versionInfoParcel, com_google_android_gms_ads_internal_zzd);
    }

    private zza zzc(zza com_google_android_gms_internal_zzif_zza) {
        zzin.v("Creating mediation ad response for non-mediated rewarded ad.");
        try {
            String jSONObject = zzhe.zzc(com_google_android_gms_internal_zzif_zza.zzLe).toString();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER, com_google_android_gms_internal_zzif_zza.zzLd.zzrj);
            zzen com_google_android_gms_internal_zzen = new zzen(jSONObject, null, Arrays.asList(new String[]{"com.google.ads.mediation.admob.AdMobAdapter"}), null, null, Collections.emptyList(), Collections.emptyList(), jSONObject2.toString(), null, Collections.emptyList(), Collections.emptyList(), null, null, null, null, null);
            return new zza(com_google_android_gms_internal_zzif_zza.zzLd, com_google_android_gms_internal_zzif_zza.zzLe, new zzeo(Arrays.asList(new zzen[]{com_google_android_gms_internal_zzen}), -1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "", -1, 0, 1, null, 0, -1, -1), com_google_android_gms_internal_zzif_zza.zzrp, com_google_android_gms_internal_zzif_zza.errorCode, com_google_android_gms_internal_zzif_zza.zzKY, com_google_android_gms_internal_zzif_zza.zzKZ, com_google_android_gms_internal_zzif_zza.zzKT);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to generate ad state for non-mediated rewarded video.", e);
            return zzd(com_google_android_gms_internal_zzif_zza);
        }
    }

    private zza zzd(zza com_google_android_gms_internal_zzif_zza) {
        return new zza(com_google_android_gms_internal_zzif_zza.zzLd, com_google_android_gms_internal_zzif_zza.zzLe, null, com_google_android_gms_internal_zzif_zza.zzrp, 0, com_google_android_gms_internal_zzif_zza.zzKY, com_google_android_gms_internal_zzif_zza.zzKZ, com_google_android_gms_internal_zzif_zza.zzKT);
    }

    public void destroy() {
        zzx.zzcD("destroy must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia com_google_android_gms_internal_zzia = (zzia) this.zzKw.get(str);
                if (!(com_google_android_gms_internal_zzia == null || com_google_android_gms_internal_zzia.zzgP() == null)) {
                    com_google_android_gms_internal_zzia.zzgP().destroy();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to destroy adapter: " + str);
            }
        }
    }

    public boolean isLoaded() {
        zzx.zzcD("isLoaded must be called on the main UI thread.");
        return this.zzpj.zzrn == null && this.zzpj.zzro == null && this.zzpj.zzrq != null && !this.zzKx;
    }

    public void onRewardedVideoAdClosed() {
        zzaQ();
    }

    public void onRewardedVideoAdLeftApplication() {
        zzaR();
    }

    public void onRewardedVideoAdOpened() {
        zza(this.zzpj.zzrq, false);
        zzaS();
    }

    public void onRewardedVideoStarted() {
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCp == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzCp.zzBH);
        }
        zzaU();
    }

    public void pause() {
        zzx.zzcD("pause must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia com_google_android_gms_internal_zzia = (zzia) this.zzKw.get(str);
                if (!(com_google_android_gms_internal_zzia == null || com_google_android_gms_internal_zzia.zzgP() == null)) {
                    com_google_android_gms_internal_zzia.zzgP().pause();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to pause adapter: " + str);
            }
        }
    }

    public void resume() {
        zzx.zzcD("resume must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia com_google_android_gms_internal_zzia = (zzia) this.zzKw.get(str);
                if (!(com_google_android_gms_internal_zzia == null || com_google_android_gms_internal_zzia.zzgP() == null)) {
                    com_google_android_gms_internal_zzia.zzgP().resume();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to resume adapter: " + str);
            }
        }
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) {
        zzx.zzcD("loadAd must be called on the main UI thread.");
        if (TextUtils.isEmpty(rewardedVideoAdRequestParcel.zzrj)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Invalid ad unit id. Aborting.");
            return;
        }
        this.zzKx = false;
        this.zzpj.zzrj = rewardedVideoAdRequestParcel.zzrj;
        super.zzb(rewardedVideoAdRequestParcel.zzHt);
    }

    public void zza(final zza com_google_android_gms_internal_zzif_zza, zzcb com_google_android_gms_internal_zzcb) {
        if (com_google_android_gms_internal_zzif_zza.errorCode != -2) {
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzht zzKy;

                public void run() {
                    this.zzKy.zzb(new zzif(com_google_android_gms_internal_zzif_zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzpj.zzrr = com_google_android_gms_internal_zzif_zza;
        if (com_google_android_gms_internal_zzif_zza.zzKV == null) {
            this.zzpj.zzrr = zzc(com_google_android_gms_internal_zzif_zza);
        }
        this.zzpj.zzrL = 0;
        this.zzpj.zzro = zzr.zzbB().zza(this.zzpj.context, this.zzpj.getUserId(), this.zzpj.zzrr, this);
    }

    public boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        return true;
    }

    @Nullable
    public zzia zzaw(String str) {
        Throwable th;
        zzia com_google_android_gms_internal_zzia = (zzia) this.zzKw.get(str);
        if (com_google_android_gms_internal_zzia != null) {
            return com_google_android_gms_internal_zzia;
        }
        try {
            zzia com_google_android_gms_internal_zzia2 = new zzia(("com.google.ads.mediation.admob.AdMobAdapter".equals(str) ? zzKv : this.zzpn).zzaf(str), this);
            try {
                this.zzKw.put(str, com_google_android_gms_internal_zzia2);
                return com_google_android_gms_internal_zzia2;
            } catch (Throwable e) {
                Throwable th2 = e;
                com_google_android_gms_internal_zzia = com_google_android_gms_internal_zzia2;
                th = th2;
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, th);
                return com_google_android_gms_internal_zzia;
            }
        } catch (Exception e2) {
            th = e2;
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, th);
            return com_google_android_gms_internal_zzia;
        }
    }

    public void zzc(@Nullable RewardItemParcel rewardItemParcel) {
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCp == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzCp.zzBI);
        }
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzKV == null || TextUtils.isEmpty(this.zzpj.zzrq.zzKV.zzBV))) {
            rewardItemParcel = new RewardItemParcel(this.zzpj.zzrq.zzKV.zzBV, this.zzpj.zzrq.zzKV.zzBW);
        }
        zza(rewardItemParcel);
    }

    public void zzgL() {
        zzx.zzcD("showAd must be called on the main UI thread.");
        if (isLoaded()) {
            this.zzKx = true;
            zzia zzaw = zzaw(this.zzpj.zzrq.zzCr);
            if (zzaw != null && zzaw.zzgP() != null) {
                try {
                    zzaw.zzgP().showVideo();
                    return;
                } catch (Throwable e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call showVideo.", e);
                    return;
                }
            }
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK("The reward video has not loaded.");
    }

    public void zzgM() {
        onAdClicked();
    }
}
