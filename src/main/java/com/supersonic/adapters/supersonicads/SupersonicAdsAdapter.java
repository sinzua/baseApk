package com.supersonic.adapters.supersonicads;

import android.app.Activity;
import android.text.TextUtils;
import com.supersonic.mediationsdk.AbstractAdapter;
import com.supersonic.mediationsdk.SupersonicObject;
import com.supersonic.mediationsdk.events.EventsHelper;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.RewardedVideoHelper;
import com.supersonic.mediationsdk.sdk.InterstitialApi;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.OfferwallApi;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.RewardedVideoAdapterApi;
import com.supersonic.mediationsdk.sdk.RewardedVideoManagerListener;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import com.supersonicads.sdk.SSAFactory;
import com.supersonicads.sdk.SSAPublisher;
import com.supersonicads.sdk.data.AdUnitsReady;
import com.supersonicads.sdk.listeners.OnInterstitialListener;
import com.supersonicads.sdk.listeners.OnOfferWallListener;
import com.supersonicads.sdk.listeners.OnRewardedVideoListener;
import com.supersonicads.sdk.utils.SDKUtils;
import java.util.HashMap;
import java.util.Map;

class SupersonicAdsAdapter extends AbstractAdapter implements RewardedVideoAdapterApi, InterstitialApi, OfferwallApi, OnOfferWallListener {
    private static SupersonicAdsAdapter mInstance;
    private final String ITEM_SIGNATURE = "itemSignature";
    private final String SDK_PLUGIN_TYPE = "SDKPluginType";
    private String TAG = SupersonicAdsAdapter.class.getSimpleName();
    private final String TIMESTAMP = "timestamp";
    private final String VERSION = "6.3.8";
    private InterstitialListener mInterstitialListener;
    private OfferwallListener mOfferwallListener;
    private RewardedVideoHelper mRewardedVideoHelper = new RewardedVideoHelper();
    private RewardedVideoManagerListener mRewardedVideoManager;
    private SSAPublisher mSSAPublisher;

    public static SupersonicAdsAdapter getInstance(String providerName, String providerUrl) {
        if (mInstance == null) {
            mInstance = new SupersonicAdsAdapter(providerName, providerUrl);
        }
        return mInstance;
    }

    private SupersonicAdsAdapter(String providerName, String providerUrl) {
        super(providerName, providerUrl);
        setInterstitialSupport(true);
        setOfferwallSupport(true);
        SDKUtils.setControllerUrl(SupersonicConfig.getConfigObj().getDynamicControllerUrl());
        SDKUtils.setDebugMode(SupersonicConfig.getConfigObj().getDebugMode());
        SDKUtils.setControllerConfig(SupersonicConfig.getConfigObj().getControllerConfig());
    }

    public int getMaxVideosPerIteration() {
        return SupersonicConfig.getConfigObj().getMaxVideosPerIteration();
    }

    public String getVersion() {
        return "6.3.8";
    }

    public String getCoreSDKVersion() {
        return SDKUtils.getSDKVersion();
    }

    private HashMap<String, String> getGenenralExtraParams() {
        HashMap<String, String> params = new HashMap();
        SupersonicConfig config = SupersonicConfig.getConfigObj();
        String ageGroup = config.getUserAgeGroup();
        if (!TextUtils.isEmpty(ageGroup)) {
            params.put(SupersonicConfig.APPLICATION_USER_AGE_GROUP, ageGroup);
        }
        String uGender = config.getUserGender();
        if (!TextUtils.isEmpty(uGender)) {
            params.put(SupersonicConfig.APPLICATION_USER_GENDER, uGender);
        }
        String pluginType = config.getPluginType();
        if (!TextUtils.isEmpty(pluginType)) {
            params.put("SDKPluginType", pluginType);
        }
        return params;
    }

    private HashMap<String, String> getRewardedVideoExtraParams() {
        HashMap<String, String> rvExtraParams = getGenenralExtraParams();
        SupersonicConfig config = SupersonicConfig.getConfigObj();
        String language = config.getLanguage();
        if (!SupersonicUtils.isEmpty(language)) {
            rvExtraParams.put("language", language);
        }
        String maxVideoLength = config.getMaxVideoLength();
        if (!SupersonicUtils.isEmpty(maxVideoLength)) {
            rvExtraParams.put(SupersonicConfig.MAX_VIDEO_LENGTH, maxVideoLength);
        }
        String campaignId = config.getCampaignId();
        if (!SupersonicUtils.isEmpty(campaignId)) {
            rvExtraParams.put(SupersonicConfig.CAMPAIGN_ID, campaignId);
        }
        addItemNameCountSignature(rvExtraParams);
        Map<String, String> customParams = config.getRewardedVideoCustomParams();
        if (!(customParams == null || customParams.isEmpty())) {
            rvExtraParams.putAll(customParams);
        }
        return rvExtraParams;
    }

