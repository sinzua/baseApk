package com.nativex.monetization;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.SystemClock;
import com.nativex.common.DeviceFriendlyNameHelper;
import com.nativex.common.Log;
import com.nativex.common.NetworkConnectionManager;
import com.nativex.common.Version;
import com.nativex.common.billingtracking.BillingCallback;
import com.nativex.common.billingtracking.BillingInputs;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.BannerPosition;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.CurrencyListenerBase;
import com.nativex.monetization.listeners.OnAdEvent;
import com.nativex.monetization.listeners.OnAdEventBase;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.mraid.AdInfo;
import com.nativex.monetization.mraid.MRAIDManager;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;
import com.nativex.monetization.theme.Theme;
import com.nativex.monetization.theme.ThemeManager;
import java.util.ArrayList;
import java.util.Iterator;

public class MonetizationManager {
    private static final int STATELESS_SESSION_MAX_ATTEMPT_WAIT_SECONDS = 15;
    private static final int STATELESS_SESSION_MAX_DURATION_SECONDS = 3600;
    private static MonetizationManager instance;
    private static Boolean sIsStatelessMode;
    private static long sLastStatelessSessionAttemptTimeSeconds = 0;
    private static long sLastStatelessSessionCreateTimeSeconds = 0;
    private static final Object sStatelessAdSessionLock = new Object();
    private static ArrayList<PlacementFetchSet> sStatelessPlacementsAwaitingSession = new ArrayList();
    private MonetizationSDK monetizationSDK;

    private static class PlacementFetchSet {
        Activity activity;
        OnAdEventBase adEventListener;
        String placementName;

        PlacementFetchSet(String p, Activity a, OnAdEventBase l) {
            this.placementName = p;
            this.activity = a;
            this.adEventListener = l;
        }
    }

    private MonetizationManager() {
        instance = this;
    }

    private static void createInstance() {
        if (instance == null) {
            instance = new MonetizationManager();
            instance.monetizationSDK = new MonetizationSDK();
        }
    }

    private static void checkInstance() {
        if (instance == null) {
            throw new NullPointerException("MonetizationManager not initialized. Please call MonetizationManager.createSession() first.");
        }
    }

    private static boolean isAndroidVersionSupported() {
        boolean isSupported = true;
        if (VERSION.SDK_INT < 11) {
            isSupported = false;
        }
        DeviceFriendlyNameHelper devices = new DeviceFriendlyNameHelper();
        String deviceName = devices.getDeviceName();
        Log.d("MonetizationManager.isAndroidVersionSupported(): Device name: " + deviceName);
        if (!devices.getDeviceName().equals("HTC One") || VERSION.SDK_INT != 18) {
            return isSupported;
        }
        Log.i("MonetizationManager.isAndroidVersionSupported(): Device " + deviceName + " OS VERSION " + 18 + " isn't supported by the SDK.");
        Log.i("No ads will be shown.");
        return false;
    }

    @Deprecated
    public static void initialize(Activity context, ApplicationInputs inputs) {
        if (isAndroidVersionSupported()) {
            createInstance();
            instance.monetizationSDK.initialize(context, inputs);
        }
    }

    @Deprecated
    public static void initialize(Activity context, String applicationName, int appId, String publisherUserId, String packageName) {
        if (isAndroidVersionSupported()) {
            createInstance();
            ApplicationInputs inputs = new ApplicationInputs();
            inputs.setApplicationName(applicationName);
            inputs.setAppId(Integer.toString(appId));
            inputs.setPublisherUserId(publisherUserId);
            inputs.setPackageName(packageName);
            instance.monetizationSDK.initialize(context, inputs);
        }
    }

    @Deprecated
    public static void createSession() {
        createSession(null);
    }

    @Deprecated
    public static void createSession(SessionListener listener) {
        if (isAndroidVersionSupported()) {
            checkInstance();
            instance.monetizationSDK.createSession(listener);
        } else if (listener != null) {
            listener.createSessionCompleted(false, false, null);
        }
    }

    public static void createSession(Context context, String appId, SessionListener listener) {
        if (isAndroidVersionSupported()) {
            createInstance();
            ApplicationInputs inputs = new ApplicationInputs();
            inputs.setAppId(appId);
            instance.monetizationSDK.initialize(context, inputs);
            instance.monetizationSDK.createSession(listener);
        } else if (listener != null) {
            listener.createSessionCompleted(false, false, null);
        }
    }

