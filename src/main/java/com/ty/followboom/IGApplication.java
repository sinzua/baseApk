package com.ty.followboom;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.ty.followboom.helpers.TrackHelper;
import java.util.ArrayList;
import java.util.Iterator;

public class IGApplication extends Application {
    private static IGApplication sInstance = null;
    private ArrayList<Activity> mList = new ArrayList();

    public static IGApplication getInstance() {
        if (sInstance == null) {
            sInstance = new IGApplication();
        }
        return sInstance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void onCreate() {
        super.onCreate();
        enableStrictModeInDebugMode();
        TrackHelper.initialize(this);
    }

    private void enableStrictModeInDebugMode() {
    }

    public void addActivity(Activity activity) {
        this.mList.add(activity);
    }

    public void closeAllActivities() {
        try {
            Iterator it = this.mList.iterator();
            while (it.hasNext()) {
                Activity activity = (Activity) it.next();
                if (!(activity == null || (activity instanceof LoginActivity))) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
