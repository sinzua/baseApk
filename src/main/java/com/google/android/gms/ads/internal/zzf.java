package com.google.android.gms.ads.internal;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzjk;
import com.google.android.gms.internal.zzjp;
import com.google.android.gms.internal.zzjq;
import java.util.List;

@zzhb
public class zzf extends zzc implements OnGlobalLayoutListener, OnScrollChangedListener {
    private boolean zzpE;

    public class zza {
        final /* synthetic */ zzf zzpG;

        public zza(zzf com_google_android_gms_ads_internal_zzf) {
            this.zzpG = com_google_android_gms_ads_internal_zzf;
        }

        public void onClick() {
            this.zzpG.onAdClicked();
        }
    }

    public zzf(Context context, AdSizeParcel adSizeParcel, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        super(context, adSizeParcel, str, com_google_android_gms_internal_zzex, versionInfoParcel, com_google_android_gms_ads_internal_zzd);
    }

    private AdSizeParcel zzb(com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza) {
        if (com_google_android_gms_internal_zzif_zza.zzLe.zzul) {
            return this.zzpj.zzrp;
        }
        AdSize adSize;
        String str = com_google_android_gms_internal_zzif_zza.zzLe.zzHW;
        if (str != null) {
            String[] split = str.split("[xX]");
            split[0] = split[0].trim();
            split[1] = split[1].trim();
            adSize = new AdSize(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } else {
            adSize = this.zzpj.zzrp.zzcQ();
        }
        return new AdSizeParcel(this.zzpj.context, adSize);
    }

    private boolean zzb(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        if (com_google_android_gms_internal_zzif2.zzHT) {
            View zzf = zzm.zzf(com_google_android_gms_internal_zzif2);
            if (zzf == null) {
                zzb.zzaK("Could not get mediation view");
                return false;
            }
            View nextView = this.zzpj.zzrm.getNextView();
            if (nextView != null) {
                if (nextView instanceof zzjp) {
                    ((zzjp) nextView).destroy();
                }
                this.zzpj.zzrm.removeView(nextView);
            }
            if (!zzm.zzg(com_google_android_gms_internal_zzif2)) {
                try {
                    zzb(zzf);
                } catch (Throwable th) {
                    zzb.zzd("Could not add mediation view to view hierarchy.", th);
                    return false;
                }
            }
        } else if (!(com_google_android_gms_internal_zzif2.zzKW == null || com_google_android_gms_internal_zzif2.zzED == null)) {
            com_google_android_gms_internal_zzif2.zzED.zza(com_google_android_gms_internal_zzif2.zzKW);
            this.zzpj.zzrm.removeAllViews();
            this.zzpj.zzrm.setMinimumWidth(com_google_android_gms_internal_zzif2.zzKW.widthPixels);
            this.zzpj.zzrm.setMinimumHeight(com_google_android_gms_internal_zzif2.zzKW.heightPixels);
            zzb(com_google_android_gms_internal_zzif2.zzED.getView());
        }
        if (this.zzpj.zzrm.getChildCount() > 1) {
            this.zzpj.zzrm.showNext();
        }
        if (com_google_android_gms_internal_zzif != null) {
            View nextView2 = this.zzpj.zzrm.getNextView();
            if (nextView2 instanceof zzjp) {
                ((zzjp) nextView2).zza(this.zzpj.context, this.zzpj.zzrp, this.zzpe);
            } else if (nextView2 != null) {
                this.zzpj.zzrm.removeView(nextView2);
            }
            this.zzpj.zzbV();
        }
        this.zzpj.zzrm.setVisibility(0);
        return true;
    }

    private void zzd(final zzif com_google_android_gms_internal_zzif) {
        if (this.zzpj.zzbW()) {
            if (com_google_android_gms_internal_zzif.zzED != null) {
                if (com_google_android_gms_internal_zzif.zzKT != null) {
                    this.zzpl.zza(this.zzpj.zzrp, com_google_android_gms_internal_zzif);
                }
                if (com_google_android_gms_internal_zzif.zzcv()) {
                    this.zzpl.zza(this.zzpj.zzrp, com_google_android_gms_internal_zzif).zza(com_google_android_gms_internal_zzif.zzED);
                } else {
                    com_google_android_gms_internal_zzif.zzED.zzhU().zza(new zzjq.zzb(this) {
                        final /* synthetic */ zzf zzpG;

                        public void zzbi() {
                            this.zzpG.zzpl.zza(this.zzpG.zzpj.zzrp, com_google_android_gms_internal_zzif).zza(com_google_android_gms_internal_zzif.zzED);
                        }
                    });
                }
            }
        } else if (this.zzpj.zzrK != null && com_google_android_gms_internal_zzif.zzKT != null) {
            this.zzpl.zza(this.zzpj.zzrp, com_google_android_gms_internal_zzif, this.zzpj.zzrK);
        }
    }

    public void onGlobalLayout() {
        zze(this.zzpj.zzrq);
    }

    public void onScrollChanged() {
        zze(this.zzpj.zzrq);
    }

    public void setManualImpressionsEnabled(boolean enabled) {
        zzx.zzcD("setManualImpressionsEnabled must be called from the main thread.");
        this.zzpE = enabled;
    }

    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by BannerAdManager.");
    }

