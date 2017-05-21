package com.parse;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

class GCMService implements ProxyService {
    public static final String RECEIVE_PUSH_ACTION = "com.google.android.c2dm.intent.RECEIVE";
    public static final String REGISTER_RESPONSE_ACTION = "com.google.android.c2dm.intent.REGISTRATION";
    private static final String TAG = "GCMService";
    private ExecutorService executor;
    private final WeakReference<Service> parent;

    GCMService(Service parent) {
        this.parent = new WeakReference(parent);
    }

    public void onCreate() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void onDestroy() {
        if (this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
    }

    public int onStartCommand(final Intent intent, int flags, final int startId) {
        this.executor.execute(new Runnable() {
            public void run() {
                try {
                    GCMService.this.onHandleIntent(intent);
                } finally {
                    ServiceUtils.completeWakefulIntent(intent);
                    GCMService.this.stopParent(startId);
                }
            }
        });
        return 2;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (REGISTER_RESPONSE_ACTION.equals(action)) {
                handleGcmRegistrationIntent(intent);
            } else if (RECEIVE_PUSH_ACTION.equals(action)) {
                handleGcmPushIntent(intent);
            } else {
                PLog.e(TAG, "PushService got unknown intent in GCM mode: " + intent);
            }
        }
    }

    private void handleGcmRegistrationIntent(Intent intent) {
        try {
            GcmRegistrar.getInstance().handleRegistrationIntentAsync(intent).waitForCompletion();
        } catch (InterruptedException e) {
        }
    }

    private void handleGcmPushIntent(Intent intent) {
        String messageType = intent.getStringExtra("message_type");
        if (messageType != null) {
            PLog.i(TAG, "Ignored special message type " + messageType + " from GCM via intent " + intent);
            return;
        }
        String pushId = intent.getStringExtra("push_id");
        String timestamp = intent.getStringExtra("time");
        String dataString = intent.getStringExtra("data");
        String channel = intent.getStringExtra("channel");
        JSONObject data = null;
        if (dataString != null) {
            try {
                data = new JSONObject(dataString);
            } catch (JSONException e) {
                PLog.e(TAG, "Ignoring push because of JSON exception while processing: " + dataString, e);
                return;
            }
        }
        PushRouter.getInstance().handlePush(pushId, timestamp, channel, data);
    }

    private void stopParent(int startId) {
        Service p = (Service) this.parent.get();
        if (p != null) {
            p.stopSelf(startId);
        }
    }
}