    public static void createSession(Context context, String appId, String publisherUserId, SessionListener listener) {
        if (isAndroidVersionSupported()) {
            createInstance();
            ApplicationInputs inputs = new ApplicationInputs();
            inputs.setAppId(appId);
            inputs.setPublisherUserId(publisherUserId);
            instance.monetizationSDK.initialize(context, inputs);
            instance.monetizationSDK.createSession(listener);
        } else if (listener != null) {
            listener.createSessionCompleted(false, false, null);
        }
    }

    public static void createSession(Context context, ApplicationInputs inputs, SessionListener listener) {
        if (isAndroidVersionSupported()) {
            createInstance();
            if (inputs == null) {
                throw new NullPointerException("ApplicationInputs cannot be null.");
            }
            instance.monetizationSDK.initialize(context, inputs);
            instance.monetizationSDK.createSession(listener);
        } else if (listener != null) {
            listener.createSessionCompleted(false, false, null);
        }
    }

    public static void actionTaken(int actionId) {
        if (isAndroidVersionSupported()) {
            checkInstance();
            ServerRequestManager.getInstance().actionTaken(actionId);
        }
    }

    @Deprecated
    public static void disableLegacyDeviceIdentifiers(boolean disable) {
    }

    @Deprecated
    public static void setCurrencyListener(CurrencyListenerBase listener) {
        if (isAndroidVersionSupported()) {
            MonetizationSharedDataManager.setCurrencyListener(listener);
        }
    }

    public static void setRewardListener(RewardListener listener) {
        if (isAndroidVersionSupported()) {
            MonetizationSharedDataManager.setRewardListener(listener);
        }
    }

    @Deprecated
    public static void redeemCurrency() {
        if (isAndroidVersionSupported()) {
            checkInstance();
            instance.monetizationSDK.redeemCurrency();
        }
    }

    public static void redeemRewards() {
        if (isAndroidVersionSupported()) {
            checkInstance();
            instance.monetizationSDK.redeemRewards();
        }
    }

    public static void enableLogging(boolean isEnabled) {
        Log.enableLogging(isEnabled);
    }

    public static String getSessionId() {
        if (!isAndroidVersionSupported()) {
            return null;
        }
        checkInstance();
        return instance.monetizationSDK.getSessionId();
    }

    public static String getSDKVersion() {
        return Version.MONETIZATION;
    }

    public static void trackInAppPurchase(BillingInputs inputs) {
        if (isAndroidVersionSupported()) {
            BillingCallback.trackPurchase(inputs.getStoreProductId(), inputs.getStoreTransactionId(), inputs.getStoreTransactionTimeUTC(), inputs.getCostPerItem(), inputs.getCurrencyLocale(), inputs.getQuantity(), inputs.getProductTitle());
        }
    }

    public static void trackInAppPurchase(String storeProductId, String storeTransactionId, String storeTransactionTimeUTC, float costPerItem, String currencyLocale, int quantity, String productTitle) {
        if (isAndroidVersionSupported()) {
            BillingCallback.trackPurchase(storeProductId, storeTransactionId, storeTransactionTimeUTC, costPerItem, currencyLocale, quantity, productTitle);
        }
    }

