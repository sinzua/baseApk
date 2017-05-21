package com.ty.followboom.helpers;

import android.content.Context;
import android.util.Log;
import com.parse.ConfigCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseConfig;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import java.util.HashMap;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class ParseHelper {
    public static final String PARSE_APPLICATION_ID = "qjE0SYClXQJX3guq0r24L4cSgb3zkFccBLiEhJd6";
    public static final String PARSE_CLIENT_KEY = "5PsAdMBlfMbhKIWMiHhCm1KPRSjNOZlc0QSWjmKX";
    protected static final String TAG = "ParseHelper";

    public static void initialize(Context context) {
        try {
            Parse.enableLocalDatastore(context);
            ParseCrashReporting.enable(context);
            Parse.initialize(context, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        } catch (IllegalStateException e) {
        } finally {
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }
    }

    public static void track(String category, String action, String label) {
        HashMap<String, String> dimensions = new HashMap();
        dimensions.put(action, label);
        ParseAnalytics.trackEvent(category, dimensions);
    }

    public static void signup(Context context) {
        ParseUser user = new ParseUser();
        user.setUsername(AppConfigHelper.getUserInfo(context).getUsername());
        user.setPassword(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(ParseHelper.TAG, "parse user signup succeed!");
                    return;
                }
                Log.d(ParseHelper.TAG, String.format("parse user signup failed! %s", new Object[]{e.toString()}));
            }
        });
    }

    public static void getCurrentConfig(final Context context) {
        Log.d(TAG, "Getting the latest config...");
        ParseConfig.getInBackground(new ConfigCallback() {
            public void done(ParseConfig config, ParseException e) {
                if (e == null) {
                    Log.d("TAG", "Yay! Config was fetched from the server.");
                } else {
                    Log.d("TAG", "Failed to fetch. Using Cached Config.");
                    config = ParseConfig.getCurrentConfig();
                }
                AppConfigHelper.saveInReviewVersion(context, config.getString(AppConfigHelper.IN_REVIEW_VERSION, "1"));
                Log.d("TAG", String.format("Welcome Messsage From Config = %s", new Object[]{config.getString(AppConfigHelper.IN_REVIEW_VERSION, "1")}));
            }
        });
    }
}
