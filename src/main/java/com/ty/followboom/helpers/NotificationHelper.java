package com.ty.followboom.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import com.ty.instaview.R;

public class NotificationHelper {
    public static final int NOTIFICATION_FLAG_DAILY_LOGIN = 1;
    public static final int NOTIFICATION_RATE_US_LOGIN = 2;
    private static final String TAG = "NotificationHelper";

    public static void notify(Context context, String title, String content, int id) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService("notification");
            Notification notify = new Notification();
            notify.icon = R.drawable.ic_launcher;
            notify.tickerText = title + " \n " + content;
            notify.when = System.currentTimeMillis();
            notify.number = 1;
            notify.defaults = 1;
            notify.flags |= 1;
            notify.flags |= 2;
            notify.flags |= 16;
            manager.notify(id, notify);
        } catch (Exception e) {
            Log.d(TAG, "Notification Exception: " + e.toString());
        }
    }
}
