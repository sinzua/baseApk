package com.nativex.monetization;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.nativex.common.Log;
import com.nativex.common.NetworkConnectionManager;
import com.nativex.common.ServerConfig;
import com.nativex.common.SharedPreferenceManager;
import com.nativex.common.Utilities;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.manager.CacheFileManager;
import com.nativex.monetization.manager.CacheManager;
import com.nativex.monetization.manager.ManagementService;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.manager.SessionManager;
import com.nativex.monetization.manager.StringsManager;
import com.nativex.monetization.mraid.MRAIDManager;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.volleytoolbox.NativeXVolley;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.reflect.Method;

public class MonetizationSDK {
    private static HandlerThread handlerThread;
    private static Handler threadHandler;
    private final String advertisingClientClassName = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private final String advertisingInfoClassName = "com.google.android.gms.ads.identifier.AdvertisingIdClient$Info";
    private boolean isAdvertiserIdTaskRunning = false;

    public MonetizationSDK() {
        handlerThread = new HandlerThread("FetchAdvertiserId-Thread");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());
    }

    public void initialize(Context context, ApplicationInputs inputs) {
        if (inputs == null) {
            Log.e("Inputs cannot be null in MonetizationSDK.initialize().");
            throw new IllegalArgumentException("Inputs cannot be null in MonetizationSDK.initialize().");
        } else {
            initialize(context, inputs.getAppId(), inputs.getApplicationName(), inputs.getPublisherUserId());
        }
    }

    void initialize(Context context, String appId, String applicationName, String publisherUserId) {
        boolean isSDKUninitialized = true;
        android.util.Log.i("nativeX", "Using NativeX MonetizationSDK version 5.5.6");
        if (context == null) {
            Log.e("Context cannot be null in MonetizationSDK.initialize().");
            throw new IllegalArgumentException("Context cannot be null in MonetizationSDK.initialize().");
        }
        if (applicationName == null) {
            ApplicationInfo info = context.getApplicationInfo();
            if (info == null || Utilities.stringIsEmpty(info.name)) {
                applicationName = "";
            } else {
                applicationName = info.name;
            }
        }
        if (publisherUserId == null) {
            publisherUserId = "";
        }
        if (appId == null || appId.length() == 0) {
            String err = appId + " is not a valid application id.";
            Log.e(err);
            throw new IllegalArgumentException(err);
        }
        if (appId.startsWith("@")) {
            ServerConfig.setTestEndpointEnabled(true);
        } else {
            ServerConfig.setTestEndpointEnabled(false);
        }
        if (MonetizationSharedDataManager.getContext() != null) {
            isSDKUninitialized = false;
        }
        MonetizationSharedDataManager.setContext(context.getApplicationContext());
        MonetizationSharedDataManager.setAppId(appId);
        MonetizationSharedDataManager.setApplicationName(applicationName);
        MonetizationSharedDataManager.setPublisherUserId(publisherUserId);
        SharedPreferenceManager.initialize(context);
        StringsManager.initialize();
        if (isSDKUninitialized) {
            MonetizationSharedDataManager.setApplicationFilesDirectoryPath(context.getFilesDir().getPath());
            CacheFileManager.createNativeXCacheDirectoryIfNotExists();
            CacheManager.getInstance().verifyFileStatusForAllRecords();
            MonetizationSharedDataManager.setWebViewUserAgent(getUserAgent(context));
        }
    }

    public void enableLogging(boolean isEnabled) {
        Log.enableLogging(isEnabled);
    }

    public void release() {
        try {
            monetizationSDKRelease();
            MRAIDManager.release();
            SessionManager.clearSession();
            CacheManager.release();
            NativeXVolley.release();
            NetworkConnectionManager.release();
            ServerRequestManager.release();
            SharedPreferenceManager.release();
            ManagementService.release();
            ThemeManager.release();
        } catch (Exception e) {
            Log.e("MonetizationSDK: Exception caught while releasing the managers", e);
        }
    }

    void monetizationSDKRelease() {
        if (handlerThread != null) {
            handlerThread.quit();
        }
        handlerThread = null;
        threadHandler = null;
    }

    @Deprecated
    public void redeemCurrency() {
        try {
            if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
                ServerRequestManager.getInstance().getDeviceBalance();
            } else {
                Log.e("Cannot redeem currency; no internet connection is available!");
            }
        } catch (Exception e) {
            Log.e("MonetizationSDK: Exception caught in redeemCurrency()", e);
        }
    }

    public void redeemRewards() {
        try {
            if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
                ServerRequestManager.getInstance().getDeviceBalance();
            } else {
                Log.e("Cannot redeem currency; no internet connection is available!");
            }
        } catch (Exception e) {
            Log.e("MonetizationSDK: Exception caught in redeemRewards()", e);
        }
    }

    public void createSession(SessionListener listener) {
        if (!this.isAdvertiserIdTaskRunning) {
            if (NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
                this.isAdvertiserIdTaskRunning = true;
                fetchAdvertiserInfoAndCreateSession(listener);
                return;
            }
            Log.e("Cannot create session; no internet connection exists!");
            if (listener != null) {
                listener.createSessionCompleted(false, false, "");
            }
        }
    }

    private void fetchAdvertiserInfoAndCreateSession(final SessionListener listener) {
        threadHandler.post(new Runnable() {
            public void run() {
                try {
                    Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
                    Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
                    MonetizationSDK.this.setAdvertiserIdInfo();
                } catch (ClassNotFoundException e) {
                    Log.d("MonetizationSDK: Google Play services jar not found.");
                    MonetizationSharedDataManager.setAdvertiserId(null);
                } catch (Exception e2) {
                    Log.e("Unhandled exception", e2);
                    MonetizationSharedDataManager.setAdvertiserId(null);
                }
                try {
                    MonetizationSDK.this.continueWithCreateSession(listener);
                } catch (Exception e22) {
                    Log.e("Unhandled exception", e22);
                }
            }
        });
    }

    void continueWithCreateSession(SessionListener listener) {
        SessionManager.createSession(listener);
        this.isAdvertiserIdTaskRunning = false;
    }

    private void setAdvertiserIdInfo() {
        try {
            Object adInfo = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getMethod("getAdvertisingIdInfo", new Class[]{Context.class}).invoke(null, new Object[]{MonetizationSharedDataManager.getContext()});
            Class<?> adInfoClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            Method getIdMethod = adInfoClass.getMethod("getId", new Class[0]);
            Method isLimitAdTrackingEnabledMethod = adInfoClass.getMethod(RequestParameters.isLAT, new Class[0]);
            Object getId = getIdMethod.invoke(adInfo, new Object[0]);
            Object isLimitAdTrackingEnabled = isLimitAdTrackingEnabledMethod.invoke(adInfo, new Object[0]);
            if (getId instanceof String) {
                MonetizationSharedDataManager.setAdvertiserId((String) getId);
            }
            if (isLimitAdTrackingEnabled instanceof Boolean) {
                MonetizationSharedDataManager.setLimitAdTracking(((Boolean) isLimitAdTrackingEnabled).booleanValue());
            }
        } catch (Exception e) {
            MonetizationSharedDataManager.setAdvertiserId(null);
        }
    }

    public String getSessionId() {
        if (SessionManager.hasSession()) {
            return SessionManager.getSessionId();
        }
        return null;
    }

    @TargetApi(17)
    String getUserAgent(Context context) {
        try {
            if (VERSION.SDK_INT >= 17) {
                return WebSettings.getDefaultUserAgent(context);
            }
            return new WebView(context).getSettings().getUserAgentString();
        } catch (Exception ex) {
            Log.e("Caught exception with getUserAgent call", ex);
            return null;
        }
    }
}
