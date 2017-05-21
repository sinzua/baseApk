package com.forwardwin.base.widgets;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.ty.followboom.MainActivity;

public class PushReceiver extends BroadcastReceiver {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "title";
    private static final int OPEN_APP_NOTIFICATION_ID = 1;
    private static final String TAG = "PushReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        sendMessage(context, intent.getStringExtra("title"), intent.getStringExtra("description"));
    }

    public void sendMessage(final Context context, final String title, final String description) {
        new AsyncTask() {
            protected Object doInBackground(Object[] params) {
                NotificationManager nm = (NotificationManager) context.getSystemService("notification");
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(67108864);
                nm.notify(1, NotificationUtil.buildNotification(context, title, description, PendingIntent.getActivity(context, 1, intent, 134217728), null, true));
                return null;
            }
        }.execute(new Object[0]);
    }
}
