package com.parse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

class PushRouter {
    private static final String LEGACY_STATE_LOCATION = "pushState";
    private static int MAX_HISTORY_LENGTH = 10;
    private static final String STATE_LOCATION = "push";
    private static final String TAG = "com.parse.ParsePushRouter";
    private static PushRouter instance;
    private final File diskState;
    private final PushHistory history;

    public static synchronized PushRouter getInstance() {
        PushRouter pushRouter;
        synchronized (PushRouter.class) {
            if (instance == null) {
                instance = pushRouterFromState(new File(ParsePlugins.get().getFilesDir(), STATE_LOCATION), new File(ParsePlugins.get().getParseDir(), LEGACY_STATE_LOCATION), MAX_HISTORY_LENGTH);
            }
            pushRouter = instance;
        }
        return pushRouter;
    }

    static synchronized void resetInstance() {
        synchronized (PushRouter.class) {
            ParseFileUtils.deleteQuietly(new File(ParsePlugins.get().getFilesDir(), STATE_LOCATION));
            instance = null;
        }
    }

    static PushRouter pushRouterFromState(File diskState, File oldDiskState, int maxHistoryLength) {
        JSONObject historyJSON;
        JSONObject state = readJSONFileQuietly(diskState);
        if (state != null) {
            historyJSON = state.optJSONObject("history");
        } else {
            historyJSON = null;
        }
        PushHistory history = new PushHistory(maxHistoryLength, historyJSON);
        boolean didMigrate = false;
        if (history.getLastReceivedTimestamp() == null) {
            JSONObject oldState = readJSONFileQuietly(oldDiskState);
            if (oldState != null) {
                String lastTime = oldState.optString("lastTime", null);
                if (lastTime != null) {
                    history.setLastReceivedTimestamp(lastTime);
                }
                didMigrate = true;
            }
        }
        PushRouter router = new PushRouter(diskState, history);
        if (didMigrate) {
            router.saveStateToDisk();
            ParseFileUtils.deleteQuietly(oldDiskState);
        }
        return router;
    }

    private static JSONObject readJSONFileQuietly(File file) {
        JSONObject json = null;
        if (file != null) {
            try {
                json = ParseFileUtils.readFileToJSONObject(file);
            } catch (IOException e) {
            } catch (JSONException e2) {
            }
        }
        return json;
    }

    private PushRouter(File diskState, PushHistory history) {
        this.diskState = diskState;
        this.history = history;
    }

    synchronized JSONObject toJSON() throws JSONException {
        JSONObject json;
        json = new JSONObject();
        json.put("history", this.history.toJSON());
        return json;
    }

    private synchronized void saveStateToDisk() {
        Exception e;
        try {
            ParseFileUtils.writeJSONObjectToFile(this.diskState, toJSON());
        } catch (Exception e2) {
            e = e2;
            PLog.e(TAG, "Unexpected error when serializing push state to " + this.diskState, e);
        } catch (Exception e22) {
            e = e22;
            PLog.e(TAG, "Unexpected error when serializing push state to " + this.diskState, e);
        }
    }

    public synchronized String getLastReceivedTimestamp() {
        return this.history.getLastReceivedTimestamp();
    }

    public synchronized boolean handlePush(String pushId, String timestamp, String channel, JSONObject data) {
        boolean z = false;
        synchronized (this) {
            if (!(ParseTextUtils.isEmpty(pushId) || ParseTextUtils.isEmpty(timestamp))) {
                if (this.history.tryInsertPush(pushId, timestamp)) {
                    saveStateToDisk();
                    Bundle extras = new Bundle();
                    extras.putString(ParsePushBroadcastReceiver.KEY_PUSH_CHANNEL, channel);
                    if (data == null) {
                        extras.putString(ParsePushBroadcastReceiver.KEY_PUSH_DATA, "{}");
                    } else {
                        extras.putString(ParsePushBroadcastReceiver.KEY_PUSH_DATA, data.toString());
                    }
                    Intent intent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_RECEIVE);
                    intent.putExtras(extras);
                    Context context = Parse.getApplicationContext();
                    intent.setPackage(context.getPackageName());
                    context.sendBroadcast(intent);
                    z = true;
                }
            }
        }
        return z;
    }
}
