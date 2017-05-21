package com.nativex.monetization.manager;

import android.content.Context;
import com.nativex.monetization.listeners.CurrencyListenerBase;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.theme.OriginalTheme;
import com.nativex.monetization.theme.ThemeManager;

public class MonetizationSharedDataManager {
    private volatile String advertiserId;
    private volatile String appId;
    private volatile String applicationFilesDirectoryPath = null;
    private volatile String applicationName;
    private volatile Context contextInstance;
    private volatile CurrencyListenerBase currencyListener = null;
    private volatile boolean currencySupported = true;
    private volatile boolean limitAdTracking = true;
    private volatile String publisherUserId;
    private volatile RewardListener rewardListener = null;
    private volatile String webViewUserAgent = null;

    private MonetizationSharedDataManager() {
    }

    private static synchronized MonetizationSharedDataManager getInstance() {
        MonetizationSharedDataManager monetizationSharedDataManager;
        synchronized (MonetizationSharedDataManager.class) {
            monetizationSharedDataManager = ManagementService.getInstance().getMonetizationSharedDataManager();
            if (monetizationSharedDataManager == null) {
                monetizationSharedDataManager = new MonetizationSharedDataManager();
                ManagementService.getInstance().setMonetizationSharedDataManager(monetizationSharedDataManager);
            }
        }
        return monetizationSharedDataManager;
    }

    public static Context getContext() {
        return getInstance().contextInstance;
    }

    public static void setContext(Context context) {
        getInstance().contextInstance = context;
    }

    public static String getAppId() {
        return getInstance().appId;
    }

    public static void setAppId(String appId) {
        getInstance().appId = appId;
    }

    public static String getPublisherUserId() {
        return getInstance().publisherUserId;
    }

    public static void setPublisherUserId(String publisherUserId) {
        getInstance().publisherUserId = publisherUserId;
    }

    public static String getApplicationName() {
        return getInstance().applicationName;
    }

    public static void setApplicationName(String applicationName) {
        getInstance().applicationName = applicationName;
    }

    public static CurrencyListenerBase getCurrencyListener() {
        return getInstance().currencyListener;
    }

    public static RewardListener getRewardListener() {
        return getInstance().rewardListener;
    }

    public static void setCurrencyListener(CurrencyListenerBase currencyListener) {
        getInstance().currencyListener = currencyListener;
    }

    public static void setRewardListener(RewardListener rewardListener) {
        getInstance().rewardListener = rewardListener;
    }

    public static boolean isUsingRewardListener() {
        return getInstance().rewardListener != null;
    }

    public static void checkTheme() {
        if (ThemeManager.getTheme() == null) {
            ThemeManager.setTheme(new OriginalTheme());
        }
    }

    public static void setCurrencySupport(boolean isSupported) {
        getInstance().currencySupported = isSupported;
    }

    public static boolean isCurrencySupported() {
        return getInstance().currencySupported;
    }

    public static void setLimitAdTracking(boolean isLimitTracking) {
        getInstance().limitAdTracking = isLimitTracking;
    }

    public static boolean isLimitAdTracking() {
        return getInstance().limitAdTracking;
    }

    public static void setAdvertiserId(String advertiserId) {
        getInstance().advertiserId = advertiserId;
    }

    public static String getAdvertiserId() {
        return getInstance().advertiserId;
    }

    public static String getApplicationFilesDirectoryPath() {
        return getInstance().applicationFilesDirectoryPath;
    }

    public static void setApplicationFilesDirectoryPath(String applicationFilesDirectoryPath) {
        getInstance().applicationFilesDirectoryPath = applicationFilesDirectoryPath;
    }

    public static String getWebViewUserAgent() {
        return getInstance().webViewUserAgent;
    }

    public static void setWebViewUserAgent(String webViewUserAgent) {
        getInstance().webViewUserAgent = webViewUserAgent;
    }
}
