package com.supersonic.mediationsdk.sdk;

import android.app.Activity;

public interface InterstitialApi extends BaseApi {
    void initInterstitial(Activity activity, String str, String str2);

    boolean isInterstitialAdAvailable();

    void setInterstitialListener(InterstitialListener interstitialListener);

    void showInterstitial();
}
