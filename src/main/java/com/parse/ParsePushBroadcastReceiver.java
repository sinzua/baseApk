package com.parse;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.parse.NotificationCompat.Builder;
import com.parse.NotificationCompat.Builder.BigTextStyle;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Locale;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class ParsePushBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_PUSH_DELETE = "com.parse.push.intent.DELETE";
    public static final String ACTION_PUSH_OPEN = "com.parse.push.intent.OPEN";
    public static final String ACTION_PUSH_RECEIVE = "com.parse.push.intent.RECEIVE";
    public static final String KEY_PUSH_CHANNEL = "com.parse.Channel";
    public static final String KEY_PUSH_DATA = "com.parse.Data";
    public static final String PROPERTY_PUSH_ICON = "com.parse.push.notification_icon";
    protected static final int SMALL_NOTIFICATION_MAX_CHARACTER_LIMIT = 38;
    private static final String TAG = "com.parse.ParsePushReceiver";

    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        Object obj = -1;
        switch (intentAction.hashCode()) {
            case -824874927:
                if (intentAction.equals(ACTION_PUSH_DELETE)) {
                    obj = 1;
                    break;
                }
                break;
            case -269490979:
                if (intentAction.equals(ACTION_PUSH_RECEIVE)) {
                    obj = null;
                    break;
                }
                break;
            case 374898288:
                if (intentAction.equals(ACTION_PUSH_OPEN)) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                onPushReceive(context, intent);
                return;
            case 1:
                onPushDismiss(context, intent);
                return;
            case 2:
                onPushOpen(context, intent);
                return;
            default:
                return;
        }
    }

    protected void onPushReceive(Context context, Intent intent) {
        JSONObject pushData = null;
        try {
            pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }
        String action = null;
        if (pushData != null) {
            action = pushData.optString(ParametersKeys.ACTION, null);
        }
        if (action != null) {
            Bundle extras = intent.getExtras();
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtras(extras);
            broadcastIntent.setAction(action);
            broadcastIntent.setPackage(context.getPackageName());
            context.sendBroadcast(broadcastIntent);
        }
        Notification notification = getNotification(context, intent);
        if (notification != null) {
            ParseNotificationManager.getInstance().showNotification(context, notification);
        }
    }

    protected void onPushDismiss(Context context, Intent intent) {
    }

    protected void onPushOpen(Context context, Intent intent) {
        Intent activityIntent;
        ParseAnalytics.trackAppOpenedInBackground(intent);
        String uriString = null;
        try {
            uriString = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA)).optString("uri", null);
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }
        Class<? extends Activity> cls = getActivity(context, intent);
        if (uriString != null) {
            activityIntent = new Intent("android.intent.action.VIEW", Uri.parse(uriString));
        } else {
            activityIntent = new Intent(context, cls);
        }
        activityIntent.putExtras(intent.getExtras());
        if (VERSION.SDK_INT >= 16) {
            TaskStackBuilderHelper.startActivities(context, cls, activityIntent);
            return;
        }
        activityIntent.addFlags(268435456);
        activityIntent.addFlags(67108864);
        context.startActivity(activityIntent);
    }

    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent == null) {
            return null;
        }
        Class<? extends Activity> cls = null;
        try {
            return Class.forName(launchIntent.getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            return cls;
        }
    }

    protected int getSmallIconId(Context context, Intent intent) {
        Bundle metaData = ManifestInfo.getApplicationMetadata(context);
        int explicitId = 0;
        if (metaData != null) {
            explicitId = metaData.getInt(PROPERTY_PUSH_ICON);
        }
        return explicitId != 0 ? explicitId : ManifestInfo.getIconId();
    }

    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return null;
    }

    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }

    protected Notification getNotification(Context context, Intent intent) {
        JSONObject pushData = getPushData(intent);
        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }
        String title = pushData.optString("title", ManifestInfo.getDisplayName(context));
        String alert = pushData.optString("alert", "Notification received.");
        String tickerText = String.format(Locale.getDefault(), "%s: %s", new Object[]{title, alert});
        Bundle extras = intent.getExtras();
        Random random = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode = random.nextInt();
        String packageName = context.getPackageName();
        Intent contentIntent = new Intent(ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);
        Intent deleteIntent = new Intent(ACTION_PUSH_DELETE);
        deleteIntent.putExtras(extras);
        deleteIntent.setPackage(packageName);
        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode, contentIntent, 134217728);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode, deleteIntent, 134217728);
        Builder parseBuilder = new Builder(context);
        parseBuilder.setContentTitle(title).setContentText(alert).setTicker(tickerText).setSmallIcon(getSmallIconId(context, intent)).setLargeIcon(getLargeIcon(context, intent)).setContentIntent(pContentIntent).setDeleteIntent(pDeleteIntent).setAutoCancel(true).setDefaults(-1);
        if (alert != null && alert.length() > 38) {
            parseBuilder.setStyle(new BigTextStyle().bigText(alert));
        }
        return parseBuilder.build();
    }
}
