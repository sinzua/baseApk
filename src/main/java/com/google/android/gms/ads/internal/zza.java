package com.google.android.gms.ads.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.ThinAdSizeParcel;
import com.google.android.gms.ads.internal.client.zzf;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.overlay.zzp;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzax;
import com.google.android.gms.internal.zzbc;
import com.google.android.gms.internal.zzbf;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzdb;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhr;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzig;
import com.google.android.gms.internal.zzij;
import com.google.android.gms.internal.zzik;
import java.util.HashSet;

@zzhb
public abstract class zza extends com.google.android.gms.ads.internal.client.zzu.zza implements com.google.android.gms.ads.internal.client.zza, zzp, com.google.android.gms.ads.internal.request.zza.zza, zzdb, com.google.android.gms.internal.zzgr.zza, zzij {
    protected zzcb zzpe;
    protected zzbz zzpf;
    protected zzbz zzpg;
    protected boolean zzph = false;
    protected final zzq zzpi;
    protected final zzs zzpj;
    protected transient AdRequestParcel zzpk;
    protected final zzax zzpl;
    protected final zzd zzpm;

    zza(zzs com_google_android_gms_ads_internal_zzs, zzq com_google_android_gms_ads_internal_zzq, zzd com_google_android_gms_ads_internal_zzd) {
        this.zzpj = com_google_android_gms_ads_internal_zzs;
        if (com_google_android_gms_ads_internal_zzq == null) {
            com_google_android_gms_ads_internal_zzq = new zzq(this);
        }
        this.zzpi = com_google_android_gms_ads_internal_zzq;
        this.zzpm = com_google_android_gms_ads_internal_zzd;
        zzr.zzbC().zzJ(this.zzpj.context);
        zzr.zzbF().zzb(this.zzpj.context, this.zzpj.zzrl);
        this.zzpl = zzr.zzbF().zzhh();
    }

    private AdRequestParcel zza(AdRequestParcel adRequestParcel) {
        return (!zze.zzap(this.zzpj.context) || adRequestParcel.zztK == null) ? adRequestParcel : new zzf(adRequestParcel).zza(null).zzcN();
    }

    public void destroy() {
        zzx.zzcD("destroy must be called on the main UI thread.");
        this.zzpi.cancel();
        this.zzpl.zzj(this.zzpj.zzrq);
        this.zzpj.destroy();
    }

    public boolean isLoading() {
        return this.zzph;
    }

    public boolean isReady() {
        zzx.zzcD("isLoaded must be called on the main UI thread.");
        return this.zzpj.zzrn == null && this.zzpj.zzro == null && this.zzpj.zzrq != null;
    }