    public static void setTheme(Theme theme) {
        if (isAndroidVersionSupported()) {
            checkInstance();
            ThemeManager.setTheme(theme);
        }
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static void release() {
        if (instance != null) {
            instance.monetizationSDK.release();
            instance = null;
        }
    }

    public static boolean isAdReady(NativeXAdPlacement name) {
        return isAdReady(name.toString());
    }

    public static boolean isAdReady(String name) {
        if (name != null) {
            return MRAIDManager.isAdReady(PlacementType.INTERSTITIAL, name);
        }
        Log.e("No Placement Provided!");
        return false;
    }

    public static final void showAd(Activity activity, NativeXAdPlacement name) {
        showAd(activity, name.toString());
    }

    public static final void showAd(Activity activity, String customPlacement) {
        showAd(activity, customPlacement, null);
    }

    public static final void showAd(Activity activity, NativeXAdPlacement name, OnAdEventBase listener) {
        showAd(activity, name.toString(), listener);
    }

    public static final void showAd(final Activity activity, final String customPlacement, final OnAdEventBase listener) {
        if (!isAndroidVersionSupported()) {
            return;
        }
        if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
            setOrCheckStatelessMode(false);
            checkPlacementName(customPlacement);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        MRAIDManager.showInterstitial(activity, customPlacement, listener, true);
                    } catch (Throwable tr) {
                        Log.e("Unhandled exception/error", tr);
                    }
                }
            });
            return;
        }
        Log.e("Cannot show ad; no internet connection available!");
        if (listener == null) {
            return;
        }
        if (listener instanceof OnAdEvent) {
            ((OnAdEvent) listener).onEvent(AdEvent.ERROR, "Cannot show ad; no internet connection exists!");
        } else if (listener instanceof OnAdEventV2) {
            AdInfo adInfo = new AdInfo();
            adInfo.setPlacement(customPlacement);
            ((OnAdEventV2) listener).onEvent(AdEvent.ERROR, adInfo, "Cannot show ad; no internet connection available!");
        }
    }

    public static final void showReadyAd(Activity activity, NativeXAdPlacement name, OnAdEventBase listener) {
        showReadyAd(activity, name.toString(), listener);
    }

    public static final void showReadyAd(final Activity activity, final String customPlacement, final OnAdEventBase listener) {
        if (!isAndroidVersionSupported()) {
            return;
        }
        if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
            setOrCheckStatelessMode(false);
            checkPlacementName(customPlacement);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (MonetizationManager.isAdReady(customPlacement)) {
                        try {
                            MRAIDManager.showInterstitial(activity, customPlacement, listener, true);
                        } catch (Throwable tr) {
                            Log.e("Unhandled exception/error", tr);
                        }
                    }
                }
            });
            return;
        }
        Log.e("Cannot show ad; no internet connection available!");
        if (listener == null) {
            return;
        }
        if (listener instanceof OnAdEvent) {
            ((OnAdEvent) listener).onEvent(AdEvent.ERROR, "Cannot show ad; no internet connection available!");
        } else if (listener instanceof OnAdEventV2) {
            AdInfo adInfo = new AdInfo();
            adInfo.setPlacement(customPlacement);
            ((OnAdEventV2) listener).onEvent(AdEvent.ERROR, adInfo, "Cannot show ad; no internet connection available!");
        }
    }

    public static final void showReadyAdStateless(Activity activity, NativeXAdPlacement placement, OnAdEventBase listener) {
        showReadyAdStateless(activity, placement.toString(), listener);
    }

    public static final void showReadyAdStateless(final Activity activity, final String placementName, final OnAdEventBase listener) {
        if (!isAndroidVersionSupported()) {
            return;
        }
        if (NetworkConnectionManager.getInstance(activity).isConnected()) {
            setOrCheckStatelessMode(true);
            checkPlacementName(placementName);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (MonetizationManager.isAdReady(placementName)) {
                        try {
                            MRAIDManager.showInterstitial(activity, placementName, listener, true);
                            return;
                        } catch (Throwable tr) {
                            Log.e("Unhandled exception/error", tr);
                            return;
                        }
                    }
                    Log.w("Ad is not ready to show.. ");
                }
            });
            return;
        }
        Log.e("Cannot show ad; no internet connection available!");
        if (listener == null) {
            return;
        }
        if (listener instanceof OnAdEvent) {
            ((OnAdEvent) listener).onEvent(AdEvent.ERROR, "Cannot show ad; no internet connection available!");
        } else if (listener instanceof OnAdEventV2) {
            AdInfo adInfo = new AdInfo();
            adInfo.setPlacement(placementName);
            ((OnAdEventV2) listener).onEvent(AdEvent.ERROR, adInfo, "Cannot show ad; no internet connection available!");
        }
    }

    public static final void showBannerAd(Activity activity, String customPlacement, Rect position) {
        showBannerAd(activity, customPlacement, position, null);
    }

    public static final void showBannerAd(Activity activity, String customPlacement, BannerPosition position) {
        showBannerAd(activity, customPlacement, position, null);
    }

    public static final void showBannerAd(Activity activity, NativeXAdPlacement placement, BannerPosition position) {
        showBannerAd(activity, placement, position, null);
    }

    public static final void showBannerAd(Activity activity, NativeXAdPlacement name, Rect position, OnAdEventBase listener) {
        showBannerAd(activity, name.toString(), position, listener);
    }

    public static final void showBannerAd(Activity activity, NativeXAdPlacement name, BannerPosition position, OnAdEventBase listener) {
        showBannerAd(activity, name.toString(), position, listener);
    }

    public static final void showBannerAd(Activity activity, String customPlacement, Rect position, OnAdEventBase listener) {
        showBannerAd(activity, customPlacement, position, null, listener);
    }

    public static final void showBannerAd(Activity activity, String customPlacement, BannerPosition position, OnAdEventBase listener) {
        showBannerAd(activity, customPlacement, null, position, listener);
    }

    private static void showBannerAd(Activity activity, String customPlacement, Rect position, BannerPosition adPosition, OnAdEventBase listener) {
        if (isAndroidVersionSupported()) {
            setOrCheckStatelessMode(false);
            checkPlacementName(customPlacement);
            checkPosition(position, adPosition, customPlacement);
            final Activity activity2 = activity;
            final String str = customPlacement;
            final Rect rect = position;
            final BannerPosition bannerPosition = adPosition;
            final OnAdEventBase onAdEventBase = listener;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    MRAIDManager.showBanner(activity2, str, rect, bannerPosition, onAdEventBase, true);
                }
            });
        }
    }

    private static void checkPosition(Rect position, BannerPosition enumPosition, String placementName) {
        if (enumPosition == null) {
            if (position == null || position.width() < 0 || position.height() < 0) {
                throw new NullPointerException("Invalid placement position in " + placementName + ".");
            }
        }
    }

    public static final void update(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                MRAIDManager.update(activity);
            }
        });
    }

    public static final void fetchAd(Activity activity, NativeXAdPlacement name, OnAdEventBase listener) {
        fetchAd(activity, name.toString(), listener);
    }

    public static final void fetchAd(final Activity activity, final String customPlacement, final OnAdEventBase listener) {
        if (!isAndroidVersionSupported()) {
            return;
        }
        if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
            setOrCheckStatelessMode(false);
            checkPlacementName(customPlacement);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    MRAIDManager.cacheInterstitial(activity, customPlacement, listener);
                }
            });
            return;
        }
        Log.e("Cannot fetch ad; no internet connection available!");
        if (listener == null) {
            return;
        }
        if (listener instanceof OnAdEvent) {
            ((OnAdEvent) listener).onEvent(AdEvent.ERROR, "Cannot fetch ad; no internet connection available!");
        } else if (listener instanceof OnAdEventV2) {
            AdInfo adInfo = new AdInfo();
            adInfo.setPlacement(customPlacement);
            ((OnAdEventV2) listener).onEvent(AdEvent.ERROR, adInfo, "Cannot fetch ad; no internet connection available!");
        }
    }

    public static final void fetchAdStateless(Activity activity, String appId, NativeXAdPlacement placement, OnAdEventBase listener) {
        fetchAdStateless(activity, appId, placement.toString(), listener);
    }

    public static final void fetchAdStateless(final Activity activity, String appId, final String customPlacement, final OnAdEventBase listener) {
        Log.d("fetching ad stateless:" + customPlacement);
        if (isAndroidVersionSupported()) {
            setOrCheckStatelessMode(true);
            checkPlacementName(customPlacement);
            if (NetworkConnectionManager.getInstance(activity).isConnected()) {
                createInstance();
                synchronized (sStatelessAdSessionLock) {
                    Log.d("in stateless session lock: fetch_" + customPlacement);
                    long currentTimeSeconds = SystemClock.elapsedRealtime() / 1000;
                    if (instance.monetizationSDK.getSessionId() == null || instance.monetizationSDK.getSessionId().isEmpty() || currentTimeSeconds - sLastStatelessSessionCreateTimeSeconds > 3600) {
                        if (currentTimeSeconds - sLastStatelessSessionAttemptTimeSeconds > 15) {
                            sStatelessPlacementsAwaitingSession.clear();
                        }
                        if (sStatelessPlacementsAwaitingSession.size() == 0) {
                            Log.d("first stateless fetch, calling CreateSession: " + customPlacement);
                            sLastStatelessSessionAttemptTimeSeconds = SystemClock.elapsedRealtime() / 1000;
                            createSession((Context) activity, appId, new SessionListener() {
                                public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
                                    MonetizationManager.onStatelessCreateSessionCompleted(success);
                                }
                            });
                        }
                        sStatelessPlacementsAwaitingSession.add(new PlacementFetchSet(customPlacement, activity, listener));
                        Log.d("exiting stateless session lock: fetch_" + customPlacement);
                        return;
                    }
                    Log.d("exiting stateless session lock: fetch_" + customPlacement);
                    Log.d("session exists, calling fetch on " + customPlacement);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            MRAIDManager.cacheInterstitial(activity, customPlacement, listener);
                        }
                    });
                    return;
                }
            }
            Log.e("Cannot fetch ad; no internet connection available!");
            if (listener == null) {
                return;
            }
            if (listener instanceof OnAdEvent) {
                ((OnAdEvent) listener).onEvent(AdEvent.ERROR, "Cannot fetch ad; no internet connection available!");
            } else if (listener instanceof OnAdEventV2) {
                AdInfo adInfo = new AdInfo();
                adInfo.setPlacement(customPlacement);
                ((OnAdEventV2) listener).onEvent(AdEvent.ERROR, adInfo, "Cannot fetch ad; no internet connection available!");
            }
        }
    }

    private static void onStatelessCreateSessionCompleted(boolean success) {
        synchronized (sStatelessAdSessionLock) {
            Log.d("in stateless session lock: onStatelessCreateSessionCompleted");
            if (success) {
                sLastStatelessSessionCreateTimeSeconds = SystemClock.elapsedRealtime() / 1000;
                Iterator it = sStatelessPlacementsAwaitingSession.iterator();
                while (it.hasNext()) {
                    final PlacementFetchSet fetchEntry = (PlacementFetchSet) it.next();
                    Log.d("calling fetch on " + fetchEntry.placementName);
                    fetchEntry.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            MRAIDManager.cacheInterstitial(fetchEntry.activity, fetchEntry.placementName, fetchEntry.adEventListener);
                        }
                    });
                }
                sStatelessPlacementsAwaitingSession.clear();
            } else {
                Log.w("CreateSession Stateless failed; cannot fetch placements..");
            }
            Log.d("exiting stateless session lock: onStatelessCreateSessionCompleted");
        }
    }

    public static final void fetchBannerAd(Activity activity, NativeXAdPlacement name, OnAdEventBase listener) {
        fetchBannerAd(activity, name.toString(), listener);
    }

    public static final void fetchBannerAd(final Activity activity, final String customPlacement, final OnAdEventBase listener) {
        if (isAndroidVersionSupported()) {
            setOrCheckStatelessMode(false);
            checkPlacementName(customPlacement);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    MRAIDManager.cacheBanner(activity, customPlacement, null, listener);
                }
            });
        }
    }

    public static final void reloadBannerAd(NativeXAdPlacement name) {
        if (isAndroidVersionSupported()) {
            MRAIDManager.reloadBanner(name.toString());
        }
    }

    public static final void reloadBannerAd(String customPlacement) {
        if (isAndroidVersionSupported()) {
            checkPlacementName(customPlacement);
            MRAIDManager.reloadBanner(customPlacement);
        }
    }

    public static final void setBannerAdPosition(NativeXAdPlacement name, Rect position) {
        if (isAndroidVersionSupported()) {
            setBannerAdPosition(name.toString(), position);
        }
    }

    public static final void setBannerAdPosition(NativeXAdPlacement name, BannerPosition position) {
        if (isAndroidVersionSupported()) {
            setBannerAdPosition(name.toString(), position);
        }
    }

    public static final void setBannerAdPosition(String customPlacement, BannerPosition position) {
        if (isAndroidVersionSupported()) {
            checkPlacementName(customPlacement);
            checkPosition(null, position, customPlacement);
            MRAIDManager.setAdPosition(customPlacement, null, position);
        }
    }

    public static final void setBannerAdPosition(String customPlacement, Rect position) {
        if (isAndroidVersionSupported()) {
            checkPlacementName(customPlacement);
            checkPosition(position, null, customPlacement);
            MRAIDManager.setAdPosition(customPlacement, position, null);
        }
    }

    public static final void dismissBannerAd(NativeXAdPlacement name) {
        if (isAndroidVersionSupported()) {
            MRAIDManager.releaseBanner(name.toString());
        }
    }

    public static final void dismissBannerAd(String customPlacement) {
        if (isAndroidVersionSupported()) {
            checkPlacementName(customPlacement);
            MRAIDManager.releaseBanner(customPlacement);
        }
    }

    public static final void dismissAd(NativeXAdPlacement name) {
        if (isAndroidVersionSupported()) {
            MRAIDManager.releaseInterstitial(name.toString());
        }
    }

    public static final void dismissAd(String customPlacement) {
        if (isAndroidVersionSupported()) {
            checkPlacementName(customPlacement);
            MRAIDManager.releaseInterstitial(customPlacement);
        }
    }

    private static void checkPlacementName(String name) {
        if (name == null || name.trim().equals("")) {
            throw new NullPointerException("Placement name cannot be empty or null in " + Thread.currentThread().getStackTrace()[3].getMethodName() + ".");
        }
    }

    private static void setOrCheckStatelessMode(boolean isStateless) {
        if (sIsStatelessMode == null) {
            sIsStatelessMode = Boolean.valueOf(isStateless);
        } else if (sIsStatelessMode.booleanValue() != isStateless) {
            android.util.Log.println(5, "NativeXSDK", "Do not use both regular and Stateless NativeX SDK fetch/show ad calls.  Doing so will cause breakage");
        }
    }
}
