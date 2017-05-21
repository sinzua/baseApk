package com.supersonic.mediationsdk;

import android.app.Activity;
import android.content.Context;
import com.parse.ParseException;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonic.environment.DeviceStatus;
import com.supersonic.mediationsdk.config.ConfigFile;
import com.supersonic.mediationsdk.config.ConfigValidationResult;
import com.supersonic.mediationsdk.events.EventsHelper;
import com.supersonic.mediationsdk.logger.ConsoleLogger;
import com.supersonic.mediationsdk.logger.LogListener;
import com.supersonic.mediationsdk.logger.PublisherLogger;
import com.supersonic.mediationsdk.logger.ServerLogger;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.Placement;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.ListenersWrapper;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.server.HttpFunctions;
import com.supersonic.mediationsdk.server.Server;
import com.supersonic.mediationsdk.server.ServerURL;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.ServerResponseWrapper;
import com.supersonic.mediationsdk.utils.SupersonicConstants;
import com.supersonic.mediationsdk.utils.SupersonicConstants.Gender;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

public class SupersonicObject implements Supersonic {
    private final String TAG = getClass().getName();
    private ServerResponseWrapper currentServerResponse = null;
    private ArrayList<AbstractAdapter> mAdaptersList;
    private String mAppKey = null;
    private AtomicBoolean mAtomicBaseInit;
    private InterstitialManager mInterstitialManager;
    private ListenersWrapper mListenersWrapper;
    private SupersonicLoggerManager mLoggerManager;
    private OfferwallManager mOfferwallManager;
    private PublisherLogger mPublisherLogger;
    private RewardedVideoManager mRewardedVideoManager;
    private Integer mUserAge = null;
    private String mUserGender = null;
    private String mUserId = null;
    private final Object serverResponseLocker = new Object();

    public SupersonicObject() {
        EventsHelper.getInstance().notifyGetInstanceEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
        initializeManagers();
        this.mAtomicBaseInit = new AtomicBoolean();
        this.mAdaptersList = new ArrayList();
    }

    public void initRewardedVideo(Activity activity, String appKey, String userId) {
        ConfigValidationResult validationResult = validateAppKeyUserId(appKey, userId);
        if (validationResult.isValid()) {
            setSupersonicAppKeyUserId(appKey, userId);
            String logMessage = "initRewardedVideo(appKey:" + getSupersonicAppKey() + ", userId:" + getSupersonicUserId() + ")";
            try {
                baseInit(activity, getSupersonicUserId());
                this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
                EventsHelper.getInstance().notifyInitRewardedVideoEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
                this.mRewardedVideoManager.initRewardedVideo(activity, getSupersonicAppKey(), getSupersonicUserId());
                return;
            } catch (Exception e) {
                this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
                return;
            }
        }
        this.mListenersWrapper.onRewardedVideoInitFail(validationResult.getSupersonicError());
    }

    public void initOfferwall(Activity activity, String appKey, String userId) {
        ConfigValidationResult validationResult = validateAppKeyUserId(appKey, userId);
        if (validationResult.isValid()) {
            setSupersonicAppKeyUserId(appKey, userId);
            String logMessage = "initOfferwall(appKey:" + getSupersonicAppKey() + ", userId:" + getSupersonicUserId() + ")";
            try {
                baseInit(activity, getSupersonicUserId());
                this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
                EventsHelper.getInstance().notifyInitOfferwallEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
                this.mOfferwallManager.initOfferwall(activity, getSupersonicAppKey(), getSupersonicUserId());
                return;
            } catch (Exception e) {
                this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
                return;
            }
        }
        this.mListenersWrapper.onOfferwallInitFail(validationResult.getSupersonicError());
    }

