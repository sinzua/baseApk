package com.nativex.monetization.manager;

import android.os.SystemClock;
import com.nativex.common.Log;
import com.nativex.common.OnTaskCompletedListener;
import com.nativex.common.SharedPreferenceManager;
import com.nativex.common.Utilities;
import com.nativex.monetization.business.CreateSessionResponseData;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.listeners.SessionListener;

public class SessionManager {
    private static final long SessionExpiryMS = 1800000;
    private static final int SessionExpiryMinutes = 30;
    private static long lastSessionResponseMs = 0;
    private static CreateSessionResponseData sessionResponse;

    private SessionManager() {
    }

    public static String getSessionId() {
        if (sessionResponse != null) {
            return sessionResponse.getSessionId();
        }
        return null;
    }

    public static boolean hasPreviousSessionId() {
        if (getPreviousSessionId() == null) {
            return false;
        }
        return true;
    }

    public static String getPreviousSessionId() {
        return SharedPreferenceManager.getPreviousSessionId();
    }

    public static void setSessionResponse(CreateSessionResponseData sessionResponse) {
        if (sessionResponse != sessionResponse) {
            clearSession();
        }
        sessionResponse = sessionResponse;
    }

    public static boolean hasSession() {
        if (sessionResponse != null) {
            return true;
        }
        return false;
    }

    public static boolean isBackupAdsEnabled() {
        if (sessionResponse != null) {
            return sessionResponse.isBackupAdsEnabled();
        }
        return true;
    }

    public static boolean shouldReplaceWebViewUserAgent() {
        if (sessionResponse != null) {
            return sessionResponse.shouldReplaceWebViewUserAgent();
        }
        return true;
    }

    private static boolean hasSessionExpired() {
        if (lastSessionResponseMs <= 0 || Math.abs(SystemClock.elapsedRealtime() - lastSessionResponseMs) <= SessionExpiryMS) {
            return false;
        }
        return true;
    }

    public static void clearSession() {
        sessionResponse = null;
    }

    public static void createSession() {
        createSession(null);
    }

    public static void createSession(final SessionListener listener) {
        if (ServerRequestManager.getInstance().isCreateSessionExecuting()) {
            Log.w("createSession is currently running; wait for the current request to finish before calling another!");
            return;
        }
        boolean createNewSession;
        if (!hasSession() || hasSessionExpired()) {
            createNewSession = true;
        } else {
            createNewSession = false;
        }
        if (createNewSession) {
            ServerRequestManager.getInstance().createSession(new OnTaskCompletedListener() {
                public void onTaskCompleted() {
                    if (SessionManager.hasSession()) {
                        CacheManager.restartOrStopCaching(SessionManager.sessionResponse.getCachingFrequency());
                        Log.i("Running GetDeviceBalanceTask");
                        ServerRequestManager.getInstance().getDeviceBalance();
                        SessionManager.lastSessionResponseMs = SystemClock.elapsedRealtime();
                        Log.d("  isBackupAdsEnabled = " + SessionManager.sessionResponse.isBackupAdsEnabled());
                    }
                    if (listener == null) {
                        return;
                    }
                    if (SessionManager.sessionResponse != null) {
                        listener.createSessionCompleted(SessionManager.hasSession(), SessionManager.sessionResponse.isOfferwallEnabled(), SessionManager.getSessionId());
                    } else {
                        listener.createSessionCompleted(false, false, null);
                    }
                }
            });
            return;
        }
        Log.d("Short-circuiting createSession with previous session information...");
        CacheManager.restartOrStopCaching(sessionResponse.getCachingFrequency());
        if (listener == null) {
            return;
        }
        if (sessionResponse != null) {
            listener.createSessionCompleted(hasSession(), sessionResponse.isOfferwallEnabled(), getSessionId());
        } else {
            listener.createSessionCompleted(false, false, null);
        }
    }

    public static void storeSession() {
        if (SharedPreferenceManager.isInitialized() && sessionResponse != null && !Utilities.stringIsEmpty(sessionResponse.getSessionId())) {
            Log.d("SessionManager: Storing current session in SharedPreferences as previous session");
            SharedPreferenceManager.storePreviousSessionId(sessionResponse.getSessionId());
        }
    }
}
