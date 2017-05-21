package com.supersonicads.sdk.listeners;

import com.supersonicads.sdk.data.AdUnitsReady;

public interface OnRewardedVideoListener {
    void onRVAdClosed();

    void onRVAdCredited(int i);

    void onRVAdOpened();

    void onRVGeneric(String str, String str2);

    void onRVInitFail(String str);

    void onRVInitSuccess(AdUnitsReady adUnitsReady);

    void onRVNoMoreOffers();

    void onRVShowFail(String str);
}
