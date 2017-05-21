package com.supersonic.mediationsdk.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.logger.ThreadExceptionHandler;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;

public class SupersonicUtils {
    private static final String DEFAULT_EVENTS_URL = "default_events_url";
    private static final String GENERAL_PROPERTIES = "general_properties";
    private static final String LAST_RESPONSE = "last_response";
    private static final String SDK_VERSION = "6.3.8";
    private static final String SHARED_PREFERENCES_NAME = "Mediation_Shared_Preferences";
    private static final String TAG = "SupersonicUtils";
    private static String mAppKey;

    public static String getMD5(String input) {
        try {
            String bigInteger = new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes())).toString(16);
            while (bigInteger.length() < 32) {
                bigInteger = "0" + bigInteger;
            }
            return bigInteger;
        } catch (Throwable e) {
            if (input == null) {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getMD5(input:null)", e);
            } else {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getMD5(input:" + input + ")", e);
            }
            return "";
        }
    }

    public static String getSHA256(String input) {
        try {
            return String.format("%064x", new Object[]{new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(input.getBytes()))});
        } catch (NoSuchAlgorithmException e) {
            if (input == null) {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getSHA256(input:null)", e);
            } else {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getSHA256(input:" + input + ")", e);
            }
            return "";
        }
    }

    public static String getTransId(String strToTransId) {
        return getSHA256(strToTransId);
    }

    public static int getCurrentTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String getSDKVersion() {
        return SDK_VERSION;
    }

    public static void put(JSONObject jsonSettings, String key, String value) {
        try {
            jsonSettings.put(key, value);
        } catch (Throwable e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:put(jsonSettings:" + jsonSettings + ", " + "key:" + key + ", " + "value:" + value + ")", e);
        }
    }

    public static void put(JSONObject jsonSettings, String key, boolean value) {
        try {
            jsonSettings.put(key, value);
        } catch (Throwable e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:put(jsonSettings:" + jsonSettings + ", " + "key:" + key + ", " + "value:" + value + ")", e);
        }
    }

    public static void put(JSONObject jsonSettings, String key, int value) {
        try {
            jsonSettings.put(key, value);
        } catch (Throwable e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:put(jsonSettings:" + jsonSettings + ", " + "key:" + key + ", " + "value:" + value + ")", e);
        }
    }

    public static String get(JSONObject jsonSettings, String key) {
        String result = "";
        try {
            result = jsonSettings.optString(key);
        } catch (Throwable e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:put(jsonSettings:" + jsonSettings + ", " + "key:" + key + ")", e);
        }
        return result;
    }

    public static void createAndStartWorker(Runnable runnable, String threadName) {
        Thread worker = new Thread(runnable, threadName);
        worker.setUncaughtExceptionHandler(new ThreadExceptionHandler());
        worker.start();
    }

    public static String getBase64Auth(String loginUsername, String loginPass) {
        return "Basic " + Base64.encodeToString((loginUsername + ":" + loginPass).getBytes(), 10);
    }

    public static synchronized void saveDefaultEventsURL(Context context, String eventsUrl) {
        synchronized (SupersonicUtils.class) {
            try {
                Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
                editor.putString(DEFAULT_EVENTS_URL, eventsUrl);
                editor.commit();
            } catch (Exception e) {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:saveDefaultEventsURL(eventsUrl:" + eventsUrl + ")", e);
            }
        }
    }

    public static String getDefaultEventsURL(Context context, String defaultEventsURL) {
        String serverUrl = defaultEventsURL;
        try {
            serverUrl = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getString(DEFAULT_EVENTS_URL, defaultEventsURL);
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "SupersonicUtils:getDefaultEventsURL(defaultEventsURL:" + defaultEventsURL + ")", e);
        }
        return serverUrl;
    }

    public static synchronized void saveLastResponse(Context context, String response) {
        synchronized (SupersonicUtils.class) {
            Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
            editor.putString(LAST_RESPONSE, response);
            editor.apply();
        }
    }

    public static String getLastResponse(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getString(LAST_RESPONSE, "");
    }

    static synchronized void saveGeneralProperties(Context context, JSONObject properties) {
        synchronized (SupersonicUtils.class) {
            if (!(context == null || properties == null)) {
                Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).edit();
                editor.putString(GENERAL_PROPERTIES, properties.toString());
                editor.apply();
            }
        }
    }

    public static synchronized JSONObject getGeneralProperties(Context context) {
        Object result;
        synchronized (SupersonicUtils.class) {
            JSONObject result2 = new JSONObject();
            if (context == null) {
                result = result2;
            } else {
                try {
                    result2 = new JSONObject(context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0).getString(GENERAL_PROPERTIES, result2.toString()));
                } catch (JSONException e) {
                }
                JSONObject result3 = result2;
            }
        }
        return result;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }
}
