package com.google.android.gms.ads.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.purchase.GInAppPurchaseManagerInfoParcel;
import com.google.android.gms.ads.internal.purchase.zzc;
import com.google.android.gms.ads.internal.purchase.zzd;
import com.google.android.gms.ads.internal.purchase.zzf;
import com.google.android.gms.ads.internal.purchase.zzj;
import com.google.android.gms.ads.internal.purchase.zzk;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza;
import com.google.android.gms.ads.internal.request.CapabilityParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzdh;
import com.google.android.gms.internal.zzep;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzga;
import com.google.android.gms.internal.zzgc;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzig;
import com.google.android.gms.internal.zzir;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@zzhb
public abstract class zzb extends zza implements zzg, zzj, zzdh, zzep {
    private final Messenger mMessenger;
    protected final zzex zzpn;
    protected transient boolean zzpo;

    public zzb(Context context, AdSizeParcel adSizeParcel, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        this(new zzs(context, adSizeParcel, str, versionInfoParcel), com_google_android_gms_internal_zzex, null, com_google_android_gms_ads_internal_zzd);
    }

    zzb(zzs com_google_android_gms_ads_internal_zzs, zzex com_google_android_gms_internal_zzex, zzq com_google_android_gms_ads_internal_zzq, zzd com_google_android_gms_ads_internal_zzd) {
        super(com_google_android_gms_ads_internal_zzs, com_google_android_gms_ads_internal_zzq, com_google_android_gms_ads_internal_zzd);
        this.zzpn = com_google_android_gms_internal_zzex;
        this.mMessenger = new Messenger(new zzga(this.zzpj.context));
        this.zzpo = false;
    }

    private zza zza(AdRequestParcel adRequestParcel, Bundle bundle) {
        PackageInfo packageInfo;
        int i;
        ApplicationInfo applicationInfo = this.zzpj.context.getApplicationInfo();
        try {
            packageInfo = this.zzpj.context.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        DisplayMetrics displayMetrics = this.zzpj.context.getResources().getDisplayMetrics();
        Bundle bundle2 = null;
        if (!(this.zzpj.zzrm == null || this.zzpj.zzrm.getParent() == null)) {
            int[] iArr = new int[2];
            this.zzpj.zzrm.getLocationOnScreen(iArr);
            int i2 = iArr[0];
            int i3 = iArr[1];
            int width = this.zzpj.zzrm.getWidth();
            int height = this.zzpj.zzrm.getHeight();
            i = 0;
            if (this.zzpj.zzrm.isShown() && i2 + width > 0 && i3 + height > 0 && i2 <= displayMetrics.widthPixels && i3 <= displayMetrics.heightPixels) {
                i = 1;
            }
            bundle2 = new Bundle(5);
            bundle2.putInt("x", i2);
            bundle2.putInt("y", i3);
            bundle2.putInt("width", width);
            bundle2.putInt("height", height);
            bundle2.putInt("visible", i);
        }
        String zzgZ = zzr.zzbF().zzgZ();
        this.zzpj.zzrs = new zzig(zzgZ, this.zzpj.zzrj);
        this.zzpj.zzrs.zzk(adRequestParcel);
        String zza = zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrm, this.zzpj.zzrp);
        long j = 0;
        if (this.zzpj.zzrw != null) {
            try {
                j = this.zzpj.zzrw.getValue();
            } catch (RemoteException e2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Cannot get correlation id, default to 0.");
            }
        }
        String uuid = UUID.randomUUID().toString();
        Bundle zza2 = zzr.zzbF().zza(this.zzpj.context, this, zzgZ);
        List arrayList = new ArrayList();
        for (i = 0; i < this.zzpj.zzrC.size(); i++) {
            arrayList.add(this.zzpj.zzrC.keyAt(i));
        }
        boolean z = this.zzpj.zzrx != null;
        boolean z2 = this.zzpj.zzry != null && zzr.zzbF().zzhj();
        return new zza(bundle2, adRequestParcel, this.zzpj.zzrp, this.zzpj.zzrj, applicationInfo, packageInfo, zzgZ, zzr.zzbF().getSessionId(), this.zzpj.zzrl, zza2, this.zzpj.zzrH, arrayList, bundle, zzr.zzbF().zzhd(), this.mMessenger, displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.density, zza, j, uuid, zzbt.zzdr(), this.zzpj.zzri, this.zzpj.zzrD, new CapabilityParcel(z, z2, this.zzpm.zzpy.zzfM()), this.zzpj.zzca(), zzr.zzbC().zzbt(), zzr.zzbC().zzR(this.zzpj.context), zzr.zzbC().zzl(this.zzpj.zzrm));
    }