    protected zzjp zza(com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza, zze com_google_android_gms_ads_internal_zze) {
        if (this.zzpj.zzrp.zzul) {
            this.zzpj.zzrp = zzb(com_google_android_gms_internal_zzif_zza);
        }
        return super.zza(com_google_android_gms_internal_zzif_zza, com_google_android_gms_ads_internal_zze);
    }

    protected void zza(zzif com_google_android_gms_internal_zzif, boolean z) {
        super.zza(com_google_android_gms_internal_zzif, z);
        if (zzm.zzg(com_google_android_gms_internal_zzif)) {
            zzm.zza(com_google_android_gms_internal_zzif, new zza(this));
        }
    }

    public boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        if (!super.zza(com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzif2)) {
            return false;
        }
        if (!this.zzpj.zzbW() || zzb(com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzif2)) {
            if (com_google_android_gms_internal_zzif2.zzIm) {
                zze(com_google_android_gms_internal_zzif2);
                zzjk.zza(this.zzpj.zzrm, (OnGlobalLayoutListener) this);
                zzjk.zza(this.zzpj.zzrm, (OnScrollChangedListener) this);
            } else if (!this.zzpj.zzbX() || ((Boolean) zzbt.zzxg.get()).booleanValue()) {
                zza(com_google_android_gms_internal_zzif2, false);
            }
            zzd(com_google_android_gms_internal_zzif2);
            return true;
        }
        zzf(0);
        return false;
    }

    protected boolean zzaV() {
        boolean z = true;
        if (!zzr.zzbC().zza(this.zzpj.context.getPackageManager(), this.zzpj.context.getPackageName(), "android.permission.INTERNET")) {
            zzn.zzcS().zza(this.zzpj.zzrm, this.zzpj.zzrp, "Missing internet permission in AndroidManifest.xml.", "Missing internet permission in AndroidManifest.xml. You must have the following declaration: <uses-permission android:name=\"android.permission.INTERNET\" />");
            z = false;
        }
        if (!zzr.zzbC().zzI(this.zzpj.context)) {
            zzn.zzcS().zza(this.zzpj.zzrm, this.zzpj.zzrp, "Missing AdActivity with android:configChanges in AndroidManifest.xml.", "Missing AdActivity with android:configChanges in AndroidManifest.xml. You must have the following declaration within the <application> element: <activity android:name=\"com.google.android.gms.ads.AdActivity\" android:configChanges=\"keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize\" />");
            z = false;
        }
        if (!(z || this.zzpj.zzrm == null)) {
            this.zzpj.zzrm.setVisibility(0);
        }
        return z;
    }

    public boolean zzb(AdRequestParcel adRequestParcel) {
        return super.zzb(zze(adRequestParcel));
    }

    AdRequestParcel zze(AdRequestParcel adRequestParcel) {
        if (adRequestParcel.zztH == this.zzpE) {
            return adRequestParcel;
        }
        int i = adRequestParcel.versionCode;
        long j = adRequestParcel.zztC;
        Bundle bundle = adRequestParcel.extras;
        int i2 = adRequestParcel.zztD;
        List list = adRequestParcel.zztE;
        boolean z = adRequestParcel.zztF;
        int i3 = adRequestParcel.zztG;
        boolean z2 = adRequestParcel.zztH || this.zzpE;
        return new AdRequestParcel(i, j, bundle, i2, list, z, i3, z2, adRequestParcel.zztI, adRequestParcel.zztJ, adRequestParcel.zztK, adRequestParcel.zztL, adRequestParcel.zztM, adRequestParcel.zztN, adRequestParcel.zztO, adRequestParcel.zztP, adRequestParcel.zztQ, adRequestParcel.zztR);
    }

    void zze(zzif com_google_android_gms_internal_zzif) {
        if (com_google_android_gms_internal_zzif != null && !com_google_android_gms_internal_zzif.zzKU && this.zzpj.zzrm != null && zzr.zzbC().zza(this.zzpj.zzrm, this.zzpj.context) && this.zzpj.zzrm.getGlobalVisibleRect(new Rect(), null)) {
            zza(com_google_android_gms_internal_zzif, false);
            com_google_android_gms_internal_zzif.zzKU = true;
        }
    }
}
