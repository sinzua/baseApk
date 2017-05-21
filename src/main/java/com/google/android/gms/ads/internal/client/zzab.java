package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzgi;
import com.google.android.gms.internal.zzgm;
import com.google.android.gms.internal.zzhb;
import java.util.concurrent.atomic.AtomicBoolean;

@zzhb
public class zzab {
    private final zzh zzoB;
    private boolean zzpE;
    private String zzpS;
    private AdListener zztA;
    private zza zztz;
    private final zzew zzuJ;
    private final AtomicBoolean zzuK;
    private zzu zzuL;
    private String zzuM;
    private ViewGroup zzuN;
    private InAppPurchaseListener zzuO;
    private PlayStorePurchaseListener zzuP;
    private OnCustomRenderedAdLoadedListener zzuQ;
    private Correlator zzuR;
    private boolean zzuS;
    private AppEventListener zzun;
    private AdSize[] zzuo;

    public zzab(ViewGroup viewGroup) {
        this(viewGroup, null, false, zzh.zzcO(), false);
    }

    public zzab(ViewGroup viewGroup, AttributeSet attributeSet, boolean z) {
        this(viewGroup, attributeSet, z, zzh.zzcO(), false);
    }

    zzab(ViewGroup viewGroup, AttributeSet attributeSet, boolean z, zzh com_google_android_gms_ads_internal_client_zzh, zzu com_google_android_gms_ads_internal_client_zzu, boolean z2) {
        this.zzuJ = new zzew();
        this.zzuN = viewGroup;
        this.zzoB = com_google_android_gms_ads_internal_client_zzh;
        this.zzuL = com_google_android_gms_ads_internal_client_zzu;
        this.zzuK = new AtomicBoolean(false);
        this.zzuS = z2;
        if (attributeSet != null) {
            Context context = viewGroup.getContext();
            try {
                zzk com_google_android_gms_ads_internal_client_zzk = new zzk(context, attributeSet);
                this.zzuo = com_google_android_gms_ads_internal_client_zzk.zzj(z);
                this.zzpS = com_google_android_gms_ads_internal_client_zzk.getAdUnitId();
                if (viewGroup.isInEditMode()) {
                    zzn.zzcS().zza(viewGroup, zza(context, this.zzuo[0], this.zzuS), "Ads by Google");
                }
            } catch (IllegalArgumentException e) {
                zzn.zzcS().zza(viewGroup, new AdSizeParcel(context, AdSize.BANNER), e.getMessage(), e.getMessage());
            }
        }
    }

    zzab(ViewGroup viewGroup, AttributeSet attributeSet, boolean z, zzh com_google_android_gms_ads_internal_client_zzh, boolean z2) {
        this(viewGroup, attributeSet, z, com_google_android_gms_ads_internal_client_zzh, null, z2);
    }

    public zzab(ViewGroup viewGroup, AttributeSet attributeSet, boolean z, boolean z2) {
        this(viewGroup, attributeSet, z, zzh.zzcO(), z2);
    }

    public zzab(ViewGroup viewGroup, boolean z) {
        this(viewGroup, null, false, zzh.zzcO(), z);
    }

    private static AdSizeParcel zza(Context context, AdSize adSize, boolean z) {
        AdSizeParcel adSizeParcel = new AdSizeParcel(context, adSize);
        adSizeParcel.zzi(z);
        return adSizeParcel;
    }

    private static AdSizeParcel zza(Context context, AdSize[] adSizeArr, boolean z) {
        AdSizeParcel adSizeParcel = new AdSizeParcel(context, adSizeArr);
        adSizeParcel.zzi(z);
        return adSizeParcel;
    }