    public String getMediationAdapterClassName() {
        return this.zzpj.zzrq == null ? null : this.zzpj.zzrq.zzCr;
    }

    public void onAdClicked() {
        if (this.zzpj.zzrq == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Ad state was null when trying to ping click URLs.");
            return;
        }
        if (!(this.zzpj.zzrq.zzKV == null || this.zzpj.zzrq.zzKV.zzBQ == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzKV.zzBQ);
        }
        if (!(this.zzpj.zzrq.zzCp == null || this.zzpj.zzrq.zzCp.zzBE == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzCp.zzBE);
        }
        super.onAdClicked();
    }

    public void onPause() {
        this.zzpl.zzk(this.zzpj.zzrq);
    }

    public void onResume() {
        this.zzpl.zzl(this.zzpj.zzrq);
    }

    public void pause() {
        zzx.zzcD("pause must be called on the main UI thread.");
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzED == null || !this.zzpj.zzbW())) {
            zzr.zzbE().zzi(this.zzpj.zzrq.zzED);
        }
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCq == null)) {
            try {
                this.zzpj.zzrq.zzCq.pause();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Could not pause mediation adapter.");
            }
        }
        this.zzpl.zzk(this.zzpj.zzrq);
        this.zzpi.pause();
    }

    public void recordImpression() {
        zza(this.zzpj.zzrq, false);
    }

    public void resume() {
        zzx.zzcD("resume must be called on the main UI thread.");
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzED == null || !this.zzpj.zzbW())) {
            zzr.zzbE().zzj(this.zzpj.zzrq.zzED);
        }
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCq == null)) {
            try {
                this.zzpj.zzrq.zzCq.resume();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Could not resume mediation adapter.");
            }
        }
        this.zzpi.resume();
        this.zzpl.zzl(this.zzpj.zzrq);
    }

    public void showInterstitial() {
        throw new IllegalStateException("showInterstitial is not supported for current ad type");
    }

    public void zza(zzgd com_google_android_gms_internal_zzgd) {
        zzx.zzcD("setInAppPurchaseListener must be called on the main UI thread.");
        this.zzpj.zzrx = com_google_android_gms_internal_zzgd;
    }

    public void zza(zzgh com_google_android_gms_internal_zzgh, String str) {
        zzx.zzcD("setPlayStorePurchaseParams must be called on the main UI thread.");
        this.zzpj.zzrI = new zzk(str);
        this.zzpj.zzry = com_google_android_gms_internal_zzgh;
        if (!zzr.zzbF().zzhc() && com_google_android_gms_internal_zzgh != null) {
            new zzc(this.zzpj.context, this.zzpj.zzry, this.zzpj.zzrI).zzhn();
        }
    }

    protected void zza(zzif com_google_android_gms_internal_zzif, boolean z) {
        if (com_google_android_gms_internal_zzif == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Ad state was null when trying to ping impression URLs.");
            return;
        }
        super.zzc(com_google_android_gms_internal_zzif);
        if (!(com_google_android_gms_internal_zzif.zzKV == null || com_google_android_gms_internal_zzif.zzKV.zzBR == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, com_google_android_gms_internal_zzif, this.zzpj.zzrj, z, com_google_android_gms_internal_zzif.zzKV.zzBR);
        }
        if (com_google_android_gms_internal_zzif.zzCp != null && com_google_android_gms_internal_zzif.zzCp.zzBF != null) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, com_google_android_gms_internal_zzif, this.zzpj.zzrj, z, com_google_android_gms_internal_zzif.zzCp.zzBF);
        }
    }

    public void zza(String str, ArrayList<String> arrayList) {
        zzgc com_google_android_gms_ads_internal_purchase_zzd = new zzd(str, arrayList, this.zzpj.context, this.zzpj.zzrl.afmaVersion);
        if (this.zzpj.zzrx == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("InAppPurchaseListener is not set. Try to launch default purchase flow.");
            if (!zzn.zzcS().zzU(this.zzpj.context)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Google Play Service unavailable, cannot launch default purchase flow.");
                return;
            } else if (this.zzpj.zzry == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("PlayStorePurchaseListener is not set.");
                return;
            } else if (this.zzpj.zzrI == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("PlayStorePurchaseVerifier is not initialized.");
                return;
            } else if (this.zzpj.zzrM) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("An in-app purchase request is already in progress, abort");
                return;
            } else {
                this.zzpj.zzrM = true;
                try {
                    if (this.zzpj.zzry.isValidPurchase(str)) {
                        zzr.zzbM().zza(this.zzpj.context, this.zzpj.zzrl.zzNb, new GInAppPurchaseManagerInfoParcel(this.zzpj.context, this.zzpj.zzrI, com_google_android_gms_ads_internal_purchase_zzd, this));
                        return;
                    } else {
                        this.zzpj.zzrM = false;
                        return;
                    }
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaK("Could not start In-App purchase.");
                    this.zzpj.zzrM = false;
                    return;
                }
            }
        }
        try {
            this.zzpj.zzrx.zza(com_google_android_gms_ads_internal_purchase_zzd);
        } catch (RemoteException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Could not start In-App purchase.");
        }
    }

    public void zza(String str, boolean z, int i, final Intent intent, zzf com_google_android_gms_ads_internal_purchase_zzf) {
        try {
            if (this.zzpj.zzry != null) {
                this.zzpj.zzry.zza(new com.google.android.gms.ads.internal.purchase.zzg(this.zzpj.context, str, z, i, intent, com_google_android_gms_ads_internal_purchase_zzf));
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to invoke PlayStorePurchaseListener.");
        }
        zzir.zzMc.postDelayed(new Runnable(this) {
            final /* synthetic */ zzb zzpq;

            public void run() {
                int zzd = zzr.zzbM().zzd(intent);
                zzr.zzbM();
                if (!(zzd != 0 || this.zzpq.zzpj.zzrq == null || this.zzpq.zzpj.zzrq.zzED == null || this.zzpq.zzpj.zzrq.zzED.zzhS() == null)) {
                    this.zzpq.zzpj.zzrq.zzED.zzhS().close();
                }
                this.zzpq.zzpj.zzrM = false;
            }
        }, 500);
    }

    public boolean zza(AdRequestParcel adRequestParcel, zzcb com_google_android_gms_internal_zzcb) {
        if (!zzaV()) {
            return false;
        }
        Bundle zza = zza(zzr.zzbF().zzG(this.zzpj.context));
        this.zzpi.cancel();
        this.zzpj.zzrL = 0;
        zza zza2 = zza(adRequestParcel, zza);
        com_google_android_gms_internal_zzcb.zzc("seq_num", zza2.zzHw);
        com_google_android_gms_internal_zzcb.zzc("request_id", zza2.zzHI);
        com_google_android_gms_internal_zzcb.zzc("session_id", zza2.zzHx);
        if (zza2.zzHu != null) {
            com_google_android_gms_internal_zzcb.zzc("app_version", String.valueOf(zza2.zzHu.versionCode));
        }
        this.zzpj.zzrn = zzr.zzby().zza(this.zzpj.context, zza2, this.zzpj.zzrk, this);
        return true;
    }

    protected boolean zza(AdRequestParcel adRequestParcel, zzif com_google_android_gms_internal_zzif, boolean z) {
        if (!z && this.zzpj.zzbW()) {
            if (com_google_android_gms_internal_zzif.zzBU > 0) {
                this.zzpi.zza(adRequestParcel, com_google_android_gms_internal_zzif.zzBU);
            } else if (com_google_android_gms_internal_zzif.zzKV != null && com_google_android_gms_internal_zzif.zzKV.zzBU > 0) {
                this.zzpi.zza(adRequestParcel, com_google_android_gms_internal_zzif.zzKV.zzBU);
            } else if (!com_google_android_gms_internal_zzif.zzHT && com_google_android_gms_internal_zzif.errorCode == 2) {
                this.zzpi.zzg(adRequestParcel);
            }
        }
        return this.zzpi.zzbw();
    }

    boolean zza(zzif com_google_android_gms_internal_zzif) {
        AdRequestParcel adRequestParcel;
        boolean z = false;
        if (this.zzpk != null) {
            adRequestParcel = this.zzpk;
            this.zzpk = null;
        } else {
            adRequestParcel = com_google_android_gms_internal_zzif.zzHt;
            if (adRequestParcel.extras != null) {
                z = adRequestParcel.extras.getBoolean("_noRefresh", false);
            }
        }
        return zza(adRequestParcel, com_google_android_gms_internal_zzif, z);
    }

    protected boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        int i;
        int i2 = 0;
        if (!(com_google_android_gms_internal_zzif == null || com_google_android_gms_internal_zzif.zzCs == null)) {
            com_google_android_gms_internal_zzif.zzCs.zza(null);
        }
        if (com_google_android_gms_internal_zzif2.zzCs != null) {
            com_google_android_gms_internal_zzif2.zzCs.zza((zzep) this);
        }
        if (com_google_android_gms_internal_zzif2.zzKV != null) {
            i = com_google_android_gms_internal_zzif2.zzKV.zzBZ;
            i2 = com_google_android_gms_internal_zzif2.zzKV.zzCa;
        } else {
            i = 0;
        }
        this.zzpj.zzrJ.zzg(i, i2);
        return true;
    }

    protected boolean zzaV() {
        return zzr.zzbC().zza(this.zzpj.context.getPackageManager(), this.zzpj.context.getPackageName(), "android.permission.INTERNET") && zzr.zzbC().zzI(this.zzpj.context);
    }

    public void zzaW() {
        this.zzpl.zzi(this.zzpj.zzrq);
        this.zzpo = false;
        zzaQ();
        this.zzpj.zzrs.zzgU();
    }

    public void zzaX() {
        this.zzpo = true;
        zzaS();
    }

    public void zzaY() {
        onAdClicked();
    }

    public void zzaZ() {
        zzaW();
    }

    public void zzb(zzif com_google_android_gms_internal_zzif) {
        super.zzb(com_google_android_gms_internal_zzif);
        if (com_google_android_gms_internal_zzif.errorCode == 3 && com_google_android_gms_internal_zzif.zzKV != null && com_google_android_gms_internal_zzif.zzKV.zzBS != null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Pinging no fill URLs.");
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, com_google_android_gms_internal_zzif, this.zzpj.zzrj, false, com_google_android_gms_internal_zzif.zzKV.zzBS);
        }
    }

    public void zzba() {
        zzaO();
    }

    public void zzbb() {
        zzaX();
    }

    public void zzbc() {
        if (this.zzpj.zzrq != null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Mediation adapter " + this.zzpj.zzrq.zzCr + " refreshed, but mediation adapters should never refresh.");
        }
        zza(this.zzpj.zzrq, true);
        zzaT();
    }

    protected boolean zzc(AdRequestParcel adRequestParcel) {
        return super.zzc(adRequestParcel) && !this.zzpo;
    }
}
