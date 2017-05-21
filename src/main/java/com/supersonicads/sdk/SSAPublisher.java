package com.supersonicads.sdk;

import android.app.Activity;
import com.supersonicads.sdk.listeners.OnGenericFunctionListener;
import com.supersonicads.sdk.listeners.OnInterstitialListener;
import com.supersonicads.sdk.listeners.OnOfferWallListener;
import com.supersonicads.sdk.listeners.OnRewardedVideoListener;
import java.util.Map;

public interface SSAPublisher {
    void forceShowInterstitial();

    void getOfferWallCredits(String str, String str2, OnOfferWallListener onOfferWallListener);

    void initInterstitial(String str, String str2, Map<String, String> map, OnInterstitialListener onInterstitialListener);

    void initOfferWall(String str, String str2, Map<String, String> map, OnOfferWallListener onOfferWallListener);

    void initRewardedVideo(String str, String str2, Map<String, String> map, OnRewardedVideoListener onRewardedVideoListener);

    boolean isInterstitialAdAvailable();

    void onPause(Activity activity);

    void onResume(Activity activity);

    void release(Activity activity);

    void runGenericFunction(String str, Map<String, String> map, OnGenericFunctionListener onGenericFunctionListener);

    void showInterstitial();

    void showOfferWall(Map<String, String> map);

    void showRewardedVideo();
}
