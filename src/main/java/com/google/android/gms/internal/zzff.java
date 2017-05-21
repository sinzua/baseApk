package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.common.internal.zzx;

@zzhb
public final class zzff implements MediationBannerListener, MediationInterstitialListener, MediationNativeListener {
    private final zzez zzCK;
    private NativeAdMapper zzCL;

    public zzff(zzez com_google_android_gms_internal_zzez) {
        this.zzCK = com_google_android_gms_internal_zzez;
    }

    public void onAdClicked(MediationBannerAdapter adapter) {
        zzx.zzcD("onAdClicked must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClicked.");
        try {
            this.zzCK.onAdClicked();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClicked.", e);
        }
    }

    public void onAdClicked(MediationInterstitialAdapter adapter) {
        zzx.zzcD("onAdClicked must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClicked.");
        try {
            this.zzCK.onAdClicked();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClicked.", e);
        }
    }

    public void onAdClicked(MediationNativeAdapter adapter) {
        zzx.zzcD("onAdClicked must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClicked.");
        try {
            this.zzCK.onAdClicked();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClicked.", e);
        }
    }

    public void onAdClosed(MediationBannerAdapter adapter) {
        zzx.zzcD("onAdClosed must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClosed.");
        try {
            this.zzCK.onAdClosed();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClosed.", e);
        }
    }

    public void onAdClosed(MediationInterstitialAdapter adapter) {
        zzx.zzcD("onAdClosed must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClosed.");
        try {
            this.zzCK.onAdClosed();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClosed.", e);
        }
    }

    public void onAdClosed(MediationNativeAdapter adapter) {
        zzx.zzcD("onAdClosed must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdClosed.");
        try {
            this.zzCK.onAdClosed();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdClosed.", e);
        }
    }

    public void onAdFailedToLoad(MediationBannerAdapter adapter, int errorCode) {
        zzx.zzcD("onAdFailedToLoad must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdFailedToLoad with error. " + errorCode);
        try {
            this.zzCK.onAdFailedToLoad(errorCode);
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdFailedToLoad.", e);
        }
    }

    public void onAdFailedToLoad(MediationInterstitialAdapter adapter, int errorCode) {
        zzx.zzcD("onAdFailedToLoad must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdFailedToLoad with error " + errorCode + ".");
        try {
            this.zzCK.onAdFailedToLoad(errorCode);
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdFailedToLoad.", e);
        }
    }

    public void onAdFailedToLoad(MediationNativeAdapter adapter, int error) {
        zzx.zzcD("onAdFailedToLoad must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdFailedToLoad with error " + error + ".");
        try {
            this.zzCK.onAdFailedToLoad(error);
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdFailedToLoad.", e);
        }
    }

    public void onAdLeftApplication(MediationBannerAdapter adapter) {
        zzx.zzcD("onAdLeftApplication must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLeftApplication.");
        try {
            this.zzCK.onAdLeftApplication();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLeftApplication.", e);
        }
    }

    public void onAdLeftApplication(MediationInterstitialAdapter adapter) {
        zzx.zzcD("onAdLeftApplication must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLeftApplication.");
        try {
            this.zzCK.onAdLeftApplication();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLeftApplication.", e);
        }
    }

    public void onAdLeftApplication(MediationNativeAdapter adapter) {
        zzx.zzcD("onAdLeftApplication must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLeftApplication.");
        try {
            this.zzCK.onAdLeftApplication();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLeftApplication.", e);
        }
    }

    public void onAdLoaded(MediationBannerAdapter adapter) {
        zzx.zzcD("onAdLoaded must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLoaded.");
        try {
            this.zzCK.onAdLoaded();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLoaded.", e);
        }
    }

    public void onAdLoaded(MediationInterstitialAdapter adapter) {
        zzx.zzcD("onAdLoaded must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLoaded.");
        try {
            this.zzCK.onAdLoaded();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLoaded.", e);
        }
    }

    public void onAdLoaded(MediationNativeAdapter adapter, NativeAdMapper nativeAdMapper) {
        zzx.zzcD("onAdLoaded must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdLoaded.");
        this.zzCL = nativeAdMapper;
        try {
            this.zzCK.onAdLoaded();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdLoaded.", e);
        }
    }

    public void onAdOpened(MediationBannerAdapter adapter) {
        zzx.zzcD("onAdOpened must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdOpened.");
        try {
            this.zzCK.onAdOpened();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdOpened.", e);
        }
    }

    public void onAdOpened(MediationInterstitialAdapter adapter) {
        zzx.zzcD("onAdOpened must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdOpened.");
        try {
            this.zzCK.onAdOpened();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdOpened.", e);
        }
    }

    public void onAdOpened(MediationNativeAdapter adapter) {
        zzx.zzcD("onAdOpened must be called on the main UI thread.");
        zzb.zzaI("Adapter called onAdOpened.");
        try {
            this.zzCK.onAdOpened();
        } catch (Throwable e) {
            zzb.zzd("Could not call onAdOpened.", e);
        }
    }

    public NativeAdMapper zzeJ() {
        return this.zzCL;
    }
}
