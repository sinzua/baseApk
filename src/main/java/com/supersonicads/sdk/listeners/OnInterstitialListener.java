package com.supersonicads.sdk.listeners;

public interface OnInterstitialListener {
    void onInterstitialAdClicked();

    void onInterstitialAdClosed();

    void onInterstitialAvailability(boolean z);

    void onInterstitialInitFail(String str);

    void onInterstitialInitSuccess();

    void onInterstitialShowFail(String str);

    void onInterstitialShowSuccess();
}