    private void zzdf() {
        try {
            zzd zzaM = this.zzuL.zzaM();
            if (zzaM != null) {
                this.zzuN.addView((View) zze.zzp(zzaM));
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to get an ad frame.", e);
        }
    }

    public void destroy() {
        try {
            if (this.zzuL != null) {
                this.zzuL.destroy();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to destroy AdView.", e);
        }
    }

    public AdListener getAdListener() {
        return this.zztA;
    }

    public AdSize getAdSize() {
        try {
            if (this.zzuL != null) {
                AdSizeParcel zzaN = this.zzuL.zzaN();
                if (zzaN != null) {
                    return zzaN.zzcQ();
                }
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to get the current AdSize.", e);
        }
        return this.zzuo != null ? this.zzuo[0] : null;
    }

    public AdSize[] getAdSizes() {
        return this.zzuo;
    }

    public String getAdUnitId() {
        return this.zzpS;
    }

    public AppEventListener getAppEventListener() {
        return this.zzun;
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zzuO;
    }

    public String getMediationAdapterClassName() {
        try {
            if (this.zzuL != null) {
                return this.zzuL.getMediationAdapterClassName();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to get the mediation adapter class name.", e);
        }
        return null;
    }

    public OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zzuQ;
    }

    public boolean isLoading() {
        try {
            if (this.zzuL != null) {
                return this.zzuL.isLoading();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to check if ad is loading.", e);
        }
        return false;
    }

    public void pause() {
        try {
            if (this.zzuL != null) {
                this.zzuL.pause();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to call pause.", e);
        }
    }

    public void recordManualImpression() {
        if (!this.zzuK.getAndSet(true)) {
            try {
                if (this.zzuL != null) {
                    this.zzuL.zzaP();
                }
            } catch (Throwable e) {
                zzb.zzd("Failed to record impression.", e);
            }
        }
    }

    public void resume() {
        try {
            if (this.zzuL != null) {
                this.zzuL.resume();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to call resume.", e);
        }
    }

    public void setAdListener(AdListener adListener) {
        try {
            this.zztA = adListener;
            if (this.zzuL != null) {
                this.zzuL.zza(adListener != null ? new zzc(adListener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the AdListener.", e);
        }
    }

    public void setAdSizes(AdSize... adSizes) {
        if (this.zzuo != null) {
            throw new IllegalStateException("The ad size can only be set once on AdView.");
        }
        zza(adSizes);
    }

    public void setAdUnitId(String adUnitId) {
        if (this.zzpS != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on AdView.");
        }
        this.zzpS = adUnitId;
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        try {
            this.zzun = appEventListener;
            if (this.zzuL != null) {
                this.zzuL.zza(appEventListener != null ? new zzj(appEventListener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the AppEventListener.", e);
        }
    }

    public void setCorrelator(Correlator correlator) {
        this.zzuR = correlator;
        try {
            if (this.zzuL != null) {
                this.zzuL.zza(this.zzuR == null ? null : this.zzuR.zzaF());
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set correlator.", e);
        }
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        if (this.zzuP != null) {
            throw new IllegalStateException("Play store purchase parameter has already been set.");
        }
        try {
            this.zzuO = inAppPurchaseListener;
            if (this.zzuL != null) {
                this.zzuL.zza(inAppPurchaseListener != null ? new zzgi(inAppPurchaseListener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the InAppPurchaseListener.", e);
        }
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
        this.zzpE = manualImpressionsEnabled;
        try {
            if (this.zzuL != null) {
                this.zzuL.setManualImpressionsEnabled(this.zzpE);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set manual impressions.", e);
        }
    }

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zzuQ = onCustomRenderedAdLoadedListener;
        try {
            if (this.zzuL != null) {
                this.zzuL.zza(onCustomRenderedAdLoadedListener != null ? new zzcg(onCustomRenderedAdLoadedListener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the onCustomRenderedAdLoadedListener.", e);
        }
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        if (this.zzuO != null) {
            throw new IllegalStateException("InAppPurchaseListener has already been set.");
        }
        try {
            this.zzuP = playStorePurchaseListener;
            this.zzuM = publicKey;
            if (this.zzuL != null) {
                this.zzuL.zza(playStorePurchaseListener != null ? new zzgm(playStorePurchaseListener) : null, publicKey);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the play store purchase parameter.", e);
        }
    }

    public void zza(zza com_google_android_gms_ads_internal_client_zza) {
        try {
            this.zztz = com_google_android_gms_ads_internal_client_zza;
            if (this.zzuL != null) {
                this.zzuL.zza(com_google_android_gms_ads_internal_client_zza != null ? new zzb(com_google_android_gms_ads_internal_client_zza) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the AdClickListener.", e);
        }
    }

    public void zza(zzaa com_google_android_gms_ads_internal_client_zzaa) {
        try {
            if (this.zzuL == null) {
                zzdg();
            }
            if (this.zzuL.zzb(this.zzoB.zza(this.zzuN.getContext(), com_google_android_gms_ads_internal_client_zzaa))) {
                this.zzuJ.zzg(com_google_android_gms_ads_internal_client_zzaa.zzdb());
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to load ad.", e);
        }
    }

    public void zza(AdSize... adSizeArr) {
        this.zzuo = adSizeArr;
        try {
            if (this.zzuL != null) {
                this.zzuL.zza(zza(this.zzuN.getContext(), this.zzuo, this.zzuS));
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the ad size.", e);
        }
        this.zzuN.requestLayout();
    }

    void zzdg() throws RemoteException {
        if ((this.zzuo == null || this.zzpS == null) && this.zzuL == null) {
            throw new IllegalStateException("The ad size and ad unit ID must be set before loadAd is called.");
        }
        this.zzuL = zzdh();
        if (this.zztA != null) {
            this.zzuL.zza(new zzc(this.zztA));
        }
        if (this.zztz != null) {
            this.zzuL.zza(new zzb(this.zztz));
        }
        if (this.zzun != null) {
            this.zzuL.zza(new zzj(this.zzun));
        }
        if (this.zzuO != null) {
            this.zzuL.zza(new zzgi(this.zzuO));
        }
        if (this.zzuP != null) {
            this.zzuL.zza(new zzgm(this.zzuP), this.zzuM);
        }
        if (this.zzuQ != null) {
            this.zzuL.zza(new zzcg(this.zzuQ));
        }
        if (this.zzuR != null) {
            this.zzuL.zza(this.zzuR.zzaF());
        }
        this.zzuL.setManualImpressionsEnabled(this.zzpE);
        zzdf();
    }

    protected zzu zzdh() throws RemoteException {
        Context context = this.zzuN.getContext();
        return zzn.zzcT().zza(context, zza(context, this.zzuo, this.zzuS), this.zzpS, this.zzuJ);
    }
}
