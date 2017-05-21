package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.internal.reward.client.zzg;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.internal.zzcg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzgi;
import com.google.android.gms.internal.zzgm;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzac {
    private final Context mContext;
    private String zzaW;
    private RewardedVideoAdListener zzaX;
    private final zzh zzoB;
    private String zzpS;
    private AdListener zztA;
    private zza zztz;
    private final zzew zzuJ;
    private zzu zzuL;
    private String zzuM;
    private InAppPurchaseListener zzuO;
    private PlayStorePurchaseListener zzuP;
    private OnCustomRenderedAdLoadedListener zzuQ;
    private Correlator zzuR;
    private PublisherInterstitialAd zzuT;
    private boolean zzuU;
    private AppEventListener zzun;

    public zzac(Context context) {
        this(context, zzh.zzcO(), null);
    }

    public zzac(Context context, PublisherInterstitialAd publisherInterstitialAd) {
        this(context, zzh.zzcO(), publisherInterstitialAd);
    }

    public zzac(Context context, zzh com_google_android_gms_ads_internal_client_zzh, PublisherInterstitialAd publisherInterstitialAd) {
        this.zzuJ = new zzew();
        this.mContext = context;
        this.zzoB = com_google_android_gms_ads_internal_client_zzh;
        this.zzuT = publisherInterstitialAd;
    }

    private void zzH(String str) throws RemoteException {
        if (this.zzpS == null) {
            zzI(str);
        }
        this.zzuL = zzn.zzcT().zzb(this.mContext, this.zzuU ? AdSizeParcel.zzcP() : new AdSizeParcel(), this.zzpS, this.zzuJ);
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
        if (this.zzaX != null) {
            this.zzuL.zza(new zzg(this.zzaX));
        }
        if (this.zzaW != null) {
            this.zzuL.setUserId(this.zzaW);
        }
    }

    private void zzI(String str) {
        if (this.zzuL == null) {
            throw new IllegalStateException("The ad unit ID must be set on InterstitialAd before " + str + " is called.");
        }
    }

    public AdListener getAdListener() {
        return this.zztA;
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

    public boolean isLoaded() {
        boolean z = false;
        try {
            if (this.zzuL != null) {
                z = this.zzuL.isReady();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to check if ad is ready.", e);
        }
        return z;
    }

    public boolean isLoading() {
        boolean z = false;
        try {
            if (this.zzuL != null) {
                z = this.zzuL.isLoading();
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to check if ad is loading.", e);
        }
        return z;
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

    public void setAdUnitId(String adUnitId) {
        if (this.zzpS != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on InterstitialAd.");
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

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        try {
            this.zzuQ = onCustomRenderedAdLoadedListener;
            if (this.zzuL != null) {
                this.zzuL.zza(onCustomRenderedAdLoadedListener != null ? new zzcg(onCustomRenderedAdLoadedListener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the OnCustomRenderedAdLoadedListener.", e);
        }
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        if (this.zzuO != null) {
            throw new IllegalStateException("In app purchase parameter has already been set.");
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

    public void setRewardedVideoAdListener(RewardedVideoAdListener listener) {
        try {
            this.zzaX = listener;
            if (this.zzuL != null) {
                this.zzuL.zza(listener != null ? new zzg(listener) : null);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the AdListener.", e);
        }
    }

    public void setUserId(String userId) {
        try {
            this.zzaW = userId;
            if (this.zzuL != null) {
                this.zzuL.setUserId(userId);
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to set the AdListener.", e);
        }
    }

    public void show() {
        try {
            zzI("show");
            this.zzuL.showInterstitial();
        } catch (Throwable e) {
            zzb.zzd("Failed to show interstitial.", e);
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
                zzH("loadAd");
            }
            if (this.zzuL.zzb(this.zzoB.zza(this.mContext, com_google_android_gms_ads_internal_client_zzaa))) {
                this.zzuJ.zzg(com_google_android_gms_ads_internal_client_zzaa.zzdb());
            }
        } catch (Throwable e) {
            zzb.zzd("Failed to load ad.", e);
        }
    }

    public void zza(boolean z) {
        this.zzuU = z;
    }
}