    private HashMap<String, String> getInterstitialExtraParams() {
        return getGenenralExtraParams();
    }

    private HashMap<String, String> getOfferwallExtraParams() {
        HashMap<String, String> owExtraParams = getGenenralExtraParams();
        SupersonicConfig config = SupersonicConfig.getConfigObj();
        String language = config.getLanguage();
        if (!SupersonicUtils.isEmpty(language)) {
            owExtraParams.put("language", language);
        }
        owExtraParams.put("useClientSideCallbacks", String.valueOf(config.getClientSideCallbacks()));
        Map<String, String> customParams = config.getOfferwallCustomParams();
        if (!(customParams == null || customParams.isEmpty())) {
            owExtraParams.putAll(customParams);
        }
        addItemNameCountSignature(owExtraParams);
        return owExtraParams;
    }

    private void addItemNameCountSignature(HashMap<String, String> params) {
        try {
            SupersonicConfig config = SupersonicConfig.getConfigObj();
            String itemName = config.getItemName();
            int itemCount = config.getItemCount();
            String privateKey = config.getPrivateKey();
            boolean shouldAddSignature = true;
            if (SupersonicUtils.isEmpty(itemName)) {
                shouldAddSignature = false;
            } else {
                params.put(SupersonicConfig.ITEM_NAME, itemName);
            }
            if (SupersonicUtils.isEmpty(privateKey)) {
                shouldAddSignature = false;
            }
            if (itemCount == -1) {
                shouldAddSignature = false;
            } else {
                params.put(SupersonicConfig.ITEM_COUNT, String.valueOf(itemCount));
            }
            if (shouldAddSignature) {
                int timestamp = SupersonicUtils.getCurrentTimestamp();
                params.put("timestamp", String.valueOf(timestamp));
                params.put("itemSignature", createItemSig(timestamp, itemName, itemCount, privateKey));
            }
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.ADAPTER_API, this.TAG + ":addItemNameCountSignature", e);
        }
    }

    private String createItemSig(int timestamp, String itemName, int itemCount, String privateKey) {
        return SupersonicUtils.getMD5(timestamp + itemName + itemCount + privateKey);
    }

    private String createMinimumOfferCommissionSig(double min, String privateKey) {
        return SupersonicUtils.getMD5(min + privateKey);
    }

    private String createUserCreationDateSig(String userid, String uCreationDate, String privateKey) {
        return SupersonicUtils.getMD5(userid + uCreationDate + privateKey);
    }

    public void initRewardedVideo(final Activity activity, final String appKey, final String userId) {
        EventsHelper.getInstance().notifyInitRewardedVideoEvent(getProviderName());
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":initRewardedVideo(userId:" + userId + ")", 1);
        this.mRewardedVideoHelper.reset();
        if (validateConfigBeforeInitAndCallInitFailForInvalid(SupersonicConfig.getConfigObj(), this.mRewardedVideoManager).isValid()) {
            this.mRewardedVideoHelper.setMaxVideo(SupersonicConfig.getConfigObj().getMaxVideos());
            startTimer(this.mRewardedVideoManager);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    SupersonicAdsAdapter.this.mSSAPublisher = SSAFactory.getPublisherInstance(activity);
                    HashMap<String, String> rewardedVideoExtraParams = SupersonicAdsAdapter.this.getRewardedVideoExtraParams();
                    SupersonicAdsAdapter.this.log(SupersonicTag.ADAPTER_API, SupersonicAdsAdapter.this.getProviderName() + ":initRewardedVideo(appKey:" + appKey + ", userId:" + userId + ", extraParams:" + rewardedVideoExtraParams + ")", 1);
                    SupersonicAdsAdapter.this.mSSAPublisher.initRewardedVideo(appKey, userId, rewardedVideoExtraParams, new OnRewardedVideoListener() {
                        public void onRVNoMoreOffers() {
                            SupersonicAdsAdapter.this.cancelTimer();
                            boolean shouldNotify = SupersonicAdsAdapter.this.mRewardedVideoHelper.setVideoAvailability(false);
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null && shouldNotify) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoInitSuccess(SupersonicAdsAdapter.this);
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onVideoAvailabilityChanged(SupersonicAdsAdapter.this.mRewardedVideoHelper.isVideoAvailable(), SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVInitSuccess(AdUnitsReady aur) {
                            boolean availability = true;
                            SupersonicAdsAdapter.this.cancelTimer();
                            SupersonicAdsAdapter.this.setRVInitStatus(true);
                            int numOfAdUnits = 0;
                            try {
                                numOfAdUnits = Integer.parseInt(aur.getNumOfAdUnits());
                            } catch (NumberFormatException e) {
                                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "onRVInitSuccess:parseInt()", e);
                            }
                            if (numOfAdUnits <= 0) {
                                availability = false;
                            }
                            boolean shouldNotify = SupersonicAdsAdapter.this.mRewardedVideoHelper.setVideoAvailability(availability);
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoInitSuccess(SupersonicAdsAdapter.this);
                                if (shouldNotify) {
                                    SupersonicAdsAdapter.this.mRewardedVideoManager.onVideoAvailabilityChanged(SupersonicAdsAdapter.this.mRewardedVideoHelper.isVideoAvailable(), SupersonicAdsAdapter.this);
                                }
                            }
                        }

                        public void onRVInitFail(String error) {
                            SupersonicAdsAdapter.this.setRVInitStatus(false);
                            SupersonicAdsAdapter.this.cancelTimer();
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoInitFail(ErrorBuilder.buildAdapterInitFailedError(error), SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVShowFail(String error) {
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoShowFail(ErrorBuilder.buildShowVideoFailedError(error), SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVAdCredited(int amount) {
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoAdRewarded(SupersonicAdsAdapter.this.mPlacementHolder.getRewardedVideoPlacement(SupersonicAdsAdapter.this.mRewardedVideoHelper.getPlacementName()), SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVAdClosed() {
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoAdClosed(SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVAdOpened() {
                            if (SupersonicAdsAdapter.this.mRewardedVideoManager != null) {
                                SupersonicAdsAdapter.this.mRewardedVideoManager.onRewardedVideoAdOpened(SupersonicAdsAdapter.this);
                            }
                        }

                        public void onRVGeneric(String s, String s1) {
                        }
                    });
                }
            });
        }
    }

    public void onPause(Activity activity) {
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":onPause()", 1);
        if (this.mSSAPublisher != null) {
            this.mSSAPublisher.onPause(activity);
        }
    }

    public void setAge(int age) {
        SupersonicConfig.getConfigObj().setUserAgeGroup(age);
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":setAge(age:" + age + ")", 1);
    }

    public void setGender(String gender) {
        SupersonicConfig.getConfigObj().setUserGender(gender);
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":setGender(gender:" + gender + ")", 1);
    }

    public void onResume(Activity activity) {
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":onResume()", 1);
        if (this.mSSAPublisher != null) {
            this.mSSAPublisher.onResume(activity);
        }
    }

    public void release(Activity activity) {
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":release()", 1);
        if (this.mSSAPublisher != null) {
            this.mSSAPublisher.release(activity);
        }
    }

    public synchronized boolean isRewardedVideoAvailable() {
        boolean availability;
        availability = this.mRewardedVideoHelper.isVideoAvailable();
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":isRewardedVideoAvailable():" + availability, 1);
        return availability;
    }

    public void setRewardedVideoListener(RewardedVideoManagerListener rewardedVideoManager) {
        this.mRewardedVideoManager = rewardedVideoManager;
    }

    public void showRewardedVideo() {
    }

    public void showRewardedVideo(String placementName) {
        EventsHelper.getInstance().notifyShowRewardedVideoEvent(getProviderName(), placementName);
        if (this.mSSAPublisher != null) {
            log(SupersonicTag.ADAPTER_API, getProviderName() + ":showRewardedVideo(placement:" + placementName + ")", 1);
            this.mSSAPublisher.showRewardedVideo();
            this.mRewardedVideoHelper.setPlacementName(placementName);
            boolean shouldNotify = this.mRewardedVideoHelper.increaseCurrentVideo();
            if (this.mRewardedVideoManager != null && shouldNotify) {
                this.mRewardedVideoManager.onVideoAvailabilityChanged(this.mRewardedVideoHelper.isVideoAvailable(), this);
                return;
            }
            return;
        }
        log(SupersonicTag.NATIVE, "Please call init before calling showRewardedVideo", 2);
    }

    public void setInterstitialListener(InterstitialListener isListener) {
        this.mInterstitialListener = isListener;
    }

    public boolean isInterstitialAdAvailable() {
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":isInterstitialAdAvailable()", 1);
        if (this.mSSAPublisher != null) {
            return this.mSSAPublisher.isInterstitialAdAvailable();
        }
        log(SupersonicTag.NATIVE, "Please call init before calling isInterstitialAdAvailable", 2);
        return false;
    }

    public void initInterstitial(Activity activity, String appKey, String userId) {
        EventsHelper.getInstance().notifyInitInterstitialEvent(getProviderName());
        final HashMap<String, String> supersonicConfig = getInterstitialExtraParams();
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":initInterstitial(appKey:" + appKey + ", userId:" + userId + ", extraParams:" + supersonicConfig + ")", 1);
        final Activity activity2 = activity;
        final String str = appKey;
        final String str2 = userId;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                SupersonicAdsAdapter.this.mSSAPublisher = SSAFactory.getPublisherInstance(activity2);
                SupersonicAdsAdapter.this.mSSAPublisher.initInterstitial(str, str2, supersonicConfig, new OnInterstitialListener() {
                    public void onInterstitialInitSuccess() {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialInitSuccess();
                        }
                    }

                    public void onInterstitialInitFail(String description) {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialInitFail(ErrorBuilder.buildAdapterInitFailedError(description));
                        }
                    }

                    public void onInterstitialAvailability(boolean available) {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialAvailability(available);
                        }
                    }

                    public void onInterstitialShowFail(String description) {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialShowFail(ErrorBuilder.buildGenericError(description));
                        }
                    }

                    public void onInterstitialShowSuccess() {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialShowSuccess();
                        }
                    }

                    public void onInterstitialAdClicked() {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialAdClicked();
                        }
                    }

                    public void onInterstitialAdClosed() {
                        if (SupersonicAdsAdapter.this.mInterstitialListener != null) {
                            SupersonicAdsAdapter.this.mInterstitialListener.onInterstitialAdClosed();
                        }
                    }
                });
            }
        });
    }

    public void showInterstitial() {
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":showInterstitial()", 1);
        if (this.mSSAPublisher != null) {
            this.mSSAPublisher.showInterstitial();
        } else {
            log(SupersonicTag.NATIVE, "Please call init before calling showInterstitial", 2);
        }
    }

    public void getOfferwallCredits() {
        if (this.mSSAPublisher != null) {
            String appKey = ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicAppKey();
            String userId = ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicUserId();
            log(SupersonicTag.ADAPTER_API, getProviderName() + ":getOfferwallCredits(appKey:" + appKey + "userId:" + userId + ")", 1);
            this.mSSAPublisher.getOfferWallCredits(appKey, userId, this);
            return;
        }
        log(SupersonicTag.NATIVE, "Please call init before calling getOfferwallCredits", 2);
    }

    public void setOfferwallListener(OfferwallListener owListener) {
        this.mOfferwallListener = owListener;
    }

    public void initOfferwall(final Activity activity, final String appKey, final String userId) {
        try {
            EventsHelper.getInstance().notifyInitOfferwallEvent(getProviderName());
            log(SupersonicTag.ADAPTER_API, getProviderName() + ":initOfferwall(appKey:" + appKey + ", userId:" + userId + ")", 1);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Map<String, String> offerwallExtraParams = SupersonicAdsAdapter.this.getOfferwallExtraParams();
                    SupersonicAdsAdapter.this.mSSAPublisher = SSAFactory.getPublisherInstance(activity);
                    SupersonicAdsAdapter.this.mSSAPublisher.initOfferWall(appKey, userId, offerwallExtraParams, SupersonicAdsAdapter.this);
                }
            });
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.ADAPTER_API, getProviderName() + ":initOfferwall(userId:" + userId + ")", e);
            this.mOfferwallListener.onOfferwallInitFail(ErrorBuilder.buildAdapterInitFailedError("Offerwall init failed"));
        }
    }

    public void showOfferwall() {
        Map<String, String> offerwallExtraParams = getOfferwallExtraParams();
        log(SupersonicTag.ADAPTER_API, getProviderName() + ":showOfferwall(" + "extraParams:" + offerwallExtraParams + ")", 1);
        if (this.mSSAPublisher != null) {
            this.mSSAPublisher.showOfferWall(offerwallExtraParams);
        } else {
            log(SupersonicTag.NATIVE, "Please call init before calling showOfferwall", 2);
        }
    }

    public boolean isOfferwallAvailable() {
        return true;
    }

    public void onOWShowSuccess() {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onOfferwallOpened();
        }
    }

    public void onOWShowFail(String desc) {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onOfferwallShowFail(ErrorBuilder.buildGenericError(desc));
        }
    }

    public void onOWGeneric(String arg0, String arg1) {
    }

    public boolean onOWAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
        return this.mOfferwallListener != null && this.mOfferwallListener.onOfferwallAdCredited(credits, totalCredits, totalCreditsFlag);
    }

    public void onOWAdClosed() {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onOfferwallClosed();
        }
    }

    public void onGetOWCreditsFailed(String desc) {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onGetOfferwallCreditsFail(ErrorBuilder.buildGenericError(desc));
        }
    }

    public void onOfferwallInitSuccess() {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onOfferwallInitSuccess();
        }
    }

    public void onOfferwallInitFail(String description) {
        if (this.mOfferwallListener != null) {
            this.mOfferwallListener.onOfferwallInitFail(ErrorBuilder.buildGenericError(description));
        }
    }
}