    public void initInterstitial(Activity activity, String appKey, String userId) {
        ConfigValidationResult validationResult = validateAppKeyUserId(appKey, userId);
        if (validationResult.isValid()) {
            setSupersonicAppKeyUserId(appKey, userId);
            String logMessage = "initInterstitial(appKey:" + getSupersonicAppKey() + ", userId:" + getSupersonicUserId() + ")";
            try {
                baseInit(activity, getSupersonicUserId());
                this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
                EventsHelper.getInstance().notifyInitInterstitialEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
                this.mInterstitialManager.initInterstitial(activity, getSupersonicAppKey(), getSupersonicUserId());
                return;
            } catch (Exception e) {
                this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
                return;
            }
        }
        this.mListenersWrapper.onInterstitialInitFail(validationResult.getSupersonicError());
    }

    private void baseInit(Activity activity, String userId) {
        if (this.mAtomicBaseInit != null && this.mAtomicBaseInit.compareAndSet(false, true)) {
            prepareForInit(activity, userId);
        }
    }

    private void prepareForInit(Activity activity, String userId) {
        EventsHelper.start(activity.getApplicationContext());
        Server.notifyUniqueUser(getSupersonicAppKey(), userId);
    }

    public void addToAdaptersList(AbstractAdapter adapter) {
        if (this.mAdaptersList != null && adapter != null && !this.mAdaptersList.contains(adapter)) {
            this.mAdaptersList.add(adapter);
        }
    }

    private void initializeManagers() {
        this.mLoggerManager = SupersonicLoggerManager.getLogger(0);
        this.mPublisherLogger = new PublisherLogger(null, 1);
        this.mLoggerManager.addLogger(this.mPublisherLogger);
        this.mListenersWrapper = new ListenersWrapper();
        this.mRewardedVideoManager = new RewardedVideoManager();
        this.mRewardedVideoManager.setRewardedVideoListener(this.mListenersWrapper);
        this.mInterstitialManager = new InterstitialManager();
        this.mInterstitialManager.setInterstitialListener(this.mListenersWrapper);
        this.mOfferwallManager = new OfferwallManager();
        this.mOfferwallManager.setOfferwallListener(this.mListenersWrapper);
    }

    public void release(Activity activity) {
    }

    public void onResume(Activity activity) {
        String logMessage = "onResume()";
        try {
            this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
            EventsHelper.getInstance().notifyResumeEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
            if (this.mRewardedVideoManager != null) {
                this.mRewardedVideoManager.onResume(activity);
            }
            Iterator i$ = this.mAdaptersList.iterator();
            while (i$.hasNext()) {
                ((AbstractAdapter) i$.next()).onResume(activity);
            }
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
        }
    }

    public void onPause(Activity activity) {
        String logMessage = "onPause()";
        try {
            this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
            EventsHelper.getInstance().notifyPauseEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
            Iterator i$ = this.mAdaptersList.iterator();
            while (i$.hasNext()) {
                ((AbstractAdapter) i$.next()).onPause(activity);
            }
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
        }
    }

    public synchronized void setAge(int age) {
        try {
            this.mLoggerManager.log(SupersonicTag.API, this.TAG + ":setAge(age:" + age + ")", 1);
            ConfigValidationResult result = new ConfigValidationResult();
            validateAge(age, result);
            if (result.isValid()) {
                this.mUserAge = Integer.valueOf(age);
                Iterator i$ = this.mAdaptersList.iterator();
                while (i$.hasNext()) {
                    ((AbstractAdapter) i$.next()).setAge(age);
                }
            } else {
                SupersonicLoggerManager.getLogger().log(SupersonicTag.API, result.getSupersonicError().toString(), 2);
            }
        } catch (Exception e) {
            this.mLoggerManager.logException(SupersonicTag.API, this.TAG + ":setAge(age:" + age + ")", e);
        }
        return;
    }

