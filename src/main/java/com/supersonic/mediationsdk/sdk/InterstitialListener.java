package com.supersonic.mediationsdk.sdk;

import com.supersonic.mediationsdk.logger.SupersonicError;

public interface InterstitialListener {
    void onInterstitialAdClicked();

    void onInterstitialAdClosed();

    void onInterstitialAvailability(boolean z);

    void onInterstitialInitFail(SupersonicError supersonicError);

    void onInterstitialInitSuccess();

    void onInterstitialShowFail(SupersonicError supersonicError);

    void onInterstitialShowSuccess();
}
