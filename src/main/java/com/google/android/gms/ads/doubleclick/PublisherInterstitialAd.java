package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.ads.internal.client.zzac;

public final class PublisherInterstitialAd {
    private final zzac zzoL;

    public PublisherInterstitialAd(Context context) {
        this.zzoL = new zzac(context, this);
    }

    public AdListener getAdListener() {
        return this.zzoL.getAdListener();
    }

    public String getAdUnitId() {
        return this.zzoL.getAdUnitId();
    }

    public AppEventListener getAppEventListener() {
        return this.zzoL.getAppEventListener();
    }

    public String getMediationAdapterClassName() {
        return this.zzoL.getMediationAdapterClassName();
    }

    public OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zzoL.getOnCustomRenderedAdLoadedListener();
    }

    public boolean isLoaded() {
        return this.zzoL.isLoaded();
    }

    public boolean isLoading() {
        return this.zzoL.isLoading();
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(PublisherAdRequest publisherAdRequest) {
        this.zzoL.zza(publisherAdRequest.zzaE());
    }

    public void setAdListener(AdListener adListener) {
        this.zzoL.setAdListener(adListener);
    }

    public void setAdUnitId(String adUnitId) {
        this.zzoL.setAdUnitId(adUnitId);
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        this.zzoL.setAppEventListener(appEventListener);
    }

    public void setCorrelator(Correlator correlator) {
        this.zzoL.setCorrelator(correlator);
    }

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zzoL.setOnCustomRenderedAdLoadedListener(onCustomRenderedAdLoadedListener);
    }

    public void show() {
        this.zzoL.show();
    }
}