    public synchronized void setGender(String gender) {
        try {
            this.mLoggerManager.log(SupersonicTag.API, this.TAG + ":setGender(gender:" + gender + ")", 1);
            ConfigValidationResult result = new ConfigValidationResult();
            validateGender(gender, result);
            if (result.isValid()) {
                this.mUserGender = gender;
                Iterator i$ = this.mAdaptersList.iterator();
                while (i$.hasNext()) {
                    ((AbstractAdapter) i$.next()).setGender(gender);
                }
            } else {
                SupersonicLoggerManager.getLogger().log(SupersonicTag.API, result.getSupersonicError().toString(), 2);
            }
        } catch (Exception e) {
            this.mLoggerManager.logException(SupersonicTag.API, this.TAG + ":setGender(gender:" + gender + ")", e);
        }
        return;
    }

    private void validateGender(String gender, ConfigValidationResult result) {
        if (gender != null) {
            try {
                gender = gender.toLowerCase().trim();
                if (!Gender.MALE.equals(gender) && !Gender.FEMALE.equals(gender) && !"unknown".equals(gender)) {
                    result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(SupersonicConfig.GENDER, "SupersonicAds", "gender value should be one of male/female/unknown."));
                }
            } catch (Exception e) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(SupersonicConfig.GENDER, "SupersonicAds", "gender value should be one of male/female/unknown."));
            }
        }
    }

    private void validateAge(int age, ConfigValidationResult result) {
        if (age < 5 || age > ParseException.CACHE_MISS) {
            try {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(SupersonicConfig.AGE, "SupersonicAds", "age value should be between 5-120"));
            } catch (NumberFormatException e) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(SupersonicConfig.AGE, "SupersonicAds", "age value should be between 5-120"));
            }
        }
    }

    public synchronized Integer getAge() {
        return this.mUserAge;
    }

    public synchronized String getGender() {
        return this.mUserGender;
    }

    public void showRewardedVideo() {
        String logMessage = "showRewardedVideo()";
        try {
            this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
            Placement defaultPlacement = this.currentServerResponse.getPlacementHolder().getDefaultRewardedVideoPlacement();
            if (defaultPlacement != null) {
                showRewardedVideo(defaultPlacement.getPlacementName());
            }
        } catch (Exception e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
            this.mListenersWrapper.onRewardedVideoShowFail(ErrorBuilder.buildShowVideoFailedError("ShowRV can't be called before the RV ad unit initialization completed successfully"));
        }
    }

    public void showRewardedVideo(String placementName) {
        String logMessage = "showRewardedVideo(" + placementName + ")";
        try {
            Placement placement = this.currentServerResponse.getPlacementHolder().getRewardedVideoPlacement(placementName);
            if (placement == null) {
                this.mLoggerManager.log(SupersonicTag.API, "Placement is not valid, please make sure you are using the right placements, using the default placement.", 3);
                placement = this.currentServerResponse.getPlacementHolder().getDefaultRewardedVideoPlacement();
                if (placement == null) {
                    this.mLoggerManager.log(SupersonicTag.API, "Default placement was not found, please make sure you are using the right placements.", 3);
                    return;
                }
            }
            this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
            EventsHelper.getInstance().notifyShowRewardedVideoEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, placementName);
            this.mRewardedVideoManager.showRewardedVideo(placement.getPlacementName());
        } catch (Exception e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
            this.mListenersWrapper.onRewardedVideoShowFail(ErrorBuilder.buildShowVideoFailedError("ShowRV can't be called before the RV ad unit initialization completed successfully"));
        }
    }

    public boolean isRewardedVideoAvailable() {
        boolean isAvailable = false;
        try {
            isAvailable = this.mRewardedVideoManager.isRewardedVideoAvailable();
            EventsHelper.getInstance().notifyPublisherCheckAvailabilityEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, isAvailable);
            this.mLoggerManager.log(SupersonicTag.API, "isRewardedVideoAvailable():" + isAvailable, 1);
            return isAvailable;
        } catch (Throwable e) {
            this.mLoggerManager.log(SupersonicTag.API, "isRewardedVideoAvailable():" + isAvailable, 1);
            this.mLoggerManager.logException(SupersonicTag.API, "isRewardedVideoAvailable()", e);
            return false;
        }
    }

    public void setRewardedVideoListener(RewardedVideoListener rewardedVideoListener) {
        if (rewardedVideoListener == null) {
            this.mLoggerManager.log(SupersonicTag.API, "setRewardedVideoListener(RVListener:null)", 1);
        } else {
            this.mLoggerManager.log(SupersonicTag.API, "setRewardedVideoListener(RVListener)", 1);
        }
        this.mListenersWrapper.setRewardedVideoListener(rewardedVideoListener);
    }

    public boolean isInterstitialAdAvailable() {
        String logMessage = "isInterstitialAdAvailable()";
        this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
        try {
            return this.mInterstitialManager.isInterstitialAdAvailable();
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
            return false;
        }
    }

    public void showInterstitial() {
        String logMessage = "showInterstitial()";
        this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
        try {
            this.mInterstitialManager.showInterstitial();
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
        }
    }

    public void setInterstitialListener(InterstitialListener isListener) {
        if (isListener == null) {
            this.mLoggerManager.log(SupersonicTag.API, "setInterstitialListener(ISListener:null)", 1);
        } else {
            this.mLoggerManager.log(SupersonicTag.API, "setInterstitialListener(ISListener)", 1);
        }
        this.mListenersWrapper.setInterstitialListener(isListener);
    }

    public void showOfferwall() {
        String logMessage = "showOfferwall()";
        this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
        try {
            this.mOfferwallManager.showOfferwall();
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
        }
    }

    public boolean isOfferwallAvailable() {
        try {
            if (this.mOfferwallManager != null) {
                return this.mOfferwallManager.isOfferwallAvailable();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void getOfferwallCredits() {
        String logMessage = "getOfferwallCredits()";
        this.mLoggerManager.log(SupersonicTag.API, logMessage, 1);
        try {
            this.mOfferwallManager.getOfferwallCredits();
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.API, logMessage, e);
        }
    }

    public void setOfferwallListener(OfferwallListener offerwallListener) {
        if (offerwallListener == null) {
            this.mLoggerManager.log(SupersonicTag.API, "setOfferwallListener(OWListener:null)", 1);
        } else {
            this.mLoggerManager.log(SupersonicTag.API, "setOfferwallListener(OWListener)", 1);
        }
        this.mListenersWrapper.setOfferwallListener(offerwallListener);
    }

    public void setLogListener(LogListener logListener) {
        if (logListener == null) {
            this.mLoggerManager.log(SupersonicTag.API, "setLogListener(LogListener:null)", 1);
            return;
        }
        this.mPublisherLogger.setLogListener(logListener);
        this.mLoggerManager.log(SupersonicTag.API, "setLogListener(LogListener:" + logListener.getClass().getSimpleName() + ")", 1);
    }

    public void addAll(ArrayList<AbstractAdapter> adapters) {
        if (adapters != null) {
            for (int i = 0; i < adapters.size(); i++) {
                addToAdaptersList((AbstractAdapter) adapters.get(i));
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.supersonic.mediationsdk.utils.ServerResponseWrapper getServerResponse(android.content.Context r5, java.lang.String r6) {
        /*
        r4 = this;
        r0 = 0;
        r2 = r4.serverResponseLocker;
        monitor-enter(r2);
        r1 = r4.currentServerResponse;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0011;
    L_0x0008:
        r1 = new com.supersonic.mediationsdk.utils.ServerResponseWrapper;	 Catch:{ all -> 0x0034 }
        r3 = r4.currentServerResponse;	 Catch:{ all -> 0x0034 }
        r1.<init>(r3);	 Catch:{ all -> 0x0034 }
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
    L_0x0010:
        return r1;
    L_0x0011:
        r0 = r4.connectAndGetServerResponse(r5, r6);	 Catch:{ all -> 0x0034 }
        if (r0 == 0) goto L_0x001d;
    L_0x0017:
        r1 = r0.isValidResponse();	 Catch:{ all -> 0x0034 }
        if (r1 != 0) goto L_0x0021;
    L_0x001d:
        r0 = r4.getCachedResponse(r5, r6);	 Catch:{ all -> 0x0034 }
    L_0x0021:
        if (r0 == 0) goto L_0x0031;
    L_0x0023:
        r4.currentServerResponse = r0;	 Catch:{ all -> 0x0034 }
        r1 = r0.toString();	 Catch:{ all -> 0x0034 }
        com.supersonic.mediationsdk.utils.SupersonicUtils.saveLastResponse(r5, r1);	 Catch:{ all -> 0x0034 }
        r1 = r4.currentServerResponse;	 Catch:{ all -> 0x0034 }
        r4.initializeSettingsFromServerResponse(r1, r5);	 Catch:{ all -> 0x0034 }
    L_0x0031:
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
        r1 = r0;
        goto L_0x0010;
    L_0x0034:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.supersonic.mediationsdk.SupersonicObject.getServerResponse(android.content.Context, java.lang.String):com.supersonic.mediationsdk.utils.ServerResponseWrapper");
    }

    private ServerResponseWrapper getCachedResponse(Context context, String userId) {
        JSONObject cachedJsonObject;
        try {
            cachedJsonObject = new JSONObject(SupersonicUtils.getLastResponse(context));
        } catch (JSONException e) {
            cachedJsonObject = new JSONObject();
        }
        String cachedAppKey = cachedJsonObject.optString(ServerResponseWrapper.APP_KEY_FIELD);
        String cachedUserId = cachedJsonObject.optString("userId");
        String cachedSettings = cachedJsonObject.optString("settings");
        if (SupersonicUtils.isEmpty(cachedAppKey) || SupersonicUtils.isEmpty(cachedUserId) || SupersonicUtils.isEmpty(cachedSettings) || getSupersonicAppKey() == null || !cachedAppKey.equals(getSupersonicAppKey()) || !cachedUserId.equals(userId)) {
            return null;
        }
        ServerResponseWrapper response = new ServerResponseWrapper(cachedAppKey, cachedUserId, cachedSettings);
        SupersonicError sse = ErrorBuilder.buildUsingCachedConfigurationError(cachedAppKey, cachedUserId);
        this.mLoggerManager.log(SupersonicTag.INTERNAL, sse.toString(), 1);
        this.mLoggerManager.log(SupersonicTag.INTERNAL, sse.toString() + ": " + response.toString(), 0);
        return response;
    }

    private ServerResponseWrapper connectAndGetServerResponse(Context context, String userId) {
        ServerResponseWrapper response = null;
        try {
            String serverResponseString = HttpFunctions.getStringFromURL(ServerURL.getCPVProvidersURL(getSupersonicAppKey(), userId, getAdvertiserId(context)));
            if (serverResponseString == null) {
                return null;
            }
            ServerResponseWrapper response2 = new ServerResponseWrapper(getSupersonicAppKey(), userId, serverResponseString);
            try {
                if (response2.isValidResponse()) {
                    response = response2;
                    return response;
                }
                response = response2;
                return null;
            } catch (Exception e) {
                response = response2;
            }
        } catch (Exception e2) {
        }
    }

    private void initializeSettingsFromServerResponse(ServerResponseWrapper response, Context context) {
        initializeLoggerManager(response);
        initializeEventsSettings(response, context);
        mergeLocalServerConfigurations(response);
    }

    private void mergeLocalServerConfigurations(ServerResponseWrapper response) {
        Map<String, JSONObject> serverConfigMap = response.getConfigurationMap();
        for (String providerName : serverConfigMap.keySet()) {
            mergeSingleAdapterConfigurations(ConfigFile.getConfigFile().getProviderJsonSettings(providerName), (JSONObject) serverConfigMap.get(providerName));
        }
    }

    private void mergeSingleAdapterConfigurations(JSONObject localConfig, JSONObject remoteConfig) {
        try {
            Iterator<String> keys = remoteConfig.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!localConfig.has(key)) {
                    localConfig.put(key, remoteConfig.get(key));
                }
            }
        } catch (Exception e) {
        }
    }

    private void initializeEventsSettings(ServerResponseWrapper response, Context context) {
        boolean isEventsEnabled = response.isEventsEnabled();
        if (isEventsEnabled) {
            EventsHelper.getInstance().setEventsUrl(response.getEventsUrl(), context);
            EventsHelper.getInstance().setMaxNumberOfEvents(response.getMaxNumberOfEvents());
            EventsHelper.getInstance().setBackupThreshold(response.getEventsBackupThreshold());
            return;
        }
        EventsHelper.getInstance().setIsEventsEnabled(isEventsEnabled);
    }

    private void initializeLoggerManager(ServerResponseWrapper response) {
        this.mPublisherLogger.setDebugLevel(response.getPublisherLoggerLevel());
        this.mLoggerManager.setLoggerDebugLevel(ConsoleLogger.NAME, response.getConsoleLoggerLevel());
        this.mLoggerManager.setLoggerDebugLevel(ServerLogger.NAME, response.getServerLoggerLevel());
    }

    public void removeRewardedVideoListener() {
        this.mLoggerManager.log(SupersonicTag.API, "removeRewardedVideoListener()", 1);
        this.mListenersWrapper.setRewardedVideoListener(null);
    }

    public void removeInterstitialListener() {
        this.mLoggerManager.log(SupersonicTag.API, "removeInterstitialListener()", 1);
        this.mListenersWrapper.setInterstitialListener(null);
    }

    public void removeOfferwallListener() {
        this.mLoggerManager.log(SupersonicTag.API, "removeOfferwallListener()", 1);
        this.mListenersWrapper.setOfferwallListener(null);
    }

    private synchronized void setSupersonicAppKeyUserId(String appKey, String userId) {
        if (this.mAppKey == null) {
            this.mAppKey = appKey;
        }
        if (this.mUserId == null) {
            this.mUserId = userId;
        }
    }

    public synchronized String getSupersonicAppKey() {
        return this.mAppKey;
    }

    public synchronized String getSupersonicUserId() {
        return this.mUserId;
    }

    private ConfigValidationResult validateAppKeyUserId(String appKey, String userId) {
        ConfigValidationResult result = validateAppKey(appKey);
        if (result.isValid()) {
            return validateUserId(userId);
        }
        return result;
    }

    private ConfigValidationResult validateUserId(String userId) {
        ConfigValidationResult result = new ConfigValidationResult();
        if (userId == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("userId", "Supersonic", "userId value is missing"));
        } else if (userId.length() < 1 || userId.length() > 64) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("userId", "Supersonic", "userId length should be between 1-64 characters"));
        }
        return result;
    }

    private ConfigValidationResult validateAppKey(String appKey) {
        ConfigValidationResult result = new ConfigValidationResult();
        if (appKey == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", "Supersonic", "applicationKey value is missing"));
        } else if (appKey.length() < 5 || appKey.length() > 10) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", "Supersonic", "applicationKey length should be between 5-10 characters"));
        } else if (!appKey.matches("^[a-zA-Z0-9]*$")) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", "Supersonic", "applicationKey should contains only english characters and numbers"));
        }
        return result;
    }

    public Placement getPlacementInfo(String placementName) {
        Placement result = null;
        try {
            result = this.currentServerResponse.getPlacementHolder().getRewardedVideoPlacement(placementName);
            this.mLoggerManager.log(SupersonicTag.API, "getPlacementInfo(placement: " + placementName + "):" + result, 1);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public String getAdvertiserId(Context c) {
        try {
            String[] deviceInfo = DeviceStatus.getAdvertisingIdInfo(c);
            if (deviceInfo.length <= 0 || deviceInfo[0] == null) {
                return "";
            }
            return deviceInfo[0];
        } catch (Exception e) {
            return "";
        }
    }

    public void shouldTrackNetworkState(boolean track) {
        if (this.mRewardedVideoManager != null) {
            this.mRewardedVideoManager.shouldTrackNetworkState(track);
        }
    }
}
