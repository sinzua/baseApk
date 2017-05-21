package com.parse;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

class ParseWakeLock {
    private static final String TAG = "com.parse.ParseWakeLock";
    private static volatile boolean hasWakeLockPermission = true;
    private final WakeLock wakeLock;

    public static ParseWakeLock acquireNewWakeLock(Context context, int type, String reason, long timeout) {
        WakeLock wl = null;
        if (hasWakeLockPermission) {
            try {
                PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService("power");
                if (pm != null) {
                    wl = pm.newWakeLock(type, reason);
                    if (wl != null) {
                        wl.setReferenceCounted(false);
                        if (timeout == 0) {
                            wl.acquire();
                        } else {
                            wl.acquire(timeout);
                        }
                    }
                }
            } catch (SecurityException e) {
                PLog.e(TAG, "Failed to acquire a PowerManager.WakeLock. This isnecessary for reliable handling of pushes. Please add this to your Manifest.xml: <uses-permission android:name=\"android.permission.WAKE_LOCK\" /> ");
                hasWakeLockPermission = false;
                wl = null;
            }
        }
        return new ParseWakeLock(wl);
    }

    private ParseWakeLock(WakeLock wakeLock) {
        this.wakeLock = wakeLock;
    }

    public void release() {
        if (this.wakeLock != null) {
            this.wakeLock.release();
        }
    }
}
