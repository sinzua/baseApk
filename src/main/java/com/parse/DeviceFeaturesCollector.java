package com.parse;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

class DeviceFeaturesCollector {
    DeviceFeaturesCollector() {
    }

    public static String getFeatures(Context ctx) {
        if (Compatibility.getAPILevel() < 5) {
            return "Data available only with API Level > 5";
        }
        StringBuffer result = new StringBuffer();
        try {
            Object[] features = (Object[]) PackageManager.class.getMethod("getSystemAvailableFeatures", (Class[]) null).invoke(ctx.getPackageManager(), new Object[0]);
            if (features != null) {
                for (Object feature : features) {
                    String featureName = (String) feature.getClass().getField("name").get(feature);
                    if (featureName != null) {
                        result.append(featureName);
                    } else {
                        String glEsVersion = (String) feature.getClass().getMethod("getGlEsVersion", (Class[]) null).invoke(feature, new Object[0]);
                        result.append("glEsVersion = ");
                        result.append(glEsVersion);
                    }
                    result.append("\n");
                }
            }
        } catch (Throwable e) {
            Log.w(ACRA.LOG_TAG, "Couldn't retrieve device features for " + ctx.getPackageName(), e);
            result.append("Could not retrieve data: ");
            result.append(e.getMessage());
        }
        return result.toString();
    }
}
