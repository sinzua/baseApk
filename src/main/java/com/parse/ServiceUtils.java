package com.parse;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

final class ServiceUtils {
    private static final String TAG = "com.parse.ServiceUtils";
    private static final String WAKE_LOCK_EXTRA = "parseWakeLockId";
    private static int wakeLockId = 0;
    private static final SparseArray<ParseWakeLock> wakeLocks = new SparseArray();

    ServiceUtils() {
    }

    public static boolean runIntentInService(Context context, Intent intent, Class<? extends Service> clazz) {
        boolean startedService = false;
        if (intent != null) {
            if (clazz != null) {
                intent.setClass(context, clazz);
            }
            startedService = context.startService(intent) != null;
            if (!startedService) {
                PLog.e(TAG, "Could not start the service. Make sure that the XML tag <service android:name=\"" + clazz + "\" /> is in your " + "AndroidManifest.xml as a child of the <application> element.");
            }
        }
        return startedService;
    }

    public static boolean runWakefulIntentInService(Context context, Intent intent, Class<? extends Service> clazz) {
        boolean startedService = false;
        if (intent != null) {
            ParseWakeLock wl = ParseWakeLock.acquireNewWakeLock(context, 1, intent.toString(), 0);
            synchronized (wakeLocks) {
                intent.putExtra(WAKE_LOCK_EXTRA, wakeLockId);
                wakeLocks.append(wakeLockId, wl);
                wakeLockId++;
            }
            startedService = runIntentInService(context, intent, clazz);
            if (!startedService) {
                completeWakefulIntent(intent);
            }
        }
        return startedService;
    }

    public static void completeWakefulIntent(Intent intent) {
        if (intent != null && intent.hasExtra(WAKE_LOCK_EXTRA)) {
            ParseWakeLock wakeLock;
            int id = intent.getIntExtra(WAKE_LOCK_EXTRA, -1);
            synchronized (wakeLocks) {
                wakeLock = (ParseWakeLock) wakeLocks.get(id);
                wakeLocks.remove(id);
            }
            if (wakeLock == null) {
                PLog.e(TAG, "Got wake lock id of " + id + " in intent, but no such lock found in " + "global map. Was completeWakefulIntent called twice for the same intent?");
            } else {
                wakeLock.release();
            }
        }
    }
}
