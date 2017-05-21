package com.parse;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseIntArray;
import java.util.concurrent.atomic.AtomicInteger;

class ParseNotificationManager {
    public static final String TAG = "com.parse.ParseNotificationManager";
    private SparseIntArray iconIds = new SparseIntArray();
    private final Object lock = new Object();
    private final AtomicInteger notificationCount = new AtomicInteger(0);
    private volatile boolean shouldShowNotifications = true;

    public static class Singleton {
        private static final ParseNotificationManager INSTANCE = new ParseNotificationManager();
    }

    ParseNotificationManager() {
    }

    public static ParseNotificationManager getInstance() {
        return Singleton.INSTANCE;
    }

    public boolean getShouldShowNotifications() {
        return this.shouldShowNotifications;
    }

    public void setShouldShowNotifications(boolean show) {
        this.shouldShowNotifications = show;
    }

    public int getNotificationCount() {
        return this.notificationCount.get();
    }

    public boolean isValidIconId(Context context, int iconId) {
        synchronized (this.lock) {
            int valid = this.iconIds.get(iconId, -1);
        }
        if (valid == -1) {
            Drawable drawable = null;
            try {
                drawable = context.getResources().getDrawable(iconId);
            } catch (NotFoundException e) {
            }
            synchronized (this.lock) {
                if (drawable == null) {
                    valid = 0;
                } else {
                    valid = 1;
                }
                this.iconIds.put(iconId, valid);
            }
        }
        if (valid == 1) {
            return true;
        }
        return false;
    }

    public Notification createNotification(Context context, String title, String body, Class<? extends Activity> clazz, int iconId, Bundle extras) {
        if (!isValidIconId(context, iconId)) {
            PLog.e(TAG, "Icon id " + iconId + " is not a valid drawable. Trying to fall back to " + "default app icon.");
            iconId = ManifestInfo.getIconId();
        }
        if (iconId == 0) {
            PLog.e(TAG, "Could not find a valid icon id for this app. This is required to create a Notification object to show in the status bar. Make sure that the <application> in in your Manifest.xml has a valid android:icon attribute.");
            return null;
        } else if (context == null || title == null || body == null || clazz == null || iconId == 0) {
            PLog.e(TAG, "Must specify non-null context, title, body, and activity class to show notification.");
            return null;
        } else {
            long when = System.currentTimeMillis();
            ComponentName name = new ComponentName(context, clazz);
            Intent intent = new Intent();
            intent.setComponent(name);
            intent.setFlags(268435456);
            if (extras != null) {
                intent.putExtras(extras);
            }
            PendingIntent contentIntent = PendingIntent.getActivity(context, (int) when, intent, 0);
            Notification notification = new Notification(iconId, body, when);
            notification.flags |= 16;
            notification.defaults |= -1;
            notification.setLatestEventInfo(context, title, body, contentIntent);
            return notification;
        }
    }

    public void showNotification(Context context, Notification notification) {
        if (context != null && notification != null) {
            this.notificationCount.incrementAndGet();
            if (this.shouldShowNotifications) {
                NotificationManager nm = (NotificationManager) context.getSystemService("notification");
                int notificationId = (int) System.currentTimeMillis();
                try {
                    nm.notify(notificationId, notification);
                } catch (SecurityException e) {
                    notification.defaults = 5;
                    nm.notify(notificationId, notification);
                }
            }
        }
    }

    public void showNotification(Context context, String title, String body, Class<? extends Activity> cls, int iconId, Bundle extras) {
        showNotification(context, createNotification(context, title, body, cls, iconId, extras));
    }
}
