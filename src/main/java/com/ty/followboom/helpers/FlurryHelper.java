package com.ty.followboom.helpers;

import android.content.Context;
import com.flurry.android.FlurryAgent;
import java.util.HashMap;
import java.util.Map;

public class FlurryHelper {
    public static final String FLURRY_APPLICATION_KEY = "9KBTSVTMZJ68NZ58P896";
    protected static final String TAG = "FlurryHelper";

    public static void initialize(Context context) {
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(context, FLURRY_APPLICATION_KEY);
    }

    public static void track(String category, String action, String label) {
        Map dimensions = new HashMap();
        dimensions.put(action, label);
        FlurryAgent.logEvent(category, dimensions);
    }
}