    public void onAdClicked() {
        if (this.zzpj.zzrq == null) {
            zzb.zzaK("Ad state was null when trying to ping click URLs.");
            return;
        }
        zzb.zzaI("Pinging click URLs.");
        this.zzpj.zzrs.zzgT();
        if (this.zzpj.zzrq.zzBQ != null) {
            zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq.zzBQ);
        }
        if (this.zzpj.zzrt != null) {
            try {
                this.zzpj.zzrt.onAdClicked();
            } catch (Throwable e) {
                zzb.zzd("Could not notify onAdClicked event.", e);
            }
        }
    }

    public void onAppEvent(String name, String info) {
        if (this.zzpj.zzrv != null) {
            try {
                this.zzpj.zzrv.onAppEvent(name, info);
            } catch (Throwable e) {
                zzb.zzd("Could not call the AppEventListener.", e);
            }
        }
    }

    public void pause() {
        zzx.zzcD("pause must be called on the main UI thread.");
    }

    public void resume() {
        zzx.zzcD("resume must be called on the main UI thread.");
    }

    public void setManualImpressionsEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Attempt to call setManualImpressionsEnabled for an unsupported ad type.");
    }

    public void setUserId(String userId) {
        zzx.zzcD("setUserId must be called on the main UI thread.");
        this.zzpj.setUserId(userId);
    }

    public void stopLoading() {
        zzx.zzcD("stopLoading must be called on the main UI thread.");
        this.zzph = false;
        this.zzpj.zzf(true);
    }

    Bundle zza(zzbf com_google_android_gms_internal_zzbf) {
        Bundle bundle = null;
        if (com_google_android_gms_internal_zzbf != null) {
            String zzcy;
            String zzcz;
            if (com_google_android_gms_internal_zzbf.zzcK()) {
                com_google_android_gms_internal_zzbf.wakeup();
            }
            zzbc zzcI = com_google_android_gms_internal_zzbf.zzcI();
            if (zzcI != null) {
                zzcy = zzcI.zzcy();
                zzcz = zzcI.zzcz();
                zzb.zzaI("In AdManager: loadAd, " + zzcI.toString());
                if (zzcy != null) {
                    zzr.zzbF().zzaA(zzcy);
                }
            } else {
                zzcy = zzr.zzbF().zzhf();
                zzcz = null;
            }
            if (zzcy != null) {
                bundle = new Bundle(1);
                bundle.putString("fingerprint", zzcy);
                if (!zzcy.equals(zzcz)) {
                    bundle.putString("v_fp", zzcz);
                }
            }
        }
        return bundle;
    }

    public void zza(AdSizeParcel adSizeParcel) {
        zzx.zzcD("setAdSize must be called on the main UI thread.");
        this.zzpj.zzrp = adSizeParcel;
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzED == null || this.zzpj.zzrL != 0)) {
            this.zzpj.zzrq.zzED.zza(adSizeParcel);
        }
        if (this.zzpj.zzrm != null) {
            if (this.zzpj.zzrm.getChildCount() > 1) {
                this.zzpj.zzrm.removeView(this.zzpj.zzrm.getNextView());
            }
            this.zzpj.zzrm.setMinimumWidth(adSizeParcel.widthPixels);
            this.zzpj.zzrm.setMinimumHeight(adSizeParcel.heightPixels);
            this.zzpj.zzrm.requestLayout();
        }
    }

    public void zza(com.google.android.gms.ads.internal.client.zzp com_google_android_gms_ads_internal_client_zzp) {
        zzx.zzcD("setAdListener must be called on the main UI thread.");
        this.zzpj.zzrt = com_google_android_gms_ads_internal_client_zzp;
    }

    public void zza(zzq com_google_android_gms_ads_internal_client_zzq) {
        zzx.zzcD("setAdListener must be called on the main UI thread.");
        this.zzpj.zzru = com_google_android_gms_ads_internal_client_zzq;
    }

    public void zza(zzw com_google_android_gms_ads_internal_client_zzw) {
        zzx.zzcD("setAppEventListener must be called on the main UI thread.");
        this.zzpj.zzrv = com_google_android_gms_ads_internal_client_zzw;
    }

    public void zza(com.google.android.gms.ads.internal.client.zzx com_google_android_gms_ads_internal_client_zzx) {
        zzx.zzcD("setCorrelationIdProvider must be called on the main UI thread");
        this.zzpj.zzrw = com_google_android_gms_ads_internal_client_zzx;
    }

    public void zza(zzd com_google_android_gms_ads_internal_reward_client_zzd) {
        zzx.zzcD("setRewardedVideoAdListener can only be called from the UI thread.");
        this.zzpj.zzrF = com_google_android_gms_ads_internal_reward_client_zzd;
    }

    protected void zza(RewardItemParcel rewardItemParcel) {
        if (this.zzpj.zzrF != null) {
            try {
                String str = "";
                int i = 0;
                if (rewardItemParcel != null) {
                    str = rewardItemParcel.type;
                    i = rewardItemParcel.zzKS;
                }
                this.zzpj.zzrF.zza(new zzhr(str, i));
            } catch (Throwable e) {
                zzb.zzd("Could not call RewardedVideoAdListener.onRewarded().", e);
            }
        }
    }

    public void zza(zzcf com_google_android_gms_internal_zzcf) {
        throw new IllegalStateException("setOnCustomRenderedAdLoadedListener is not supported for current ad type");
    }

    public void zza(zzgd com_google_android_gms_internal_zzgd) {
        throw new IllegalStateException("setInAppPurchaseListener is not supported for current ad type");
    }

    public void zza(zzgh com_google_android_gms_internal_zzgh, String str) {
        throw new IllegalStateException("setPlayStorePurchaseParams is not supported for current ad type");
    }

    public void zza(com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza) {
        if (!(com_google_android_gms_internal_zzif_zza.zzLe.zzHX == -1 || TextUtils.isEmpty(com_google_android_gms_internal_zzif_zza.zzLe.zzIh))) {
            long zzp = zzp(com_google_android_gms_internal_zzif_zza.zzLe.zzIh);
            if (zzp != -1) {
                zzbz zzb = this.zzpe.zzb(zzp + com_google_android_gms_internal_zzif_zza.zzLe.zzHX);
                this.zzpe.zza(zzb, "stc");
            }
        }
        this.zzpe.zzN(com_google_android_gms_internal_zzif_zza.zzLe.zzIh);
        this.zzpe.zza(this.zzpf, "arf");
        this.zzpg = this.zzpe.zzdB();
        this.zzpe.zzc("gqi", com_google_android_gms_internal_zzif_zza.zzLe.zzIi);
        this.zzpj.zzrn = null;
        this.zzpj.zzrr = com_google_android_gms_internal_zzif_zza;
        zza(com_google_android_gms_internal_zzif_zza, this.zzpe);
    }

    protected abstract void zza(com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza, zzcb com_google_android_gms_internal_zzcb);

    public void zza(HashSet<zzig> hashSet) {
        this.zzpj.zza(hashSet);
    }

    protected abstract boolean zza(AdRequestParcel adRequestParcel, zzcb com_google_android_gms_internal_zzcb);

    boolean zza(zzif com_google_android_gms_internal_zzif) {
        return false;
    }

    protected abstract boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2);

    void zzaL() {
        this.zzpe = new zzcb(((Boolean) zzbt.zzwg.get()).booleanValue(), "load_ad", this.zzpj.zzrp.zzuh);
        this.zzpf = new zzbz(-1, null, null);
        this.zzpg = new zzbz(-1, null, null);
    }

    public com.google.android.gms.dynamic.zzd zzaM() {
        zzx.zzcD("getAdFrame must be called on the main UI thread.");
        return com.google.android.gms.dynamic.zze.zzC(this.zzpj.zzrm);
    }

    public AdSizeParcel zzaN() {
        zzx.zzcD("getAdSize must be called on the main UI thread.");
        return this.zzpj.zzrp == null ? null : new ThinAdSizeParcel(this.zzpj.zzrp);
    }

    public void zzaO() {
        zzaR();
    }

    public void zzaP() {
        zzx.zzcD("recordManualImpression must be called on the main UI thread.");
        if (this.zzpj.zzrq == null) {
            zzb.zzaK("Ad state was null when trying to ping manual tracking URLs.");
            return;
        }
        zzb.zzaI("Pinging manual tracking URLs.");
        if (this.zzpj.zzrq.zzHV != null && !this.zzpj.zzrq.zzLc) {
            zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq.zzHV);
            this.zzpj.zzrq.zzLc = true;
        }
    }

    protected void zzaQ() {
        zzb.zzaJ("Ad closing.");
        if (this.zzpj.zzru != null) {
            try {
                this.zzpj.zzru.onAdClosed();
            } catch (Throwable e) {
                zzb.zzd("Could not call AdListener.onAdClosed().", e);
            }
        }
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoAdClosed();
            } catch (Throwable e2) {
                zzb.zzd("Could not call RewardedVideoAdListener.onRewardedVideoAdClosed().", e2);
            }
        }
    }

    protected void zzaR() {
        zzb.zzaJ("Ad leaving application.");
        if (this.zzpj.zzru != null) {
            try {
                this.zzpj.zzru.onAdLeftApplication();
            } catch (Throwable e) {
                zzb.zzd("Could not call AdListener.onAdLeftApplication().", e);
            }
        }
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoAdLeftApplication();
            } catch (Throwable e2) {
                zzb.zzd("Could not call  RewardedVideoAdListener.onRewardedVideoAdLeftApplication().", e2);
            }
        }
    }

    protected void zzaS() {
        zzb.zzaJ("Ad opening.");
        if (this.zzpj.zzru != null) {
            try {
                this.zzpj.zzru.onAdOpened();
            } catch (Throwable e) {
                zzb.zzd("Could not call AdListener.onAdOpened().", e);
            }
        }
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoAdOpened();
            } catch (Throwable e2) {
                zzb.zzd("Could not call RewardedVideoAdListener.onRewardedVideoAdOpened().", e2);
            }
        }
    }

    protected void zzaT() {
        zzb.zzaJ("Ad finished loading.");
        this.zzph = false;
        if (this.zzpj.zzru != null) {
            try {
                this.zzpj.zzru.onAdLoaded();
            } catch (Throwable e) {
                zzb.zzd("Could not call AdListener.onAdLoaded().", e);
            }
        }
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoAdLoaded();
            } catch (Throwable e2) {
                zzb.zzd("Could not call RewardedVideoAdListener.onRewardedVideoAdLoaded().", e2);
            }
        }
    }

    protected void zzaU() {
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoStarted();
            } catch (Throwable e) {
                zzb.zzd("Could not call RewardedVideoAdListener.onVideoStarted().", e);
            }
        }
    }

    protected void zzb(View view) {
        this.zzpj.zzrm.addView(view, zzr.zzbE().zzhy());
    }

    public void zzb(zzif com_google_android_gms_internal_zzif) {
        this.zzpe.zza(this.zzpg, "awr");
        this.zzpj.zzro = null;
        if (!(com_google_android_gms_internal_zzif.errorCode == -2 || com_google_android_gms_internal_zzif.errorCode == 3)) {
            zzr.zzbF().zzb(this.zzpj.zzbS());
        }
        if (com_google_android_gms_internal_zzif.errorCode == -1) {
            this.zzph = false;
            return;
        }
        if (zza(com_google_android_gms_internal_zzif)) {
            zzb.zzaI("Ad refresh scheduled.");
        }
        if (com_google_android_gms_internal_zzif.errorCode != -2) {
            zzf(com_google_android_gms_internal_zzif.errorCode);
            return;
        }
        if (this.zzpj.zzrJ == null) {
            this.zzpj.zzrJ = new zzik(this.zzpj.zzrj);
        }
        this.zzpl.zzi(this.zzpj.zzrq);
        if (zza(this.zzpj.zzrq, com_google_android_gms_internal_zzif)) {
            this.zzpj.zzrq = com_google_android_gms_internal_zzif;
            this.zzpj.zzcb();
            this.zzpe.zzc("is_mraid", this.zzpj.zzrq.zzcv() ? "1" : "0");
            this.zzpe.zzc("is_mediation", this.zzpj.zzrq.zzHT ? "1" : "0");
            if (!(this.zzpj.zzrq.zzED == null || this.zzpj.zzrq.zzED.zzhU() == null)) {
                this.zzpe.zzc("is_video", this.zzpj.zzrq.zzED.zzhU().zzih() ? "1" : "0");
            }
            this.zzpe.zza(this.zzpf, "ttc");
            if (zzr.zzbF().zzhb() != null) {
                zzr.zzbF().zzhb().zza(this.zzpe);
            }
            if (this.zzpj.zzbW()) {
                zzaT();
            }
        }
    }

    public boolean zzb(AdRequestParcel adRequestParcel) {
        zzx.zzcD("loadAd must be called on the main UI thread.");
        AdRequestParcel zza = zza(adRequestParcel);
        if (this.zzpj.zzrn == null && this.zzpj.zzro == null) {
            zzb.zzaJ("Starting ad request.");
            zzaL();
            this.zzpf = this.zzpe.zzdB();
            if (!zza.zztF) {
                zzb.zzaJ("Use AdRequest.Builder.addTestDevice(\"" + zzn.zzcS().zzT(this.zzpj.context) + "\") to get test ads on this device.");
            }
            this.zzph = zza(zza, this.zzpe);
            return this.zzph;
        }
        if (this.zzpk != null) {
            zzb.zzaK("Aborting last ad request since another ad request is already in progress. The current request object will still be cached for future refreshes.");
        } else {
            zzb.zzaK("Loading already in progress, saving this object for future refreshes.");
        }
        this.zzpk = zza;
        return false;
    }

    protected void zzc(zzif com_google_android_gms_internal_zzif) {
        if (com_google_android_gms_internal_zzif == null) {
            zzb.zzaK("Ad state was null when trying to ping impression URLs.");
            return;
        }
        zzb.zzaI("Pinging Impression URLs.");
        this.zzpj.zzrs.zzgS();
        if (com_google_android_gms_internal_zzif.zzBR != null && !com_google_android_gms_internal_zzif.zzLb) {
            zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, com_google_android_gms_internal_zzif.zzBR);
            com_google_android_gms_internal_zzif.zzLb = true;
        }
    }

    protected boolean zzc(AdRequestParcel adRequestParcel) {
        ViewParent parent = this.zzpj.zzrm.getParent();
        return (parent instanceof View) && ((View) parent).isShown() && zzr.zzbC().zzhq();
    }

    public void zzd(AdRequestParcel adRequestParcel) {
        if (zzc(adRequestParcel)) {
            zzb(adRequestParcel);
            return;
        }
        zzb.zzaJ("Ad is not visible. Not refreshing ad.");
        this.zzpi.zzg(adRequestParcel);
    }

    protected void zzf(int i) {
        zzb.zzaK("Failed to load ad: " + i);
        this.zzph = false;
        if (this.zzpj.zzru != null) {
            try {
                this.zzpj.zzru.onAdFailedToLoad(i);
            } catch (Throwable e) {
                zzb.zzd("Could not call AdListener.onAdFailedToLoad().", e);
            }
        }
        if (this.zzpj.zzrF != null) {
            try {
                this.zzpj.zzrF.onRewardedVideoAdFailedToLoad(i);
            } catch (Throwable e2) {
                zzb.zzd("Could not call RewardedVideoAdListener.onRewardedVideoAdFailedToLoad().", e2);
            }
        }
    }

    long zzp(@NonNull String str) {
        int indexOf = str.indexOf("ufe");
        int indexOf2 = str.indexOf(44, indexOf);
        if (indexOf2 == -1) {
            indexOf2 = str.length();
        }
        try {
            return Long.parseLong(str.substring(indexOf + 4, indexOf2));
        } catch (IndexOutOfBoundsException e) {
            zzb.zzaK("Invalid index for Url fetch time in CSI latency info.");
            return -1;
        } catch (NumberFormatException e2) {
            zzb.zzaK("Cannot find valid format of Url fetch time in CSI latency info.");
            return -1;
        }
    }
}
