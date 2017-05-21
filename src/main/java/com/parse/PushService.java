package com.parse;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.List;

public final class PushService extends Service {
    static final String ACTION_START_IF_REQUIRED = "com.parse.PushService.startIfRequired";
    private static final String TAG = "com.parse.PushService";
    private static boolean loggedStartError = false;
    private static List<ServiceLifecycleCallbacks> serviceLifecycleCallbacks = null;
    private ProxyService proxy;

    interface ServiceLifecycleCallbacks {
        void onServiceCreated(Service service);

        void onServiceDestroyed(Service service);
    }

    static void registerServiceLifecycleCallbacks(ServiceLifecycleCallbacks callbacks) {
        synchronized (PushService.class) {
            if (serviceLifecycleCallbacks == null) {
                serviceLifecycleCallbacks = new ArrayList();
            }
            serviceLifecycleCallbacks.add(callbacks);
        }
    }

    static void unregisterServiceLifecycleCallbacks(ServiceLifecycleCallbacks callbacks) {
        synchronized (PushService.class) {
            serviceLifecycleCallbacks.remove(callbacks);
            if (serviceLifecycleCallbacks.size() <= 0) {
                serviceLifecycleCallbacks = null;
            }
        }
    }

    private static void dispatchOnServiceCreated(Service service) {
        Object[] callbacks = collectServiceLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ServiceLifecycleCallbacks) callback).onServiceCreated(service);
            }
        }
    }

    private static void dispatchOnServiceDestroyed(Service service) {
        Object[] callbacks = collectServiceLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ServiceLifecycleCallbacks) callback).onServiceDestroyed(service);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object[] collectServiceLifecycleCallbacks() {
        /*
        r0 = 0;
        r2 = com.parse.PushService.class;
        monitor-enter(r2);
        r1 = serviceLifecycleCallbacks;	 Catch:{ all -> 0x001c }
        if (r1 != 0) goto L_0x000c;
    L_0x0008:
        r1 = 0;
        monitor-exit(r2);	 Catch:{ all -> 0x001c }
        r0 = r1;
    L_0x000b:
        return r0;
    L_0x000c:
        r1 = serviceLifecycleCallbacks;	 Catch:{ all -> 0x001c }
        r1 = r1.size();	 Catch:{ all -> 0x001c }
        if (r1 <= 0) goto L_0x001a;
    L_0x0014:
        r1 = serviceLifecycleCallbacks;	 Catch:{ all -> 0x001c }
        r0 = r1.toArray();	 Catch:{ all -> 0x001c }
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x001c }
        goto L_0x000b;
    L_0x001c:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.PushService.collectServiceLifecycleCallbacks():java.lang.Object[]");
    }

    static void startServiceIfRequired(Context context) {
        switch (ManifestInfo.getPushType()) {
            case PPNS:
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                if (installation.getPushType() == PushType.GCM) {
                    PLog.w(TAG, "Detected a client that used to use GCM and is now using PPNS.");
                    installation.removePushType();
                    installation.removeDeviceToken();
                    installation.saveEventually();
                }
                ServiceUtils.runIntentInService(context, new Intent(ACTION_START_IF_REQUIRED), PushService.class);
                return;
            case GCM:
                GcmRegistrar.getInstance().registerAsync();
                return;
            default:
                if (!loggedStartError) {
                    PLog.e(TAG, "Tried to use push, but this app is not configured for push due to: " + ManifestInfo.getNonePushTypeLogMessage());
                    loggedStartError = true;
                    return;
                }
                return;
        }
    }

    static void stopServiceIfRequired(Context context) {
        switch (ManifestInfo.getPushType()) {
            case PPNS:
                context.stopService(new Intent(context, PushService.class));
                return;
            default:
                return;
        }
    }

    public void onCreate() {
        super.onCreate();
        if (Android.get().applicationContext() == null) {
            PLog.e(TAG, "The Parse push service cannot start because Parse.initialize has not yet been called. If you call Parse.initialize from an Activity's onCreate, that call should instead be in the Application.onCreate. Be sure your Application class is registered in your AndroidManifest.xml with the android:name property of your <application> tag.");
            stopSelf();
            return;
        }
        switch (ManifestInfo.getPushType()) {
            case PPNS:
                this.proxy = PPNSUtil.newPPNSService(this);
                break;
            case GCM:
                this.proxy = new GCMService(this);
                break;
            default:
                PLog.e(TAG, "PushService somehow started even though this device doesn't support push.");
                break;
        }
        if (this.proxy != null) {
            this.proxy.onCreate();
        }
        dispatchOnServiceCreated(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (ManifestInfo.getPushType()) {
            case PPNS:
            case GCM:
                return this.proxy.onStartCommand(intent, flags, startId);
            default:
                PLog.e(TAG, "Started push service even though no push service is enabled: " + intent);
                ServiceUtils.completeWakefulIntent(intent);
                return 2;
        }
    }

    public IBinder onBind(Intent intent) {
        throw new IllegalArgumentException("You cannot bind directly to the PushService. Use PushService.subscribe instead.");
    }

    public void onDestroy() {
        if (this.proxy != null) {
            this.proxy.onDestroy();
        }
        dispatchOnServiceDestroyed(this);
        super.onDestroy();
    }
}
