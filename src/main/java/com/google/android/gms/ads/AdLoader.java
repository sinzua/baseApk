package com.google.android.gms.ads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomClickListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener;
import com.google.android.gms.ads.internal.client.zzaa;
import com.google.android.gms.ads.internal.client.zzc;
import com.google.android.gms.ads.internal.client.zzd;
import com.google.android.gms.ads.internal.client.zzh;
import com.google.android.gms.ads.internal.client.zzr;
import com.google.android.gms.ads.internal.client.zzs;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzcw;
import com.google.android.gms.internal.zzcx;
import com.google.android.gms.internal.zzcy;
import com.google.android.gms.internal.zzcz;
import com.google.android.gms.internal.zzew;

public class AdLoader {
    private final Context mContext;
    private final zzh zzoB;
    private final zzr zzoC;

    public static class Builder {
        private final Context mContext;
        private final zzs zzoD;

        Builder(Context context, zzs builder) {
            this.mContext = context;
            this.zzoD = builder;
        }

        public Builder(Context context, String adUnitID) {
            this((Context) zzx.zzb((Object) context, (Object) "context cannot be null"), zzd.zza(context, adUnitID, new zzew()));
        }

        public AdLoader build() {
            try {
                return new AdLoader(this.mContext, this.zzoD.zzbn());
            } catch (Throwable e) {
                zzb.zzb("Failed to build AdLoader.", e);
                return null;
            }
        }

        public Builder forAppInstallAd(OnAppInstallAdLoadedListener listener) {
            try {
                this.zzoD.zza(new zzcw(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add app install ad listener", e);
            }
            return this;
        }

        public Builder forContentAd(OnContentAdLoadedListener listener) {
            try {
                this.zzoD.zza(new zzcx(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add content ad listener", e);
            }
            return this;
        }

        public Builder forCustomTemplateAd(String templateId, OnCustomTemplateAdLoadedListener adLoadedListener, OnCustomClickListener customClickListener) {
            try {
                this.zzoD.zza(templateId, new zzcz(adLoadedListener), customClickListener == null ? null : new zzcy(customClickListener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add custom template ad listener", e);
            }
            return this;
        }

        public Builder withAdListener(AdListener listener) {
            try {
                this.zzoD.zzb(new zzc(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to set AdListener.", e);
            }
            return this;
        }

        public Builder withCorrelator(@NonNull Correlator correlator) {
            zzx.zzz(correlator);
            try {
                this.zzoD.zzb(correlator.zzaF());
            } catch (Throwable e) {
                zzb.zzd("Failed to set correlator.", e);
            }
            return this;
        }

        public Builder withNativeAdOptions(NativeAdOptions options) {
            try {
                this.zzoD.zza(new NativeAdOptionsParcel(options));
            } catch (Throwable e) {
                zzb.zzd("Failed to specify native ad options", e);
            }
            return this;
        }
    }

    AdLoader(Context context, zzr adLoader) {
        this(context, adLoader, zzh.zzcO());
    }

    AdLoader(Context context, zzr adLoader, zzh parcelFactory) {
        this.mContext = context;
        this.zzoC = adLoader;
        this.zzoB = parcelFactory;
    }

    private void zza(zzaa com_google_android_gms_ads_internal_client_zzaa) {
        try {
            this.zzoC.zzf(this.zzoB.zza(this.mContext, com_google_android_gms_ads_internal_client_zzaa));
        } catch (Throwable e) {
            zzb.zzb("Failed to load ad.", e);
        }
    }

    public String getMediationAdapterClassName() {
        try {
            return this.zzoC.getMediationAdapterClassName();
        } catch (Throwable e) {
            zzb.zzd("Failed to get the mediation adapter class name.", e);
            return null;
        }
    }

    public boolean isLoading() {
        try {
            return this.zzoC.isLoading();
        } catch (Throwable e) {
            zzb.zzd("Failed to check if ad is loading.", e);
            return false;
        }
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(AdRequest adRequest) {
        zza(adRequest.zzaE());
    }

    public void loadAd(PublisherAdRequest publisherAdRequest) {
        zza(publisherAdRequest.zzaE());
    }
}
