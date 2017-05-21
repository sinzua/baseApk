package com.nativex.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"CommitPrefEdits"})
public class SharedPreferenceManager {
    private static final String PREFS_NAME = "com.nativex.monetization";
    private static final String PREF_DEVICE_ID = "DeviceId";
    private static final String PREF_STORED_BALANCES = "StoredBalances";
    private static final String PREVIOUS_CACHED_TIME = "PreviousCachedTime";
    private static final String PREVIOUS_SESSION_ID = "PreviousSessionIdString";
    private static String sBuildType = "Native";
    private static SharedPreferences sharedPreferences;

    private SharedPreferenceManager() {
    }

    public static boolean isInitialized() {
        return sharedPreferences != null;
    }

    public static void initialize(Context context) {
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        }
    }

    public static void release() {
        sharedPreferences = null;
    }

    public static String getBuildType() {
        return sBuildType;
    }

    public static void setBuildType(String buildType) {
        sBuildType = buildType;
    }

    public static void storePreviousSessionId(String id) {
        try {
            checkInit();
            sharedPreferences.edit().putString(PREVIOUS_SESSION_ID, id).commit();
        } catch (Exception e) {
            Log.e("Failed to store previous session id", e);
        }
    }

    public static String getPreviousSessionId() {
        String str = null;
        try {
            checkInit();
            str = sharedPreferences.getString(PREVIOUS_SESSION_ID, null);
        } catch (Exception e) {
            Log.e("Failed to retrieve previous session id", e);
        }
        return str;
    }

    public static void storeDeviceId(String deviceId) {
        try {
            checkInit();
            Editor editor = sharedPreferences.edit();
            editor.putString(PREF_DEVICE_ID, deviceId);
            editor.commit();
        } catch (Exception e) {
            Log.e("SharedPreferenceManager: Exception caught while storing DeviceId", e);
        }
    }

    public static String getDeviceId() {
        try {
            checkInit();
            if (sharedPreferences.getString(PREF_DEVICE_ID, null) != null) {
                String deviceIdValue = sharedPreferences.getString(PREF_DEVICE_ID, null);
                Log.i("DeviceId was found in shared preferences.  This is a known device.");
                return deviceIdValue;
            }
            Log.i("DeviceId was not found in shared preferences. Generating a new id.");
            return null;
        } catch (Exception e) {
            Log.d("SharedPreferencesManager: Unexpected exception caught in getDeviceId()");
            e.printStackTrace();
            return null;
        }
    }

    public static void storeBalances(Map<String, Reward> balances) {
        try {
            checkInit();
            sharedPreferences.edit().putString(PREF_STORED_BALANCES, new Gson().toJson((Object) balances)).commit();
        } catch (Exception e) {
            Log.e("Failed to store balances for later use", e);
        }
    }

    public static Map<String, Reward> getBalances() {
        try {
            checkInit();
            String balancesJson = sharedPreferences.getString(PREF_STORED_BALANCES, null);
            if (!TextUtils.isEmpty(balancesJson)) {
                return (Map) new Gson().fromJson(balancesJson, new TypeToken<Map<String, Reward>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("Failed to get balances", e);
        }
        return new HashMap();
    }

    public static void storePreviousCachedTime(long cachedTime) {
        try {
            checkInit();
            Editor editor = sharedPreferences.edit();
            editor.putLong(PREVIOUS_CACHED_TIME, cachedTime);
            editor.commit();
        } catch (Exception e) {
            Log.e("SharedPreferenceManager: Exception caught while storing previous cached time ", e);
        }
    }

    public static long getPreviousCachedTime() {
        long j = 0;
        try {
            checkInit();
            j = sharedPreferences.getLong(PREVIOUS_CACHED_TIME, 0);
        } catch (Exception e) {
            Log.e("SharedPreferenceManager: Exception caught while loading previous cached time", e);
        }
        return j;
    }

    private static void checkInit() {
        if (!isInitialized()) {
            if (MonetizationSharedDataManager.getContext() != null) {
                initialize(MonetizationSharedDataManager.getContext());
                return;
            }
            throw new NullPointerException("SharedPreferencesManager not initialized");
        }
    }
}
