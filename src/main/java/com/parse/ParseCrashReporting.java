package com.parse;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public final class ParseCrashReporting {
    static final String CRASH_REPORT = "_CrashReport";

    private ParseCrashReporting() {
    }

    public static void enable(Context context) {
        ParseCrashReporter.enableCrashReporting(context.getApplicationContext());
    }

    public static boolean isCrashReportingEnabled() {
        return ParseCrashReporter.isEnabled();
    }

    static void trackCrashReport(JSONObject crashReportData) {
        Map parameters = new HashMap();
        parameters.put("acraDump", crashReportData);
        Parse.getEventuallyQueue().enqueueEventuallyAsync(ParseRESTAnalyticsCommand.trackEventCommand(CRASH_REPORT, parameters, ParseUser.getCurrentSessionToken()), null);
    }
}
