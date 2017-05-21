package com.supersonic.mediationsdk;

import com.supersonic.mediationsdk.config.AbstractAdapterConfig;
import com.supersonic.mediationsdk.config.ConfigValidationResult;
import com.supersonic.mediationsdk.logger.LogListener;
import com.supersonic.mediationsdk.logger.LoggingApi;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.PlacementsHolder;
import com.supersonic.mediationsdk.sdk.BaseApi;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.sdk.RewardedVideoManagerListener;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractAdapter implements BaseApi, LoggingApi {
    private final String ADAPTER_CORE_SDK_VERSION_KEY = "SdkVersion";
    private final String ADAPTER_VERSION_KEY = "AdapterVersion";
    private Object mInitFlagsLocker;
    private boolean mIsInterstitialSupported = false;
    private boolean mIsOfferwallSupported = false;
    private boolean mIsRewardedVideoSupported = false;
    private SupersonicLoggerManager mLoggerManager = SupersonicLoggerManager.getLogger();
    private int mNumberOfVideosPlayed;
    protected PlacementsHolder mPlacementHolder;
    private String mProviderName;
    private String mProviderUrl;
    private Boolean mRVInitFlag;
    private int mRVPriority = -1;
    private int mRewardedVideoTimeout;
    private TimerTask timerTask;

    public abstract String getCoreSDKVersion();

    public abstract int getMaxVideosPerIteration();

    public abstract String getVersion();

    public AbstractAdapter(String providerName, String providerUrl) {
        if (providerName == null) {
            providerName = "";
        }
        if (providerUrl == null) {
            providerUrl = "";
        }
        this.mProviderName = providerName;
        this.mProviderUrl = providerUrl;
        this.mRVInitFlag = null;
        this.mInitFlagsLocker = new Object();
        this.mNumberOfVideosPlayed = 0;
        setRewardedVideoSupport(true);
        GeneralProperties.getProperties().putKey(getProviderName() + "AdapterVersion", getVersion());
        GeneralProperties.getProperties().putKey(getProviderName() + "SdkVersion", getCoreSDKVersion());
        setCustomParams();
    }

    public int getNumberOfVideosPlayed() {
        return this.mNumberOfVideosPlayed;
    }

    public void increaseNumberOfVideosPlayed() {
        this.mNumberOfVideosPlayed++;
    }

    public void resetNumberOfVideosPlayed() {
        this.mNumberOfVideosPlayed = 0;
    }

    public void setRewardedVideoTimeout(int timeout) {
        this.mRewardedVideoTimeout = timeout;
    }

    public void setRewardedVideoPriority(int priority) {
        this.mRVPriority = priority;
    }

    public int getRewardedVideoPriority() {
        return this.mRVPriority;
    }

    public void setPlacementsHolder(PlacementsHolder holder) {
        this.mPlacementHolder = holder;
    }

    private void setCustomParams() {
        try {
            Integer age = ((SupersonicObject) SupersonicFactory.getInstance()).getAge();
            if (age != null) {
                setAge(age.intValue());
            }
            String gender = ((SupersonicObject) SupersonicFactory.getInstance()).getGender();
            if (gender != null) {
                setGender(gender);
            }
        } catch (Exception e) {
            this.mLoggerManager.log(SupersonicTag.INTERNAL, getProviderName() + ":setCustomParams():" + e.getStackTrace(), 3);
        }
    }

    public String getProviderName() {
        return this.mProviderName;
    }

    public void setLogListener(LogListener logListener) {
    }

    protected void log(SupersonicTag tag, String message, int logLevel) {
        this.mLoggerManager.onLog(tag, message, logLevel);
    }

    protected String getUrl() {
        return this.mProviderUrl;
    }

    protected synchronized boolean isRewardedVideoSupported() {
        return this.mIsRewardedVideoSupported;
    }

    protected synchronized boolean isInterstitialSupported() {
        return this.mIsInterstitialSupported;
    }

    protected synchronized boolean isOfferwallSupported() {
        return this.mIsOfferwallSupported;
    }

    protected synchronized void setRewardedVideoSupport(boolean isSupported) {
        this.mIsRewardedVideoSupported = isSupported;
    }

    protected synchronized void setInterstitialSupport(boolean isSupported) {
        this.mIsInterstitialSupported = isSupported;
    }

    protected synchronized void setOfferwallSupport(boolean isSupported) {
        this.mIsOfferwallSupported = isSupported;
    }

    protected void setRVInitStatus(boolean initValue) {
        synchronized (this.mInitFlagsLocker) {
            this.mRVInitFlag = Boolean.valueOf(initValue);
        }
    }

    protected boolean isRVInitFinished() {
        boolean z;
        synchronized (this.mInitFlagsLocker) {
            z = this.mRVInitFlag != null;
        }
        return z;
    }

    protected boolean isRVInitSucceed() {
        boolean z;
        synchronized (this.mInitFlagsLocker) {
            z = this.mRVInitFlag != null && this.mRVInitFlag.booleanValue();
        }
        return z;
    }

    protected ConfigValidationResult validateConfigBeforeInitAndCallInitFailForInvalid(AbstractAdapterConfig config, RewardedVideoManagerListener manager) {
        ConfigValidationResult validationResult = config.isConfigValid();
        if (!validationResult.isValid()) {
            SupersonicError sse = validationResult.getSupersonicError();
            log(SupersonicTag.ADAPTER_API, getProviderName() + sse.getErrorMessage(), 2);
            if (manager != null) {
                manager.onRewardedVideoInitFail(sse, this);
            }
        }
        return validationResult;
    }

    public boolean equals(Object other) {
        if (other == null || !(other instanceof AbstractAdapter)) {
            return false;
        }
        return this.mProviderName.equals(((AbstractAdapter) other).mProviderName);
    }

    public SupersonicError isVersionSupported(String coreSDKVersion, Map<String, List<String>> supportedVersions) {
        String adapterVersion = getVersion();
        GeneralProperties.getProperties().putKey(getProviderName() + "AdapterVersion", adapterVersion);
        GeneralProperties.getProperties().putKey(getProviderName() + "SdkVersion", coreSDKVersion);
        if (adapterVersion == null) {
            return ErrorBuilder.buildUnsupportedSdkVersion(coreSDKVersion, getProviderName());
        }
        List<String> supportedVersionsForAdapter;
        if (supportedVersions == null || !supportedVersions.containsKey(adapterVersion)) {
            supportedVersionsForAdapter = new ArrayList();
        } else {
            supportedVersionsForAdapter = (List) supportedVersions.get(adapterVersion);
        }
        if (supportedVersionsForAdapter == null || !supportedVersionsForAdapter.contains(coreSDKVersion)) {
            return ErrorBuilder.buildUnsupportedSdkVersion(coreSDKVersion, getProviderName());
        }
        return null;
    }

    protected void startTimer(final RewardedVideoManagerListener listener) {
        this.timerTask = new TimerTask() {
            public void run() {
                listener.onVideoAvailabilityChanged(false, AbstractAdapter.this);
            }
        };
        new Timer().schedule(this.timerTask, (long) (this.mRewardedVideoTimeout * ControllerParameters.SECOND));
    }

    protected void cancelTimer() {
        try {
            if (this.timerTask != null) {
                this.timerTask.cancel();
                this.timerTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
