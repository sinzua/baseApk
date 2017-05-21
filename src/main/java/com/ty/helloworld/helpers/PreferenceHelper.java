package com.ty.helloworld.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceHelper {
    public static final String FOLLOW_LIMIT_ARRAY = "follow_limit_array";
    public static final String FOLLOW_LIMIT_PARAMS = "follow_limit_params";
    public static final String KEY_APPINFO = "appinfo";
    public static final String KEY_USERINFO = "userinfo";
    public static final String LAST_RATE_VERSION = "last_rate_version";
    public static final String LIKE_LIMIT_ARRAY = "like_limit_array";
    public static final String LIKE_LIMIT_PARAMS = "like_limit_params";
    public static final String NEXT_DAILY_LOGIN_TIME = "daily_login";
    public static final String TASKREWARDS = "taskrewards";
    public static final String USERNAME = "username";
    private static SharedPreferences mySharedPreferences;

    public static String getContent(Context context, String filename, String key) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        return mySharedPreferences.getString(key, "");
    }

    public static void saveContent(Context context, String filename, String key, String value) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String filename, String key) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        return mySharedPreferences.getBoolean(key, false);
    }

    public static void saveBoolean(Context context, String filename, String key, boolean value) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Integer getInteger(Context context, String filename, String key) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        return Integer.valueOf(mySharedPreferences.getInt(key, 0));
    }

    public static void saveInteger(Context context, String filename, String key, int value) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        Editor editor = mySharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Long getLong(Context context, String filename, String key) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        return Long.valueOf(mySharedPreferences.getLong(key, 0));
    }

    public static void saveLong(Context context, String filename, String key, Long value) {
        mySharedPreferences = context.getSharedPreferences(filename, 2);
        Editor editor = mySharedPreferences.edit();
        editor.putLong(key, value.longValue());
        editor.commit();
    }
}
