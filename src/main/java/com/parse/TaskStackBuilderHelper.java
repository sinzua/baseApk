package com.parse;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

@TargetApi(16)
class TaskStackBuilderHelper {
    TaskStackBuilderHelper() {
    }

    public static void startActivities(Context context, Class<? extends Activity> cls, Intent activityIntent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(activityIntent);
        stackBuilder.startActivities();
    }
}
