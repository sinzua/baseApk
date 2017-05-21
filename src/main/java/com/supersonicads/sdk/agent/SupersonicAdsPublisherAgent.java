package com.supersonicads.sdk.agent;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Handler;
import android.os.Looper;
import com.supersonicads.sdk.SSAPublisher;
import com.supersonicads.sdk.controller.SupersonicWebView;
import com.supersonicads.sdk.data.SSASession;
import com.supersonicads.sdk.data.SSASession.SessionType;
import com.supersonicads.sdk.listeners.OnGenericFunctionListener;
import com.supersonicads.sdk.listeners.OnInterstitialListener;
import com.supersonicads.sdk.listeners.OnOfferWallListener;
import com.supersonicads.sdk.listeners.OnRewardedVideoListener;
import com.supersonicads.sdk.utils.Constants;
import com.supersonicads.sdk.utils.DeviceProperties;
import com.supersonicads.sdk.utils.Logger;
import com.supersonicads.sdk.utils.SDKUtils;
import com.supersonicads.sdk.utils.SupersonicAsyncHttpRequestTask;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import java.util.Map;

public final class SupersonicAdsPublisherAgent implements SSAPublisher {
    private static final String TAG = "SupersonicAdsPublisherAgent";
    private static MutableContextWrapper mutableContextWrapper;
    private static SupersonicAdsPublisherAgent sInstance;
    private SSASession session;
    private SupersonicWebView wvc;

    private SupersonicAdsPublisherAgent(final Activity activity, int debugMode) {
        SupersonicSharedPrefHelper.getSupersonicPrefHelper(activity);
        Logger.enableLogging(SDKUtils.getDebugMode());
        Logger.i(TAG, "C'tor");
        mutableContextWrapper = new MutableContextWrapper(activity);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                SupersonicAdsPublisherAgent.this.wvc = new SupersonicWebView(SupersonicAdsPublisherAgent.mutableContextWrapper);
                SupersonicAdsPublisherAgent.this.wvc.registerConnectionReceiver(activity);
                SupersonicAdsPublisherAgent.this.wvc.setDebugMode(SDKUtils.getDebugMode());
                SupersonicAdsPublisherAgent.this.wvc.downloadController();
            }
        });
        startSession(activity);
    }

    public static synchronized SupersonicAdsPublisherAgent getInstance(Activity activity) {
        SupersonicAdsPublisherAgent instance;
        synchronized (SupersonicAdsPublisherAgent.class) {
            instance = getInstance(activity, 0);
        }
        return instance;
    }

    public static synchronized SupersonicAdsPublisherAgent getInstance(Activity activity, int debugMode) {
        SupersonicAdsPublisherAgent supersonicAdsPublisherAgent;
        synchronized (SupersonicAdsPublisherAgent.class) {
            Logger.i(TAG, "getInstance()");
            if (sInstance == null) {
                sInstance = new SupersonicAdsPublisherAgent(activity, debugMode);
            } else {
                mutableContextWrapper.setBaseContext(activity);
            }
            supersonicAdsPublisherAgent = sInstance;
        }
        return supersonicAdsPublisherAgent;
    }

    public SupersonicWebView getWebViewController() {
        return this.wvc;
    }

    private void startSession(Context context) {
        this.session = new SSASession(context, SessionType.launched);
    }

    public void resumeSession(Context context) {
        this.session = new SSASession(context, SessionType.backFromBG);
    }

    private void endSession() {
        if (this.session != null) {
            this.session.endSession();
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().addSession(this.session);
            this.session = null;
        }
    }

    public void initRewardedVideo(String applicationKey, String userId, Map<String, String> extraParameters, OnRewardedVideoListener listener) {
        this.wvc.initBrandConnect(applicationKey, userId, extraParameters, listener);
    }

    public void showRewardedVideo() {
        this.wvc.showBrandConnect();
    }

    public void initOfferWall(String applicationKey, String userId, Map<String, String> extraParameters, OnOfferWallListener listener) {
        this.wvc.initOfferWall(applicationKey, userId, extraParameters, listener);
    }

    public void showOfferWall(Map<String, String> extraParameters) {
        this.wvc.showOfferWall(extraParameters);
    }

    public void getOfferWallCredits(String applicationKey, String userId, OnOfferWallListener listener) {
        this.wvc.getOfferWallCredits(applicationKey, userId, listener);
    }

    public void initInterstitial(String applicationKey, String userId, Map<String, String> extraParameters, OnInterstitialListener listener) {
        this.wvc.initInterstitial(applicationKey, userId, extraParameters, listener);
    }

    public boolean isInterstitialAdAvailable() {
        return this.wvc.isInterstitialAdAvailable();
    }

    public void showInterstitial() {
        this.wvc.showInterstitial();
    }

    public void forceShowInterstitial() {
        this.wvc.forceShowInterstitial();
    }

    public void onResume(Activity activity) {
        mutableContextWrapper.setBaseContext(activity);
        this.wvc.enterForeground();
        this.wvc.registerConnectionReceiver(activity);
        if (this.session == null) {
            resumeSession(activity);
        }
    }

    public void onPause(Activity activity) {
        try {
            this.wvc.enterBackground();
            this.wvc.unregisterConnectionReceiver(activity);
            endSession();
        } catch (Exception e) {
            e.printStackTrace();
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
        }
    }

    public void release(Activity activity) {
        try {
            Logger.i(TAG, "release()");
            DeviceProperties.release();
            this.wvc.unregisterConnectionReceiver(activity);
            if (Looper.getMainLooper().equals(Looper.myLooper())) {
                this.wvc.destroy();
                this.wvc = null;
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        SupersonicAdsPublisherAgent.this.wvc.destroy();
                        SupersonicAdsPublisherAgent.this.wvc = null;
                    }
                });
            }
        } catch (Exception e) {
        }
        sInstance = null;
        endSession();
    }

    public void runGenericFunction(String method, Map<String, String> keyValPairs, OnGenericFunctionListener listener) {
        this.wvc.runGenericFunction(method, keyValPairs, listener);
    }
}
